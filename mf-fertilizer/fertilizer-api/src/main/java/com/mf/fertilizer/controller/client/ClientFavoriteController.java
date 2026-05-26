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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/favorites")
@RequiredArgsConstructor
public class ClientFavoriteController {
    private final FavoriteService service;
    private final JdbcTemplate jdbcTemplate;

    @GetMapping
    public ResultVO<PageVO<Favorite>> list(@ModelAttribute PageDTO page,
                                            @RequestParam(name = "targetType", required = false) String targetType,
                                            @RequestParam(name = "targetId", required = false, defaultValue = "") String targetId) {
        Long tid = !targetId.isEmpty() && !"null".equals(targetId) ? Long.valueOf(targetId) : null;
        var w = new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, UserContext.getUserId())
                .eq(targetType != null && !targetType.isEmpty(), Favorite::getTargetType, targetType)
                .eq(tid != null, Favorite::getTargetId, tid)
                .orderByDesc(Favorite::getCreateTime);
        var p = service.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @PostMapping
    public ResultVO<Favorite> add(@RequestBody Favorite fav) {
        Long userId = UserContext.getUserId();
        fav.setUserId(userId);
        // 查有效记录
        var exist = service.lambdaQuery().eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, fav.getTargetType()).eq(Favorite::getTargetId, fav.getTargetId()).one();
        if (exist != null) return ResultVO.success(exist);
        // 原生 SQL 绕过 @TableLogic，查找逻辑删除的旧记录并恢复
        var deletedList = jdbcTemplate.query(
                "SELECT * FROM favorite WHERE user_id = ? AND target_type = ? AND target_id = ?",
                (rs, rowNum) -> {
                    var f = new Favorite();
                    f.setId(rs.getLong("id"));
                    f.setUserId(rs.getLong("user_id"));
                    f.setTargetType(rs.getString("target_type"));
                    f.setTargetId(rs.getLong("target_id"));
                    f.setDeleted(rs.getInt("deleted"));
                    return f;
                }, userId, fav.getTargetType(), fav.getTargetId());
        if (!deletedList.isEmpty()) {
            var restored = deletedList.get(0);
            // updateById 也被 @TableLogic 追加 WHERE deleted=0，用原生 SQL 恢复
            jdbcTemplate.update("UPDATE favorite SET deleted=0 WHERE id=?", restored.getId());
            restored.setDeleted(0);
            return ResultVO.success(restored);
        }
        service.save(fav);
        return ResultVO.success(fav);
    }

    @DeleteMapping("/{id}")
    public ResultVO<?> remove(@PathVariable Long id) { service.removeById(id); return ResultVO.success(); }
}
