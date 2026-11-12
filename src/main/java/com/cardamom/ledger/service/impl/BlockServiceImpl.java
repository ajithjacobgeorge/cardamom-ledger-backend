package com.cardamom.ledger.service.impl;

import com.cardamom.ledger.domain.entity.BlockEntity;
import com.cardamom.ledger.domain.enums.CardamomVariety;
import com.cardamom.ledger.dto.request.CreateBlockRequest;
import com.cardamom.ledger.dto.request.UpdateBlockRequest;
import com.cardamom.ledger.dto.response.BlockResponse;
import com.cardamom.ledger.exception.DuplicateResourceException;
import com.cardamom.ledger.exception.ResourceNotFoundException;
import com.cardamom.ledger.repository.BlockRepository;
import com.cardamom.ledger.repository.ExpenseRepository;
import com.cardamom.ledger.repository.HarvestBatchRepository;
import com.cardamom.ledger.service.BlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BlockServiceImpl implements BlockService {

    private static final Logger log = LoggerFactory.getLogger(BlockServiceImpl.class);

    private final BlockRepository blockRepository;
    private final ExpenseRepository expenseRepository;
    private final HarvestBatchRepository harvestBatchRepository;

    public BlockServiceImpl(BlockRepository blockRepository,
                            ExpenseRepository expenseRepository,
                            HarvestBatchRepository harvestBatchRepository) {
        this.blockRepository = blockRepository;
        this.expenseRepository = expenseRepository;
        this.harvestBatchRepository = harvestBatchRepository;
    }

    @Override
    public BlockResponse createBlock(CreateBlockRequest request) {
        log.info("Creating new cardamom block: {}", request.getName());
        if (blockRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new DuplicateResourceException("Block already exists with name: " + request.getName());
        }

        BlockEntity block = new BlockEntity();
        block.setName(request.getName().trim());
        block.setVariety(request.getVariety());
        block.setTotalAreaAcres(request.getTotalAreaAcres());
        block.setPlantingYear(request.getPlantingYear());

        BlockEntity saved = blockRepository.save(block);
        log.info("Successfully created block id {} ({})", saved.getId(), saved.getName());
        return BlockResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BlockResponse getBlockById(Long id) {
        BlockEntity block = blockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Block", "id", id));
        return BlockResponse.fromEntity(block);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlockResponse> getAllBlocks(CardamomVariety variety) {
        List<BlockEntity> list = (variety != null)
                ? blockRepository.findByVariety(variety)
                : blockRepository.findAll();

        return list.stream()
                .map(BlockResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public BlockResponse updateBlock(Long id, UpdateBlockRequest request) {
        log.info("Updating cardamom block id: {}", id);
        BlockEntity block = blockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Block", "id", id));

        if (request.getName() != null && !request.getName().isBlank()) {
            String newName = request.getName().trim();
            if (!newName.equalsIgnoreCase(block.getName()) && blockRepository.existsByNameIgnoreCase(newName)) {
                throw new DuplicateResourceException("Another block already exists with name: " + newName);
            }
            block.setName(newName);
        }

        if (request.getVariety() != null) {
            block.setVariety(request.getVariety());
        }
        if (request.getTotalAreaAcres() != null) {
            block.setTotalAreaAcres(request.getTotalAreaAcres());
        }
        if (request.getPlantingYear() != null) {
            block.setPlantingYear(request.getPlantingYear());
        }

        BlockEntity updated = blockRepository.save(block);
        return BlockResponse.fromEntity(updated);
    }

    @Override
    public void deleteBlock(Long id) {
        log.info("Deleting cardamom block id: {}", id);
        if (!blockRepository.existsById(id)) {
            throw new ResourceNotFoundException("Block", "id", id);
        }

        long expenseCount = expenseRepository.findByBlockId(id).size();
        long batchCount = harvestBatchRepository.findByBlockId(id).size();
        if (expenseCount > 0 || batchCount > 0) {
            throw new IllegalStateException(String.format(
                    "Cannot delete block id %d: It has %d associated expense records and %d harvest batches.",
                    id, expenseCount, batchCount));
        }

        blockRepository.deleteById(id);
        log.info("Deleted block id: {}", id);
    }
}
