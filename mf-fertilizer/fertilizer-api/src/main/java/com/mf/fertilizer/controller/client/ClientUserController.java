package com.mf.fertilizer.controller.client;

import com.mf.fertilizer.entity.User;
import com.mf.fertilizer.entity.UserAddress;
import com.mf.fertilizer.service.UserService;
import com.mf.fertilizer.service.UserAddressService;
import com.mf.fertilizer.vo.ResultVO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientUserController {

    private final UserService userService;
    private final UserAddressService addressService;

    private Long getUserId(jakarta.servlet.http.HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return Long.valueOf(com.mf.fertilizer.util.JwtUtil.parse(token).getId());
    }

    @GetMapping("/user/profile")
    public ResultVO<User> profile(jakarta.servlet.http.HttpServletRequest request) {
        return ResultVO.success(userService.getById(getUserId(request)));
    }

    @PutMapping("/user/profile")
    public ResultVO<?> updateProfile(@RequestBody User form, jakarta.servlet.http.HttpServletRequest request) {
        var user = userService.getById(getUserId(request));
        if (form.getNickname() != null) user.setNickname(form.getNickname());
        if (form.getAvatar() != null) user.setAvatar(form.getAvatar());
        if (form.getEmail() != null) user.setEmail(form.getEmail());
        if (form.getGender() != null) user.setGender(form.getGender());
        userService.updateById(user);
        return ResultVO.success();
    }

    @PutMapping("/user/password")
    public ResultVO<?> changePassword(@RequestBody PasswordForm form, jakarta.servlet.http.HttpServletRequest request) {
        var user = userService.getById(getUserId(request));
        String oldPwd = DigestUtils.md5DigestAsHex(form.getOldPassword().getBytes(StandardCharsets.UTF_8));
        if (!oldPwd.equals(user.getPassword())) return ResultVO.fail(400, "原密码错误");
        user.setPassword(DigestUtils.md5DigestAsHex(form.getNewPassword().getBytes(StandardCharsets.UTF_8)));
        userService.updateById(user);
        return ResultVO.success();
    }

    @GetMapping("/addresses")
    public ResultVO<List<UserAddress>> addresses(jakarta.servlet.http.HttpServletRequest request) {
        return ResultVO.success(addressService.lambdaQuery().eq(UserAddress::getUserId, getUserId(request)).orderByDesc(UserAddress::getIsDefault).list());
    }

    @PostMapping("/addresses")
    public ResultVO<?> addAddress(@RequestBody UserAddress addr, jakarta.servlet.http.HttpServletRequest request) {
        addr.setUserId(getUserId(request));
        if (addr.getIsDefault() != null && addr.getIsDefault() == 1) clearDefault(getUserId(request));
        addressService.save(addr);
        return ResultVO.success();
    }

    @PutMapping("/addresses/{id}")
    public ResultVO<?> updateAddress(@PathVariable Long id, @RequestBody UserAddress addr) {
        var existing = addressService.getById(id);
        if (existing == null) return ResultVO.fail(404, "地址不存在");
        if (addr.getIsDefault() != null && addr.getIsDefault() == 1) clearDefault(existing.getUserId());
        addr.setId(id);
        addressService.updateById(addr);
        return ResultVO.success();
    }

    @DeleteMapping("/addresses/{id}")
    public ResultVO<?> deleteAddress(@PathVariable Long id) {
        addressService.removeById(id);
        return ResultVO.success();
    }

    @PutMapping("/addresses/{id}/default")
    public ResultVO<?> setDefault(@PathVariable Long id) {
        var addr = addressService.getById(id);
        if (addr == null) return ResultVO.fail(404, "地址不存在");
        clearDefault(addr.getUserId());
        addr.setIsDefault(1);
        addressService.updateById(addr);
        return ResultVO.success();
    }

    private void clearDefault(Long userId) {
        addressService.lambdaUpdate().eq(UserAddress::getUserId, userId).set(UserAddress::getIsDefault, 0).update();
    }

    @Data
    public static class PasswordForm {
        private String oldPassword;
        private String newPassword;
    }
}
