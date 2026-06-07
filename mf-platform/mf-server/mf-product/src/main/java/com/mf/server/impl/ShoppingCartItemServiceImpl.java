package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.ShoppingCartItem;
import com.mf.server.mapper.ShoppingCartItemMapper;
import com.mf.server.service.ShoppingCartItemService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShoppingCartItemServiceImpl extends ServiceImpl<ShoppingCartItemMapper, ShoppingCartItem>
        implements ShoppingCartItemService {
    private final JdbcTemplate jdbc;
    public ShoppingCartItemServiceImpl(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @Override
    public List<ShoppingCartItem> listByUser(Long userId) {
        return lambdaQuery().eq(ShoppingCartItem::getUserId, userId).list();
    }

    @Override
    public void restoreOrAdd(Long userId, Long productId, int quantity) {
        var exist = lambdaQuery().eq(ShoppingCartItem::getUserId, userId)
                .eq(ShoppingCartItem::getProductId, productId).one();
        if (exist != null) { exist.setQuantity(exist.getQuantity() + quantity); updateById(exist); return; }
        var deleted = jdbc.query("SELECT * FROM shopping_cart_item WHERE user_id=? AND product_id=?",
            (rs,rn)->{ var i=new ShoppingCartItem(); i.setId(rs.getLong("id")); i.setUserId(rs.getLong("user_id"));
                i.setProductId(rs.getLong("product_id")); i.setQuantity(rs.getInt("quantity"));
                i.setSelected(rs.getInt("selected")); i.setDeleted(rs.getInt("deleted")); return i; }, userId, productId);
        if (!deleted.isEmpty()) { var r=deleted.get(0); jdbc.update("UPDATE shopping_cart_item SET deleted=0, quantity=?, selected=1 WHERE id=?", quantity, r.getId()); return; }
        var item = new ShoppingCartItem(); item.setUserId(userId); item.setProductId(productId);
        item.setQuantity(quantity); item.setSelected(1); save(item);
    }

    @Override
    public void removeByUser(Long userId, Long productId) {
        lambdaUpdate().eq(ShoppingCartItem::getUserId, userId)
                .eq(ShoppingCartItem::getProductId, productId).remove();
    }
}
