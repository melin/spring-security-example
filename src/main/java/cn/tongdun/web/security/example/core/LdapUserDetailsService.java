package cn.tongdun.web.security.example.core;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

/**
 * Created by libinsong on 2017/6/5.
 */
public class LdapUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
        return new User(userName, "", Collections.<GrantedAuthority>emptyList());
    }
}
