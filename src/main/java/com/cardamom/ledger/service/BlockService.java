package com.cardamom.ledger.service;

import com.cardamom.ledger.domain.enums.CardamomVariety;
import com.cardamom.ledger.dto.request.CreateBlockRequest;
import com.cardamom.ledger.dto.request.UpdateBlockRequest;
import com.cardamom.ledger.dto.response.BlockResponse;

import java.util.List;

public interface BlockService {

    BlockResponse createBlock(CreateBlockRequest request);

    BlockResponse getBlockById(Long id);

    List<BlockResponse> getAllBlocks(CardamomVariety variety);

    BlockResponse updateBlock(Long id, UpdateBlockRequest request);

    void deleteBlock(Long id);
}
