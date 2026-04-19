package com.example.service;
import com.example.ckfsystem.entity.*;
import com.example.ckfsystem.entity.enums.PlanStatus;
import com.example.ckfsystem.repository.ProductionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductionManagerService {

    private final ProductionPlanRepository planRepository;
    private final InventoryService inventoryService;

    
    @Transactional
    public void finalizeProductionPlan(Long planId) {
        ProductionPlan plan = planRepository.findById(planId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy kế hoạch"));

        if (plan.getStatus() == PlanStatus.COMPLETED) return;

        // Cập nhật trạng thái
        plan.setStatus(PlanStatus.COMPLETED);
        
        
        plan.getPlanDetails().forEach(detail -> {
            inventoryService.addStock(
                plan.getKitchen(),
                detail.getProduct(),
                detail.getProducedQty(),
                "Nhập kho từ kế hoạch sản xuất #" + planId,
                LocalDate.now().plusDays(detail.getProduct().getShelfLife())
            );
        });

        planRepository.save(plan);
    }
}
