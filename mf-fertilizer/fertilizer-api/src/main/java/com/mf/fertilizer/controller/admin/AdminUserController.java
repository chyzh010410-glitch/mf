package com.mf.fertilizer.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.annotation.OperationLog;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.entity.User;
import com.mf.fertilizer.service.UserService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public ResultVO<PageVO<User>> list(@ModelAttribute PageDTO page,
                                        @RequestParam(required = false) String keyword,
                                        @RequestParam(required = false) Integer status) {
        var w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .and(StrUtil.isNotBlank(keyword), q -> q
                        .like(User::getUsername, keyword).or()
                        .like(User::getPhone, keyword).or()
                        .like(User::getNickname, keyword))
                .eq(status != null, User::getStatus, status)
                .orderByDesc(User::getCreateTime);
        var p = userService.page(new Page<>(page.getPage(), page.getSize()), w);
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @PutMapping("/{id}/status")
    @OperationLog(module = "用户管理", action = "禁用/启用")
    public ResultVO<?> toggleStatus(@PathVariable Long id, @RequestBody java.util.Map<String, Integer> body) {
        var user = userService.getById(id);
        if (user == null) return ResultVO.fail(404, "用户不存在");
        user.setStatus(body.get("status"));
        userService.updateById(user);
        return ResultVO.success();
    }
}
