package com.mf.fertilizer.controller.client;

import com.mf.fertilizer.context.UserContext;
import com.mf.fertilizer.entity.CommunityLike;
import com.mf.fertilizer.service.CommunityLikeService;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/client/likes")
@RequiredArgsConstructor
public class ClientLikeController {

    private final CommunityLikeService likeService;
    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/check")
    public ResultVO<?> check(@RequestParam String targetType, @RequestParam String targetId) {
        Long userId = UserContext.getUserId();
        boolean liked = likeService.lambdaQuery()
                .eq(CommunityLike::getUserId, userId)
                .eq(CommunityLike::getTargetType, targetType)
                .eq(CommunityLike::getTargetId, targetId)
                .count() > 0;
        long count = likeService.lambdaQuery()
                .eq(CommunityLike::getTargetType, targetType)
                .eq(CommunityLike::getTargetId, targetId)
                .count();
        return ResultVO.success(Map.of("liked", liked, "count", count));
    }

    @PostMapping
    public ResultVO<?> toggle(@RequestBody Map<String, Object> body) {
        Long userId = UserContext.getUserId();
        String targetType = (String) body.get("targetType");
        Long targetId = Long.valueOf(body.get("targetId").toString());

        var existing = likeService.lambdaQuery()
                .eq(CommunityLike::getUserId, userId)
                .eq(CommunityLike::getTargetType, targetType)
                .eq(CommunityLike::getTargetId, targetId)
                .one();
        if (existing != null) {
            likeService.removeById(existing.getId());
            return ResultVO.success(Map.of("liked", false));
        }
        // 原生 SQL 绕过 @TableLogic 过滤，查找逻辑删除的旧记录
        var deletedList = jdbcTemplate.query(
                "SELECT * FROM community_like WHERE user_id = ? AND target_type = ? AND target_id = ?",
                (rs, rowNum) -> {
                    var l = new CommunityLike();
                    l.setId(rs.getLong("id"));
                    l.setUserId(rs.getLong("user_id"));
                    l.setTargetType(rs.getString("target_type"));
                    l.setTargetId(rs.getLong("target_id"));
                    l.setDeleted(rs.getInt("deleted"));
                    return l;
                }, userId, targetType, targetId);
        if (!deletedList.isEmpty()) {
            var restored = deletedList.get(0);
            // updateById 也被 @TableLogic 追加 WHERE deleted=0，用原生 SQL 恢复
            jdbcTemplate.update("UPDATE community_like SET deleted=0 WHERE id=?", restored.getId());
        } else {
            var like = new CommunityLike();
            like.setUserId(userId);
            like.setTargetType(targetType);
            like.setTargetId(targetId);
            likeService.save(like);
        }
        return ResultVO.success(Map.of("liked", true));
    }
}
