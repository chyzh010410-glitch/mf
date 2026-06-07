package com.mf.server.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.common.base.PageDTO;
import com.mf.common.base.PageVO;
import com.mf.common.base.ResultVO;
import com.mf.server.entity.User;
import com.mf.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/admin/users") @RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping public ResultVO<PageVO<User>> list(@ModelAttribute PageDTO p, @RequestParam(required=false) String keyword) {
        var w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .and(keyword!=null, q->q.like(User::getUsername,keyword).or().like(User::getPhone,keyword).or().like(User::getNickname,keyword))
                .orderByDesc(User::getCreateTime);
        var pg = service.page(new Page<>(p.getPage(),p.getSize()),w);
        return ResultVO.success(PageVO.of(pg.getTotal(),p.getPage(),p.getSize(),pg.getRecords()));
    }
    @PutMapping("/{id}/status") public ResultVO<?> toggle(@PathVariable Long id, @RequestBody java.util.Map<String,Integer> b) {
        var u=service.getById(id); u.setStatus(b.get("status")); service.updateById(u); return ResultVO.success(); }
}
