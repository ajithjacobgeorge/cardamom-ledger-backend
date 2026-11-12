package com.cardamom.ledger.service.impl;

import com.cardamom.ledger.domain.entity.BlockEntity;
import com.cardamom.ledger.domain.entity.HarvestBatchEntity;
import com.cardamom.ledger.domain.enums.HarvestStatus;
import com.cardamom.ledger.dto.request.CreateHarvestBatchRequest;
import com.cardamom.ledger.dto.request.UpdateHarvestBatchRequest;
import com.cardamom.ledger.dto.response.HarvestBatchResponse;
import com.cardamom.ledger.exception.BusinessValidationException;
import com.cardamom.ledger.exception.ResourceNotFoundException;
import com.cardamom.ledger.repository.BlockRepository;
import com.cardamom.ledger.repository.HarvestBatchRepository;
import com.cardamom.ledger.repository.SaleTransactionRepository;
import com.cardamom.ledger.service.HarvestBatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class HarvestBatchServiceImpl implements HarvestBatchService {

    private static final Logger log = LoggerFactory.getLogger(HarvestBatchServiceImpl.class);

    private final HarvestBatchRepository harvestBatchRepository;
    private final BlockRepository blockRepository;
    private final SaleTransactionRepository saleTransactionRepository;

    public HarvestBatchServiceImpl(HarvestBatchRepository harvestBatchRepository,
                                  BlockRepository blockRepository,
                                  SaleTransactionRepository saleTransactionRepository) {
        this.harvestBatchRepository = harvestBatchRepository;
        this.blockRepository = blockRepository;
        this.saleTransactionRepository = saleTransactionRepository;
    }

    @Override
    public HarvestBatchResponse createBatch(CreateHarvestBatchRequest request) {
        log.info("Recording harvest batch for blockId: {}", request.getBlockId());
        BlockEntity block = blockRepository.findById(request.getBlockId())
                .orElseThrow(() -> new ResourceNotFoundException("Block", "id", request.getBlockId()));

        if (request.getDryWeightKg() != null && request.getDryWeightKg().compareTo(request.getGreenWeightKg()) > 0) {
            throw new BusinessValidationException(
                    "Dry weight (" + request.getDryWeightKg() + " kg) cannot exceed green harvested weight (" +
                            request.getGreenWeightKg() + " kg). Typical dry outturn is 18-24%.");
        }

        HarvestStatus initialStatus = request.getStatus();
        if (initialStatus == null) {
            initialStatus = (request.getDryWeightKg() != null && request.getDryWeightKg().compareTo(BigDecimal.ZERO) > 0)
                    ? HarvestStatus.STORED
                    : HarvestStatus.RAW;
        }

        HarvestBatchEntity batch = new HarvestBatchEntity();
        batch.setBlockId(request.getBlockId());
        batch.setHarvestDate(request.getHarvestDate());
        batch.setGreenWeightKg(request.getGreenWeightKg());
        batch.setDryWeightKg(request.getDryWeightKg());
        batch.setBhattiCost(request.getBhattiCost());
        batch.setGradeBreakdownJson(request.getGradeBreakdownJson());
        batch.setStatus(initialStatus);

        HarvestBatchEntity saved = harvestBatchRepository.save(batch);
        log.info("Created harvest batch id {} with status {}", saved.getId(), saved.getStatus());
        return HarvestBatchResponse.fromEntity(saved, block.getName(), saved.getDryWeightKg());
    }

    @Override
    @Transactional(readOnly = true)
    public HarvestBatchResponse getBatchById(Long id) {
        HarvestBatchEntity batch = harvestBatchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HarvestBatch", "id", id));

        String blockName = blockRepository.findById(batch.getBlockId())
                .map(BlockEntity::getName)
                .orElse(null);

        BigDecimal soldQty = saleTransactionRepository.sumQuantitySoldByBatchId(id);
        BigDecimal remaining = calculateRemaining(batch.getDryWeightKg(), soldQty);

        return HarvestBatchResponse.fromEntity(batch, blockName, remaining);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HarvestBatchResponse> getAllBatches(Long blockId, HarvestStatus status, LocalDate startDate, LocalDate endDate) {
        Map<Long, String> blockMap = blockRepository.findAll().stream()
                .collect(Collectors.toMap(BlockEntity::getId, BlockEntity::getName));

        List<HarvestBatchEntity> list = harvestBatchRepository.findAll();

        return list.stream()
                .filter(b -> blockId == null || blockId.equals(b.getBlockId()))
                .filter(b -> status == null || status.equals(b.getStatus()))
                .filter(b -> startDate == null || !b.getHarvestDate().isBefore(startDate))
                .filter(b -> endDate == null || !b.getHarvestDate().isAfter(endDate))
                .map(b -> {
                    BigDecimal soldQty = saleTransactionRepository.sumQuantitySoldByBatchId(b.getId());
                    BigDecimal remaining = calculateRemaining(b.getDryWeightKg(), soldQty);
                    return HarvestBatchResponse.fromEntity(b, blockMap.get(b.getBlockId()), remaining);
                })
                .collect(Collectors.toList());
    }

    @Override
    public HarvestBatchResponse updateBatch(Long id, UpdateHarvestBatchRequest request) {
        log.info("Updating harvest batch id: {}", id);
        HarvestBatchEntity batch = harvestBatchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HarvestBatch", "id", id));

        if (request.getBlockId() != null) {
            if (!blockRepository.existsById(request.getBlockId())) {
                throw new ResourceNotFoundException("Block", "id", request.getBlockId());
            }
            batch.setBlockId(request.getBlockId());
        }

        if (request.getHarvestDate() != null) {
            batch.setHarvestDate(request.getHarvestDate());
        }

        BigDecimal greenWeight = request.getGreenWeightKg() != null ? request.getGreenWeightKg() : batch.getGreenWeightKg();
        BigDecimal dryWeight = request.getDryWeightKg() != null ? request.getDryWeightKg() : batch.getDryWeightKg();

        if (dryWeight != null && greenWeight != null && dryWeight.compareTo(greenWeight) > 0) {
            throw new BusinessValidationException(
                    "Dry weight (" + dryWeight + " kg) cannot exceed green weight (" + greenWeight + " kg).");
        }

        if (request.getGreenWeightKg() != null) {
            batch.setGreenWeightKg(request.getGreenWeightKg());
        }
        if (request.getDryWeightKg() != null) {
            batch.setDryWeightKg(request.getDryWeightKg());
        }
        if (request.getBhattiCost() != null) {
            batch.setBhattiCost(request.getBhattiCost());
        }
        if (request.getGradeBreakdownJson() != null) {
            batch.setGradeBreakdownJson(request.getGradeBreakdownJson());
        }
        if (request.getStatus() != null) {
            batch.setStatus(request.getStatus());
        }

        HarvestBatchEntity updated = harvestBatchRepository.save(batch);
        String blockName = blockRepository.findById(updated.getBlockId()).map(BlockEntity::getName).orElse(null);
        BigDecimal soldQty = saleTransactionRepository.sumQuantitySoldByBatchId(id);
        BigDecimal remaining = calculateRemaining(updated.getDryWeightKg(), soldQty);

        return HarvestBatchResponse.fromEntity(updated, blockName, remaining);
    }

    @Override
    public void deleteBatch(Long id) {
        log.info("Deleting harvest batch id: {}", id);
        if (!harvestBatchRepository.existsById(id)) {
            throw new ResourceNotFoundException("HarvestBatch", "id", id);
        }

        long salesCount = saleTransactionRepository.findByBatchId(id).size();
        if (salesCount > 0) {
            throw new BusinessValidationException(
                    "Cannot delete harvest batch id " + id + ": It already has " + salesCount + " sale transactions recorded.");
        }

        harvestBatchRepository.deleteById(id);
    }

    private BigDecimal calculateRemaining(BigDecimal dryWeight, BigDecimal soldQuantity) {
        if (dryWeight == null) return BigDecimal.ZERO;
        BigDecimal sold = soldQuantity != null ? soldQuantity : BigDecimal.ZERO;
        BigDecimal rem = dryWeight.subtract(sold);
        return rem.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : rem;
    }
}
