package com.cardamom.ledger.service;

import com.cardamom.ledger.domain.enums.HarvestStatus;
import com.cardamom.ledger.dto.request.CreateHarvestBatchRequest;
import com.cardamom.ledger.dto.request.UpdateHarvestBatchRequest;
import com.cardamom.ledger.dto.response.HarvestBatchResponse;

import java.time.LocalDate;
import java.util.List;

public interface HarvestBatchService {

    HarvestBatchResponse createBatch(CreateHarvestBatchRequest request);

    HarvestBatchResponse getBatchById(Long id);

    List<HarvestBatchResponse> getAllBatches(Long blockId, HarvestStatus status, LocalDate startDate, LocalDate endDate);

    HarvestBatchResponse updateBatch(Long id, UpdateHarvestBatchRequest request);

    void deleteBatch(Long id);
}
