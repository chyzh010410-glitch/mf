package com.mf.server.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.common.base.PageDTO;
import com.mf.common.base.PageVO;
import com.mf.common.base.ResultVO;
import com.mf.common.context.UserContext;
import com.mf.server.entity.OrderEntity;
import com.mf.server.entity.OrderItem;
import com.mf.server.service.OrderEntityService;
import com.mf.server.service.OrderItemService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController @RequestMapping("/client/orders") @RequiredArgsConstructor
public class OrderController {
    private final OrderEntityService orderService;
    private final OrderItemService itemService;

    @GetMapping
    public ResultVO<PageVO<OrderEntity>> list(@ModelAttribute PageDTO p, @RequestParam(required=false) String status) {
        Long userId = UserContext.getUserId();
        var w = new LambdaQueryWrapper<OrderEntity>()
                .eq(OrderEntity::getUserId, userId)
                .eq(status != null, OrderEntity::getStatus, status)
                .orderByDesc(OrderEntity::getCreateTime);
        var pg = orderService.page(new Page<>(p.getPage(), p.getSize()), w);
        return ResultVO.success(PageVO.of(pg.getTotal(), p.getPage(), p.getSize(), pg.getRecords()));
    }

    @GetMapping("/{id}")
    public ResultVO<Map<String,Object>> detail(@PathVariable Long id) {
        var order = orderService.getById(id);
        if (order == null) return ResultVO.fail(404, "订单不存在");
        var items = itemService.lambdaQuery().eq(OrderItem::getOrderId, id).list();
        return ResultVO.success(Map.of("order", order, "items", items));
    }

    @PostMapping("/{id}/cancel")
    public ResultVO<?> cancel(@PathVariable Long id, @RequestBody(required=false) CancelRequest body) {
        var order = orderService.getById(id);
        if (order == null) return ResultVO.fail(404, "订单不存在");
        if (!"pending_pay".equals(order.getStatus())) return ResultVO.fail(400, "仅待付款订单可取消");
        order.setStatus("cancelled"); order.setCancelTime(LocalDateTime.now());
        order.setCancelReason(body != null ? body.getReason() : null);
        orderService.updateById(order);
        return ResultVO.success();
    }

    @PostMapping("/{id}/confirm")
    public ResultVO<?> confirm(@PathVariable Long id) {
        var order = orderService.getById(id);
        if (order == null) return ResultVO.fail(404, "订单不存在");
        order.setStatus("completed"); order.setCompleteTime(LocalDateTime.now());
        orderService.updateById(order);
        return ResultVO.success();
    }

    @SuppressWarnings("unchecked")
    @PostMapping
    public ResultVO<?> create(@RequestBody Map<String, Object> body) {
        try {
            var result = orderService.createOrder(
                UserContext.getUserId(),
                (List<Map<String, Object>>) body.get("items")
            );
            return ResultVO.success(result);
        } catch (RuntimeException e) {
            return ResultVO.fail(400, e.getMessage());
        }
    }

    @Data public static class CancelRequest { private String reason; }
}
