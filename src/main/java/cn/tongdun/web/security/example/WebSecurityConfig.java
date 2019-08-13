package cn.tongdun.web.security.example;

import cn.tongdun.web.security.example.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Created by binsong.li on 2019-08-13 13:45
 */
@Configuration
@EnableWebSecurity
@EnableRedisHttpSession
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();

        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/img/**", "/_csrf", "/error", "/login").permitAll()
                .antMatchers("/ok.htm", "/v1/**").permitAll()
                .antMatchers("/druid/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .csrf().disable()

                .formLogin()
                .defaultSuccessUrl("/", true)
                .failureHandler(new UserNameAuthenticationFailureHandler())
                .successHandler(new SessionAuthenticationSuccessHandler()).permitAll()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomLoginUrlAuthenticationEntryPoint("/login"))

                .and()
                .logout()
                .permitAll()

                .and()
                .rememberMe()
                .userDetailsService(new LdapUserDetailsService())
                //.tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(15 * 24 * 60 * 60); //15天
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new GeneralAuthProvider();
    }
}
