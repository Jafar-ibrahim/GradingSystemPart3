package com.example.gradingsystempart3.Security;



import com.example.gradingsystempart3.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Autowired
    public AuthSuccessHandler(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        String username = authentication.getName();
        int currentUserId = userService.getIdByUsername(username);
        if (roles.contains("ADMIN")) {
            redirectStrategy.sendRedirect(request, response, "/admin/admin_view?current_user_id="+currentUserId);
        }else if(roles.contains("INSTRUCTOR")){
            int instructorId = userService.getSpecificId(currentUserId,2);
            redirectStrategy.sendRedirect(request, response, "/instructor/instructor_view?current_user_id="
                    +currentUserId+"&instructor_id="+instructorId);
        }else {
            int studentId = userService.getSpecificId(currentUserId,3);
            redirectStrategy.sendRedirect(request, response, "/student/student_view?current_user_id="
                    +currentUserId+"&student_id="+ studentId);
        }

    }
}

