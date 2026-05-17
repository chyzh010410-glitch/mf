package com.mf.fertilizer.controller.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    private Long uid(jakarta.servlet.http.HttpServletRequest r) {
        return Long.valueOf(com.mf.fertilizer.util.JwtUtil.parse(r.getHeader("Authorization").substring(7)).getId());
    }

    @GetMapping
    public ResultVO<PageVO<Favorite>> list(@ModelAttribute PageDTO page,
                                            @RequestParam(name = "targetType", required = false) String targetType,
                                            jakarta.servlet.http.HttpServletRequest req) {
        var w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, uid(req))
                .eq(targetType != null, Favorite::getTargetType, targetType)
                .orderByDesc(Favorite::getCreateTime);
        var p = service.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @PostMapping
    public ResultVO<?> add(@RequestBody Favorite fav, jakarta.servlet.http.HttpServletRequest req) {
        fav.setUserId(uid(req));
        if (service.lambdaQuery().eq(Favorite::getUserId, fav.getUserId())
                .eq(Favorite::getTargetType, fav.getTargetType()).eq(Favorite::getTargetId, fav.getTargetId()).count() == 0) {
            service.save(fav);
        }
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    public ResultVO<?> remove(@PathVariable Long id) { service.removeById(id); return ResultVO.success(); }
}
