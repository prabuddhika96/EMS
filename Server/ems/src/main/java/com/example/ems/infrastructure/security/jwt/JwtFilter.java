package com.example.ems.infrastructure.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final String ACCESS_TOKEN = "accessToken";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String jwt;

        jwt = getJWTFromRequest(request);


        if(StringUtils.hasText(jwt) && SecurityContextHolder.getContext().getAuthentication() == null){
            String userEmail = jwtTokenProvider.extractUsername(jwt);

            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            if(jwtTokenProvider.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        if (Objects.nonNull(request.getCookies())) {
            Cookie authCookie = Arrays
                    .stream(request.getCookies()).filter(cookie -> ACCESS_TOKEN.equals(cookie.getName()))
                    .findFirst()
                    .orElse(null);
            if(Objects.isNull(authCookie) || !StringUtils.hasText(authCookie.getValue())) {
                return null;
            }
            return authCookie.getValue();
        }
        return null;
    }

}
