package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.Favorite;
import com.mf.server.mapper.FavoriteMapper;
import com.mf.server.service.FavoriteService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {
    private final JdbcTemplate jdbc;
    public FavoriteServiceImpl(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @Override
    public Favorite add(Long userId, String targetType, Long targetId) {
        var exist = lambdaQuery().eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, targetType)
                .eq(Favorite::getTargetId, targetId).one();
        if (exist != null) return exist;
        var del = jdbc.query("SELECT * FROM favorite WHERE user_id=? AND target_type=? AND target_id=?",
            (rs, rn) -> { var f = new Favorite(); f.setId(rs.getLong("id")); return f; },
            userId, targetType, targetId);
        if (!del.isEmpty()) { var r = del.get(0); jdbc.update("UPDATE favorite SET deleted=0 WHERE id=?", r.getId()); r.setDeleted(0); return r; }
        var f = new Favorite(); f.setUserId(userId); f.setTargetType(targetType); f.setTargetId(targetId);
        save(f); return f;
    }
}
