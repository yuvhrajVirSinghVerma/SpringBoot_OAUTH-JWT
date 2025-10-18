package com.example.SpringOauth;

import com.example.SpringOauth.Filters.JwtFilter;
import com.example.SpringOauth.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    private final CustomOAuthService customOAuthService;
    public SecurityConfig(CustomOAuthService customOAuthService) {
        this.customOAuthService = customOAuthService;
    }

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http
                    .csrf(cr->cr.disable())
                    .sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/", "/error", "/login/**","/h2-console/**").permitAll() // Allow public access to home and error pages
                            .anyRequest().authenticated() // All other requests require authentication
                    )
                    .oauth2Login(oauth2 -> oauth2
                            .successHandler(customSuccessHandler)
                            .userInfoEndpoint(u->u.oidcUserService(customOAuthService))
                    )
                    .logout(logout -> logout
                            .logoutSuccessUrl("/") // Redirect to home after logout
                            .permitAll()
                    );
            http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

            http.headers(h->h.frameOptions(f->f.disable()));

            return http.build();
        }
}
