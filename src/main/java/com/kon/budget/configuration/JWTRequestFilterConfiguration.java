package com.kon.budget.configuration;

import com.kon.budget.service.JWTService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@AllArgsConstructor
@Service
public class JWTRequestFilterConfiguration extends OncePerRequestFilter {

    //filtoranie zapytan przychodzacych, sprawdza czy wystpeuje tam hash autentykujacy  i wyciagania z niego infoemacjie
    private final UserDetailsService userDetailsService;
    private final JWTService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var authHandler = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (Objects.nonNull(authHandler) && authHandler.startsWith("Bearer ")) {
            jwt = authHandler.substring("Bearer ".length());
            username = jwtService.extractUserName(jwt);
        }

        if (Objects.nonNull(username)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(jwt, userDetails)) {
                var userPasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                userPasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(userPasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
