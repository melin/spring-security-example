package cn.tongdun.web.security.example.core;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by libinsong on 2017/5/10.
 */
public class SessionAuthenticationSuccessHandler extends
        SavedRequestAwareAuthenticationSuccessHandler {

    public static final int SESSION_TIMEOUT_IN_SECONDS = 60 * 60 * 24 * 7;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.onAuthenticationSuccess(request, response, authentication);
        request.getSession().setMaxInactiveInterval(SESSION_TIMEOUT_IN_SECONDS);
    }
}
