package ru.oeru.SpringBoot.configurations.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ru.oeru.SpringBoot.model.PossibleRoles;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains(PossibleRoles.getAdminRole())){
            httpServletResponse.sendRedirect("/admin");
        } else if (roles.contains(PossibleRoles.getUserRole())){
            httpServletResponse.sendRedirect("/userdetails");
        }
    }
}
