package com.system.controller;

import com.system.pojo.User;
import com.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // 注册接口
    @PostMapping(value = "/register", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<?> register(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password) {

        try {
            if (userService.isUserExist(email)) {
                return ResponseEntity.badRequest().body("{\"message\": \"邮箱已被注册\"}");
            }

            String encryptedPwd = DigestUtils.md5DigestAsHex(password.getBytes());

            User user = new User();
            user.setName(username);
            user.setEmail(email);
            user.setPassword(encryptedPwd);

            userService.insertUser(user);
            return ResponseEntity.ok("{\"message\": \"注册成功\"}");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"服务器异常\"}");
        }
    }

    // 登录接口
    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<?> login(
            @RequestParam("loginEmail") String email,
            @RequestParam("loginPassword") String password) {
        try {

            // 验证必需字段
            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body("{\"message\": \"邮箱和密码不能为空\"}");
            }

            // 查询用户
            User user = userService.selectUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\": \"用户不存在\"}");
            }

            // 验证密码
            String encryptedPwd = DigestUtils.md5DigestAsHex(password.getBytes());
            if (!user.getPassword().equals(encryptedPwd)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"密码错误\"}");
            }

            return ResponseEntity.ok()
                    .body("{\"message\": \"登录成功\", \"userEmail\": \"" + user.getEmail() + "\"}");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"服务器异常\"}");
        }
    }
}