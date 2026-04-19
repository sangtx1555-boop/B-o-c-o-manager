package com.example.service;

import com.example.ckfsystem.entity.*;
import com.example.ckfsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryManagerService {

    private final InventoryRepository inventoryRepository;
    private final InventoryBatchRepository inventoryBatchRepository;


    public List<StockReportDTO> getStockReport(Long facilityId) {
        return inventoryRepository.findAllByFacility_FacilityId(facilityId).stream()
            .map(inv -> {
                Double totalQty = inventoryBatchRepository.sumQtyByInventoryId(inv.getInventoryId());
                return new StockReportDTO(
                    inv.getProduct().getName(),
                    inv.getProduct().getUnit().getName(),
                    totalQty != null ? totalQty : 0.0
                );
            }).collect(Collectors.toList());
    }

    
    public List<InventoryBatch> getExpiringBatches(Long facilityId, int daysThreshold) {
        return inventoryBatchRepository.findExpiringSoon(facilityId, LocalDate.now().plusDays(daysThreshold));
    }
}
