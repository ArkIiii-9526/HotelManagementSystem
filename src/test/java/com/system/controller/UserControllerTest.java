package com.system.controller;

import com.system.pojo.User;
import com.system.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    // 注册接口测试
    @Test
    void register_WhenEmailExists_ReturnsBadRequest() {
        // 模拟邮箱已存在
        String email = "3325506868@qq.com";
        when(userService.isUserExist(email)).thenReturn(true);

        ResponseEntity<?> response = userController.register("admin", email, "Cdxgo124578@");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\": \"邮箱已被注册\"}", response.getBody());
    }

    @Test
    void register_WhenEmailNew_ReturnsSuccessAndEncryptsPassword() {
        // 模拟邮箱不存在
        String email = "new@test.com";
        when(userService.isUserExist(email)).thenReturn(false);

        ResponseEntity<?> response = userController.register("user", email, "Cdxgo124578@");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\": \"注册成功\"}", response.getBody());

        // 验证密码是否正确加密
        String encryptedPass = DigestUtils.md5DigestAsHex("Cdxgo124578@".getBytes());
        verify(userService).insertUser(argThat(user ->
                user.getEmail().equals(email) && user.getPassword().equals(encryptedPass)
        ));
    }

    @Test
    void register_WhenDatabaseError_ReturnsInternalError() {
        // 模拟插入用户时抛出异常
        String email = "error@test.com";
        when(userService.isUserExist(email)).thenReturn(false);
        doThrow(new RuntimeException()).when(userService).insertUser(any());

        ResponseEntity<?> response = userController.register("user", email, "pass123");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("{\"message\": \"服务器异常\"}", response.getBody());
    }

    // 登录接口测试
    @Test
    void login_WhenEmailMissing_ReturnsBadRequest() {
        // 邮箱为空
        ResponseEntity<?> response = userController.login("", "Cdxgo124578@");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\": \"邮箱和密码不能为空\"}", response.getBody());
    }

    @Test
    void login_WhenUserNotFound_ReturnsNotFound() {
        // 模拟用户不存在
        String email = "unknown@test.com";
        when(userService.selectUserByEmail(email)).thenReturn(null);

        ResponseEntity<?> response = userController.login(email, "pass123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("{\"message\": \"用户不存在\"}", response.getBody());
    }

    @Test
    void login_WhenPasswordWrong_ReturnsUnauthorized() {
        // 模拟用户存在但密码错误
        String email = "user@test.com";
        User user = new User();
        user.setPassword(DigestUtils.md5DigestAsHex("correct".getBytes())); // 正确密码
        when(userService.selectUserByEmail(email)).thenReturn(user);

        ResponseEntity<?> response = userController.login(email, "wrongpass"); // 错误密码

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("{\"message\": \"密码错误\"}", response.getBody());
    }

    @Test
    void login_WhenCredentialsValid_ReturnsSuccess() {
        // 模拟用户存在且密码正确
        String email = "3325506868@qq.com";
        String password = "Cdxgo124578@";
        User user = new User();
        user.setEmail(email);
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        when(userService.selectUserByEmail(email)).thenReturn(user);

        ResponseEntity<?> response = userController.login(email, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\": \"登录成功\", \"userEmail\": \"user@test.com\"}", response.getBody());
    }

    @Test
    void login_WhenDatabaseError_ReturnsInternalError() {
        // 模拟数据库查询异常
        String email = "error@test.com";
        when(userService.selectUserByEmail(email)).thenThrow(new RuntimeException());

        ResponseEntity<?> response = userController.login(email, "pass123");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("{\"message\": \"服务器异常\"}", response.getBody());
    }
}