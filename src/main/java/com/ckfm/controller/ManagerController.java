package com.example.controller;

import com.example.ckfsystem.service.InventoryManagerService;
import com.example.ckfsystem.service.ProductionManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final InventoryManagerService inventoryManagerService;
    private final ProductionManagerService productionManagerService;

    @GetMapping("/reports/stock/{facilityId}")
    public ResponseEntity<?> viewStockReport(@PathVariable Long facilityId) {
        return ResponseEntity.ok(inventoryManagerService.getStockReport(facilityId));
    }

    @GetMapping("/reports/expiring/{facilityId}")
    public ResponseEntity<?> viewExpiringSoon(@PathVariable Long facilityId) {
        return ResponseEntity.ok(inventoryManagerService.getExpiringBatches(facilityId, 7)); // Cảnh báo trước 7 ngày
    }

    @PostMapping("/production/finalize/{planId}")
    public ResponseEntity<?> finalizePlan(@PathVariable Long planId) {
        productionManagerService.finalizeProductionPlan(planId);
        return ResponseEntity.ok("Đã chốt kế hoạch và cập nhật tồn kho thành phẩm!");
    }
}
