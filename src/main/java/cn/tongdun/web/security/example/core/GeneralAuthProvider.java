package cn.tongdun.web.security.example.core;

import cn.tongdun.bee.model.LoginUserDetails;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

/**
 * Created by xiazhen on 17/1/4.
 */
public class GeneralAuthProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralAuthProvider.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String loginId = (String) authentication.getPrincipal();
        final String rawPassword = (String) authentication.getCredentials();

        try {
            UserInfoEntity userInfo = this.userInfoService.queryUser(loginId);

            if (!passwordEncoder.matches(rawPassword, userInfo.getPassword())) {
                throw new AuthenticationCredentialsNotFoundException("invalid username or password");
            }

            LoginUserDetails user = new LoginUserDetails(loginId, userInfo.getCnName(), "", Lists.newArrayList());
            user.setEmail(userInfo.getMail());
            user.setAppkey(userInfo.getAppkey());
            user.setTenantCode(userInfo.getTenantCode());

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("user");
            UsernamePasswordAuthenticationToken ua = new UsernamePasswordAuthenticationToken(
                    user, null, Lists.newArrayList(authority));

            LOGGER.info("{} 登录成功", loginId);
            return ua;
        } catch (Exception e) {
            LOGGER.error(loginId + " 登录失败", e);
            throw new AuthenticationCredentialsNotFoundException("invalid username or password", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
