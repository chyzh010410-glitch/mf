package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.CommunityLike;
import com.mf.server.mapper.CommunityLikeMapper;
import com.mf.server.service.CommunityLikeService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class CommunityLikeServiceImpl extends ServiceImpl<CommunityLikeMapper, CommunityLike>
        implements CommunityLikeService {
    private final JdbcTemplate jdbc;
    public CommunityLikeServiceImpl(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @Override
    public Map<String, Object> check(Long userId, String targetType, Long targetId) {
        boolean liked = lambdaQuery().eq(CommunityLike::getUserId, userId)
                .eq(CommunityLike::getTargetType, targetType)
                .eq(CommunityLike::getTargetId, targetId).count() > 0;
        long count = lambdaQuery().eq(CommunityLike::getTargetType, targetType)
                .eq(CommunityLike::getTargetId, targetId).count();
        return Map.of("liked", liked, "count", count);
    }

    @Override
    public Map<String, Object> toggle(Long userId, String targetType, Long targetId) {
        var exist = lambdaQuery().eq(CommunityLike::getUserId, userId)
                .eq(CommunityLike::getTargetType, targetType)
                .eq(CommunityLike::getTargetId, targetId).one();
        if (exist != null) { removeById(exist.getId()); return Map.of("liked", false); }
        var del = jdbc.query("SELECT * FROM community_like WHERE user_id=? AND target_type=? AND target_id=?",
            (rs, rn) -> { var l = new CommunityLike(); l.setId(rs.getLong("id")); return l; },
            userId, targetType, targetId);
        if (!del.isEmpty()) { jdbc.update("UPDATE community_like SET deleted=0 WHERE id=?", del.get(0).getId()); }
        else { var l = new CommunityLike(); l.setUserId(userId); l.setTargetType(targetType); l.setTargetId(targetId); save(l); }
        return Map.of("liked", true);
    }
}
