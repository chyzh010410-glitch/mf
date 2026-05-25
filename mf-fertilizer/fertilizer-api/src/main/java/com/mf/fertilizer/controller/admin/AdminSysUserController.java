package com.mf.fertilizer.controller.admin;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.fertilizer.annotation.OperationLog;
import com.mf.fertilizer.dto.PageDTO;
import com.mf.fertilizer.entity.SysUser;
import com.mf.fertilizer.service.SysUserService;
import com.mf.fertilizer.vo.PageVO;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/admins")
@RequiredArgsConstructor
public class AdminSysUserController {

    private final SysUserService sysUserService;

    @GetMapping
    public ResultVO<PageVO<SysUser>> list(@ModelAttribute PageDTO page,
                                           @RequestParam(required = false) String keyword,
                                           @RequestParam(required = false) Integer status) {
        var w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysUser>()
                .and(StrUtil.isNotBlank(keyword), q -> q
                        .like(SysUser::getUsername, keyword).or()
                        .like(SysUser::getRealName, keyword))
                .eq(status != null, SysUser::getStatus, status)
                .orderByDesc(SysUser::getCreateTime);
        var p = sysUserService.page(new Page<>(page.getPage(), page.getSize()), w);
        // 脱敏密码
        p.getRecords().forEach(u -> u.setPassword(null));
        return ResultVO.success(PageVO.of(p.getTotal(), page.getPage(), page.getSize(), p.getRecords()));
    }

    @GetMapping("/{id}")
    public ResultVO<SysUser> detail(@PathVariable Long id) {
        var u = sysUserService.getById(id);
        if (u == null) return ResultVO.fail(404, "管理员不存在");
        u.setPassword(null);
        return ResultVO.success(u);
    }

    @PostMapping
    @OperationLog(module = "管理员管理", action = "新增")
    public ResultVO<?> save(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        if (StrUtil.isBlank(username)) return ResultVO.fail(400, "用户名不能为空");
        if (StrUtil.isBlank(password)) return ResultVO.fail(400, "密码不能为空");

        var exist = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
        if (exist != null) return ResultVO.fail(400, "用户名已存在");

        var u = new SysUser();
        u.setUsername(username);
        u.setPassword(BCrypt.hashpw(password));
        u.setRealName((String) body.getOrDefault("realName", username));
        u.setRole((String) body.getOrDefault("role", "operator"));
        u.setStatus(1);
        sysUserService.save(u);
        return ResultVO.success();
    }

    @PutMapping("/{id}")
    @OperationLog(module = "管理员管理", action = "编辑")
    public ResultVO<?> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        var u = sysUserService.getById(id);
        if (u == null) return ResultVO.fail(404, "管理员不存在");

        String password = (String) body.get("password");
        if (StrUtil.isNotBlank(password)) u.setPassword(BCrypt.hashpw(password));
        if (body.containsKey("realName")) u.setRealName((String) body.get("realName"));
        if (body.containsKey("role")) u.setRole((String) body.get("role"));
        sysUserService.updateById(u);
        return ResultVO.success();
    }

    @PutMapping("/{id}/status")
    @OperationLog(module = "管理员管理", action = "禁用/启用")
    public ResultVO<?> toggleStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        var u = sysUserService.getById(id);
        if (u == null) return ResultVO.fail(404, "管理员不存在");
        u.setStatus(body.get("status"));
        sysUserService.updateById(u);
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    @OperationLog(module = "管理员管理", action = "删除")
    public ResultVO<?> delete(@PathVariable Long id) {
        var u = sysUserService.getById(id);
        if (u == null) return ResultVO.fail(404, "管理员不存在");
        u.setStatus(0);
        sysUserService.updateById(u);
        return ResultVO.success();
    }
}
