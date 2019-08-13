package cn.tongdun.web.security.example.core;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by libinsong on 2017/3/27.
 */
public class UserNameAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public static final String LAST_USERNAME_KEY = "LAST_USERNAME";

    public UserNameAuthenticationFailureHandler() {
        super("/login");
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        super.onAuthenticationFailure(request, response, exception);

        String lastUserName = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);

        HttpSession session = request.getSession(false);
        if (session != null || isAllowSessionCreation()) {
            request.getSession().setAttribute(LAST_USERNAME_KEY, lastUserName);
        }
    }
}
