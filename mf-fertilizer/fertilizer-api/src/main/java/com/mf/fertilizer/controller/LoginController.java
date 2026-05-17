package com.mf.fertilizer.controller;

import com.mf.fertilizer.dto.LoginDTO;
import com.mf.fertilizer.service.SysUserService;
import com.mf.fertilizer.vo.ResultVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final SysUserService sysUserService;

    @PostMapping("/login")
    public ResultVO<?> login(@Valid @RequestBody LoginDTO dto) {
        var result = sysUserService.login(dto.getUsername(), dto.getPassword());
        return ResultVO.success(result);
    }

    @PostMapping("/logout")
    public ResultVO<?> logout(@RequestHeader("Authorization") String auth) {
        var token = auth.substring(7);
        sysUserService.logout(token);
        return ResultVO.success();
    }
}
