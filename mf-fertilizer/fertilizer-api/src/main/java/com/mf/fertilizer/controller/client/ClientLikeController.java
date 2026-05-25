package com.mf.fertilizer.controller.client;

import com.mf.fertilizer.context.UserContext;
import com.mf.fertilizer.entity.CommunityLike;
import com.mf.fertilizer.service.CommunityLikeService;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/client/likes")
@RequiredArgsConstructor
public class ClientLikeController {

    private final CommunityLikeService likeService;

    @GetMapping("/check")
    public ResultVO<?> check(@RequestParam String targetType, @RequestParam Long targetId) {
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
        } else {
            var like = new CommunityLike();
            like.setUserId(userId);
            like.setTargetType(targetType);
            like.setTargetId(targetId);
            likeService.save(like);
            return ResultVO.success(Map.of("liked", true));
        }
    }
}
