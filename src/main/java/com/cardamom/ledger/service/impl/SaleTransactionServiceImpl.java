package com.cardamom.ledger.service.impl;

import com.cardamom.ledger.domain.entity.HarvestBatchEntity;
import com.cardamom.ledger.domain.entity.SaleTransactionEntity;
import com.cardamom.ledger.domain.enums.HarvestStatus;
import com.cardamom.ledger.dto.request.CreateSaleTransactionRequest;
import com.cardamom.ledger.dto.request.UpdateSaleTransactionRequest;
import com.cardamom.ledger.dto.response.SaleTransactionResponse;
import com.cardamom.ledger.exception.BusinessValidationException;
import com.cardamom.ledger.exception.ResourceNotFoundException;
import com.cardamom.ledger.repository.HarvestBatchRepository;
import com.cardamom.ledger.repository.SaleTransactionRepository;
import com.cardamom.ledger.service.SaleTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SaleTransactionServiceImpl implements SaleTransactionService {

    private static final Logger log = LoggerFactory.getLogger(SaleTransactionServiceImpl.class);

    private final SaleTransactionRepository saleTransactionRepository;
    private final HarvestBatchRepository harvestBatchRepository;

    public SaleTransactionServiceImpl(SaleTransactionRepository saleTransactionRepository,
                                      HarvestBatchRepository harvestBatchRepository) {
        this.saleTransactionRepository = saleTransactionRepository;
        this.harvestBatchRepository = harvestBatchRepository;
    }

    @Override
    public SaleTransactionResponse createSale(CreateSaleTransactionRequest request) {
        log.info("Processing sale transaction for batchId: {}, buyer: {}", request.getBatchId(), request.getBuyerOrAuctionHouse());

        HarvestBatchEntity batch = harvestBatchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException("HarvestBatch", "id", request.getBatchId()));

        if (batch.getDryWeightKg() == null || batch.getDryWeightKg().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessValidationException(
                    "Cannot execute sale for batch id " + batch.getId() + " because cured dry weight has not been recorded yet.");
        }

        BigDecimal existingSoldKg = saleTransactionRepository.sumQuantitySoldByBatchId(batch.getId());
        BigDecimal availableKg = batch.getDryWeightKg().subtract(existingSoldKg);

        if (request.getTotalQuantitySoldKg().compareTo(availableKg) > 0) {
            throw new BusinessValidationException(String.format(
                    "Insufficient cured stock in batch %d. Available: %s kg, Requested sale: %s kg.",
                    batch.getId(), availableKg, request.getTotalQuantitySoldKg()));
        }

        BigDecimal gross = request.getTotalQuantitySoldKg().multiply(request.getPricePerKg());
        if (request.getCommissionDeduction().compareTo(gross) > 0) {
            throw new BusinessValidationException(
                    "Commission deduction (" + request.getCommissionDeduction() + ") cannot exceed gross realization (" + gross + ").");
        }

        BigDecimal netRealization = gross.subtract(request.getCommissionDeduction());

        SaleTransactionEntity entity = new SaleTransactionEntity();
        entity.setBatchId(request.getBatchId());
        entity.setBuyerOrAuctionHouse(request.getBuyerOrAuctionHouse().trim());
        entity.setTotalQuantitySoldKg(request.getTotalQuantitySoldKg());
        entity.setPricePerKg(request.getPricePerKg());
        entity.setCommissionDeduction(request.getCommissionDeduction());
        entity.setNetRealizationAmount(netRealization);
        entity.setSaleDate(request.getSaleDate());

        SaleTransactionEntity saved = saleTransactionRepository.save(entity);

        // Update batch status if stock depleted
        BigDecimal newTotalSold = existingSoldKg.add(request.getTotalQuantitySoldKg());
        if (newTotalSold.compareTo(batch.getDryWeightKg()) >= 0) {
            batch.setStatus(HarvestStatus.SOLD);
            harvestBatchRepository.save(batch);
            log.info("Batch id {} is now completely SOLD.", batch.getId());
        }

        log.info("Recorded sale id {} net realization {}", saved.getId(), saved.getNetRealizationAmount());
        return SaleTransactionResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SaleTransactionResponse getSaleById(Long id) {
        SaleTransactionEntity entity = saleTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleTransaction", "id", id));
        return SaleTransactionResponse.fromEntity(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleTransactionResponse> getAllSales(Long batchId, String buyer, LocalDate startDate, LocalDate endDate) {
        List<SaleTransactionEntity> list = saleTransactionRepository.findAll();

        return list.stream()
                .filter(s -> batchId == null || batchId.equals(s.getBatchId()))
                .filter(s -> buyer == null || s.getBuyerOrAuctionHouse().toLowerCase().contains(buyer.toLowerCase().trim()))
                .filter(s -> startDate == null || !s.getSaleDate().isBefore(startDate))
                .filter(s -> endDate == null || !s.getSaleDate().isAfter(endDate))
                .map(SaleTransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public SaleTransactionResponse updateSale(Long id, UpdateSaleTransactionRequest request) {
        log.info("Updating sale transaction id: {}", id);
        SaleTransactionEntity entity = saleTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleTransaction", "id", id));

        HarvestBatchEntity batch = harvestBatchRepository.findById(
                request.getBatchId() != null ? request.getBatchId() : entity.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException("HarvestBatch", "id",
                        request.getBatchId() != null ? request.getBatchId() : entity.getBatchId()));

        if (request.getBatchId() != null) {
            entity.setBatchId(request.getBatchId());
        }
        if (request.getBuyerOrAuctionHouse() != null && !request.getBuyerOrAuctionHouse().isBlank()) {
            entity.setBuyerOrAuctionHouse(request.getBuyerOrAuctionHouse().trim());
        }
        if (request.getTotalQuantitySoldKg() != null) {
            entity.setTotalQuantitySoldKg(request.getTotalQuantitySoldKg());
        }
        if (request.getPricePerKg() != null) {
            entity.setPricePerKg(request.getPricePerKg());
        }
        if (request.getCommissionDeduction() != null) {
            entity.setCommissionDeduction(request.getCommissionDeduction());
        }
        if (request.getSaleDate() != null) {
            entity.setSaleDate(request.getSaleDate());
        }

        BigDecimal gross = entity.getTotalQuantitySoldKg().multiply(entity.getPricePerKg());
        if (entity.getCommissionDeduction().compareTo(gross) > 0) {
            throw new BusinessValidationException("Commission deduction cannot exceed gross sale value.");
        }
        entity.setNetRealizationAmount(gross.subtract(entity.getCommissionDeduction()));

        SaleTransactionEntity updated = saleTransactionRepository.save(entity);
        return SaleTransactionResponse.fromEntity(updated);
    }

    @Override
    public void deleteSale(Long id) {
        log.info("Deleting sale transaction id: {}", id);
        SaleTransactionEntity entity = saleTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SaleTransaction", "id", id));

        Long batchId = entity.getBatchId();
        saleTransactionRepository.deleteById(id);

        // Re-evaluate batch status
        harvestBatchRepository.findById(batchId).ifPresent(batch -> {
            BigDecimal remainingSold = saleTransactionRepository.sumQuantitySoldByBatchId(batchId);
            if (batch.getDryWeightKg() != null && remainingSold.compareTo(batch.getDryWeightKg()) < 0) {
                batch.setStatus(HarvestStatus.STORED);
                harvestBatchRepository.save(batch);
            }
        });
    }
}
