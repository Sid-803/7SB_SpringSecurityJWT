package com.example.securityJWT.util;

import com.example.securityJWT.filter.SecurityFilter;
import com.example.securityJWT.filter.UnAuthorizedUserAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptEncoder;

    @Autowired
    private UnAuthorizedUserAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private SecurityFilter secFilter;


    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return authenticationManager();
    }

    //@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptEncoder);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptEncoder);
        return authenticationProvider;
    }
    //@Override
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()    //Disabling CSRF as not using form based login
                .authorizeHttpRequests()
                .requestMatchers("/user/saveUser","/user/loginUser").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //To Verify user from second request onwards............
                .and()
                .addFilterBefore(secFilter, UsernamePasswordAuthenticationFilter.class)
                //.addFilterAfter()

                .exceptionHandling()
                .and()
                .authenticationProvider(authenticationProvider());

        return http.build();

    }



}