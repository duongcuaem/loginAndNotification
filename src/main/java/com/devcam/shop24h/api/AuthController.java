package com.devcam.shop24h.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;


@Controller
@CrossOrigin(value = "*" , maxAge = -1)
public class AuthController {

    // GHI CHÚ CÁCH SỬ DỤNG KHI KIỂM TRA QUYỀN NGƯỜI DÙNG
    // hasAnyAuthority : Kiểm tra nhiều quyền có 1 trong các quyền là được
    // hasAuthority : Cần chính xác quyền mới được 
    // hasRole : tự động thêm tiền tố "ROLE_" vào trước quyền bạn yêu càu
    // hasAnyAuthority : tự động thêm tiền tố "ROLE_" vào trước quyền bạn yêu càu , và truyền được nhiều role


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request) {
        return "home"; // Trả về tên view của trang chủ
    }

    
    @GetMapping("/index")
    public String index(Model model, HttpServletRequest request) {
        return "index"; // Trả về tên view của trang chủ
    }

    @GetMapping("/loginSuccess")
    public String userInfo(HttpServletRequest request, Model model) {
        return "loginSuccess";
    }

    @GetMapping("/oauth2/redirect")
    public String oauth2Redirect(@AuthenticationPrincipal OidcUser oidcUser) {
        // Xử lý sau khi đăng nhập thành công qua Google
        return "redirect:/home";
    }

    @GetMapping("/hello")
    @PreAuthorize("hasAnyAuthority('USER_CREATE', 'USER_UPDATE')")
    public ResponseEntity<Object> hello() {
        return ResponseEntity.ok("hello  have USER_READ OR USER_CREATE OR USER_UPDATE oR USER_DELETE");
    }
    
    @GetMapping("/hello2")
    @PreAuthorize("hasAnyAuthority('USER_READ','USER_DELETE')")
    public ResponseEntity<Object> hello2() {
        return ResponseEntity.ok("hello 2 have USER_READ OR USER_DELETE");
    }

    @GetMapping("/hello3")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<Object> hello3() {
        return ResponseEntity.ok("hello cho ADMIN va CUSTOMER");
    }
    
    @GetMapping("/hello4")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> hello4() {
        return ResponseEntity.ok("hello chi cho ADMIN");
    }

    // Api này check cần quyền MANAGER
    @GetMapping("/hello6")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Object> hello6() {
        return ResponseEntity.ok("hello chi cho quản lý");
    }

    @GetMapping("/hello7")
    @PreAuthorize("hasAnyAuthority('USER_DELETE')")
    public ResponseEntity<Object> hello7() {
        return ResponseEntity.ok("dành cho người có quyền xóa tài khoản");
    }

    @GetMapping("/hello5")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> hello5() {
        return ResponseEntity.ok("hello chi cho ADMIN full tất cả quyền");
    }


    // Error
    @GetMapping("/oauth2/error")
    public String oauth2Error(HttpServletRequest request, Model model) {
        // Lấy thông tin lỗi từ request và thêm vào model
        String errorMessage = (String) request.getAttribute("error");
        model.addAttribute("error", errorMessage);
        // Log lỗi để kiểm tra
        System.err.println("OAuth2 Error: " + errorMessage);
        return "oauth2_error";
    }
}
