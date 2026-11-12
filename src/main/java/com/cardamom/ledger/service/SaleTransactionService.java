package com.cardamom.ledger.service;

import com.cardamom.ledger.dto.request.CreateSaleTransactionRequest;
import com.cardamom.ledger.dto.request.UpdateSaleTransactionRequest;
import com.cardamom.ledger.dto.response.SaleTransactionResponse;

import java.time.LocalDate;
import java.util.List;

public interface SaleTransactionService {

    SaleTransactionResponse createSale(CreateSaleTransactionRequest request);

    SaleTransactionResponse getSaleById(Long id);

    List<SaleTransactionResponse> getAllSales(Long batchId, String buyer, LocalDate startDate, LocalDate endDate);

    SaleTransactionResponse updateSale(Long id, UpdateSaleTransactionRequest request);

    void deleteSale(Long id);
}
