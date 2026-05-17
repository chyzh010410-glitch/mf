package com.mf.fertilizer.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.dto.admin.OrderShipDTO;
import com.mf.fertilizer.entity.OrderEntity;
import com.mf.fertilizer.entity.OrderItem;
import com.mf.fertilizer.service.OrderEntityService;
import com.mf.fertilizer.service.OrderItemService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderEntityService orderService;
    private final OrderItemService orderItemService;

    @GetMapping
    public ResultVO<PageVO<OrderEntity>> list(@ModelAttribute PageDTO page,
                                              @RequestParam(name = "status", required = false) String status,
                                              @RequestParam(name = "orderNo", required = false) String orderNo) {
        var w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OrderEntity>()
                .eq(status != null, OrderEntity::getStatus, status)
                .eq(orderNo != null, OrderEntity::getOrderNo, orderNo)
                .orderByDesc(OrderEntity::getCreateTime);
        var p = orderService.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @GetMapping("/{id}")
    public ResultVO<?> detail(@PathVariable Long id) {
        var order = orderService.getById(id);
        if (order == null) return ResultVO.fail(404, "订单不存在");
        var items = orderItemService.lambdaQuery().eq(OrderItem::getOrderId, id).list();
        return ResultVO.success(Map.of("order", order, "items", items));
    }

    @PostMapping("/{id}/ship")
    public ResultVO<?> ship(@PathVariable Long id, @RequestBody OrderShipDTO dto) {
        var order = orderService.getById(id);
        if (order == null) return ResultVO.fail(404, "订单不存在");
        if (!"pending_ship".equals(order.getStatus())) {
            return ResultVO.fail(400, "当前状态不可发货");
        }
        order.setStatus("shipped");
        order.setShipTime(LocalDateTime.now());
        order.setAdminRemark("物流: " + dto.getLogisticsCompany() + " " + dto.getLogisticsNo());
        orderService.updateById(order);
        return ResultVO.success();
    }

    @GetMapping("/statistics")
    public ResultVO<?> statistics() {
        long total = orderService.count();
        long pendingPay = orderService.lambdaQuery().eq(OrderEntity::getStatus, "pending_pay").count();
        long pendingShip = orderService.lambdaQuery().eq(OrderEntity::getStatus, "pending_ship").count();
        long shipped = orderService.lambdaQuery().eq(OrderEntity::getStatus, "shipped").count();
        long completed = orderService.lambdaQuery().eq(OrderEntity::getStatus, "completed").count();
        return ResultVO.success(Map.of(
            "total", total, "pendingPay", pendingPay,
            "pendingShip", pendingShip, "shipped", shipped, "completed", completed
        ));
    }

    @PostMapping("/{id}/status")
    public ResultVO<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        var order = orderService.getById(id);
        if (order == null) return ResultVO.fail(404, "订单不存在");
        order.setStatus(body.get("status"));
        orderService.updateById(order);
        return ResultVO.success();
    }
}
