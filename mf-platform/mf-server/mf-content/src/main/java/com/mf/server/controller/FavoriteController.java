package com.mf.server.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.common.base.PageDTO;
import com.mf.common.base.PageVO;
import com.mf.common.base.ResultVO;
import com.mf.common.context.UserContext;
import com.mf.server.entity.Favorite;
import com.mf.server.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/client/favorites") @RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService service;
    @GetMapping public ResultVO<PageVO<Favorite>> list(@ModelAttribute PageDTO p, @RequestParam(required=false) String targetType, @RequestParam(required=false) String targetId) {
        Long tid=targetId!=null&&!targetId.isEmpty()&&!"null".equals(targetId)?Long.valueOf(targetId):null;
        var w=new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId,UserContext.getUserId())
                .eq(targetType!=null&&!targetType.isEmpty(),Favorite::getTargetType,targetType)
                .eq(tid!=null,Favorite::getTargetId,tid).orderByDesc(Favorite::getCreateTime);
        var pg=service.page(new Page<>(p.getPage(),p.getSize()),w);
        return ResultVO.success(PageVO.of(pg.getTotal(),p.getPage(),p.getSize(),pg.getRecords()));
    }
    @PostMapping public ResultVO<Favorite> add(@RequestBody Favorite fav) {
        return ResultVO.success(service.add(UserContext.getUserId(), fav.getTargetType(), fav.getTargetId()));
    }
    @DeleteMapping("/{id}") public ResultVO<?> remove(@PathVariable Long id) { service.removeById(id); return ResultVO.success(); }
}
