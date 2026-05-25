package com.mf.fertilizer.controller.client;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.context.UserContext;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.entity.Favorite;
import com.mf.fertilizer.service.FavoriteService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/favorites")
@RequiredArgsConstructor
public class ClientFavoriteController {
    private final FavoriteService service;

    @GetMapping
    public ResultVO<PageVO<Favorite>> list(@ModelAttribute PageDTO page,
                                            @RequestParam(name = "targetType", required = false) String targetType) {
        var w = new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, UserContext.getUserId())
                .eq(targetType != null, Favorite::getTargetType, targetType)
                .orderByDesc(Favorite::getCreateTime);
        var p = service.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @PostMapping
    public ResultVO<?> add(@RequestBody Favorite fav) {
        Long userId = UserContext.getUserId();
        fav.setUserId(userId);
        if (service.lambdaQuery().eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, fav.getTargetType()).eq(Favorite::getTargetId, fav.getTargetId()).count() == 0) {
            service.save(fav);
        }
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    public ResultVO<?> remove(@PathVariable Long id) { service.removeById(id); return ResultVO.success(); }
}
