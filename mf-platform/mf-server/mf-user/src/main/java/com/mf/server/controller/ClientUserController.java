package com.mf.server.controller;
import com.mf.common.base.ResultVO;
import com.mf.common.context.UserContext;
import com.mf.server.entity.User;
import com.mf.server.entity.UserAddress;
import com.mf.server.service.UserService;
import com.mf.server.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/client") @RequiredArgsConstructor
public class ClientUserController {
    private final UserService userService;
    private final UserAddressService addressService;

    @GetMapping("/user/profile")
    public ResultVO<User> profile() { return ResultVO.success(userService.getById(UserContext.getUserId())); }

    @PutMapping("/user/profile")
    public ResultVO<?> updateProfile(@RequestBody User form) {
        var u = userService.getById(UserContext.getUserId());
        if (form.getNickname() != null) u.setNickname(form.getNickname());
        if (form.getAvatar() != null) u.setAvatar(form.getAvatar());
        if (form.getEmail() != null) u.setEmail(form.getEmail());
        userService.updateById(u); return ResultVO.success();
    }

    @PutMapping("/user/password")
    public ResultVO<?> changePassword(@RequestBody Map<String,String> body) {
        var u = userService.getById(UserContext.getUserId());
        if (!DigestUtils.md5DigestAsHex(body.get("oldPassword").getBytes(StandardCharsets.UTF_8)).equals(u.getPassword()))
            return ResultVO.fail(400, "原密码错误");
        u.setPassword(DigestUtils.md5DigestAsHex(body.get("newPassword").getBytes(StandardCharsets.UTF_8)));
        userService.updateById(u); return ResultVO.success();
    }

    @GetMapping("/addresses") public ResultVO<List<UserAddress>> addresses() {
        return ResultVO.success(addressService.lambdaQuery().eq(UserAddress::getUserId, UserContext.getUserId()).orderByDesc(UserAddress::getIsDefault).list());
    }
    @PostMapping("/addresses") public ResultVO<?> addAddress(@RequestBody UserAddress a) {
        a.setUserId(UserContext.getUserId()); addressService.save(a); return ResultVO.success();
    }
    @PutMapping("/addresses/{id}") public ResultVO<?> updateAddress(@PathVariable Long id, @RequestBody UserAddress a) {
        a.setId(id); addressService.updateById(a); return ResultVO.success();
    }
    @DeleteMapping("/addresses/{id}") public ResultVO<?> deleteAddress(@PathVariable Long id) {
        addressService.removeById(id); return ResultVO.success();
    }
}
