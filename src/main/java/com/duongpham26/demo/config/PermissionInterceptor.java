package com.duongpham26.demo.config;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import com.duongpham26.demo.entity.Permission;
import com.duongpham26.demo.entity.Role;
import com.duongpham26.demo.entity.User;
import com.duongpham26.demo.service.UserService;
import com.duongpham26.demo.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private final UserService userService = null;

    // public PermissionInterceptor(UserService userService) {
    // this.userService = userService;
    // }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();

        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        // check permission
        Optional<String> userInfo = SecurityUtil.getCurrentUserLogin();
        String email = userInfo.isPresent() ? userInfo.get() : "";

        if (email != null && !email.isEmpty()) {
            User user = this.userService.handleGetUserByUserName(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> listPermissions = role.getPermissions();
                    boolean isAllow = listPermissions.stream()
                            .anyMatch(permission -> permission.getApiPath().equals(path)
                                    && permission.getMethod().equals(httpMethod));
                    System.out.println(">>> is allow" + isAllow);
                }
            }
        }

        return true; // quyet dinh di tiep hay khong
    }
}
