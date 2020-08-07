package com.krasimirkolchev.photomag.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .antMatchers("/", "/about", "/contacts", "/users/register").permitAll()
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                        .loginPage("/users/login").permitAll()
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home")
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID")
                .and()
                    .exceptionHandling()
                        .accessDeniedPage("/error")
                .and()
                .csrf().disable()
                .cors().disable();

    }

}
