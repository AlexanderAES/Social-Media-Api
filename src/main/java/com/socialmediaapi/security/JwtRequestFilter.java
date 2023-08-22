package com.socialmediaapi.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

import static com.socialmediaapi.security.SecurityConstants.HEADER_STRING;
import static com.socialmediaapi.security.SecurityConstants.TOKEN_PREFIX;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HEADER_STRING);
        String token = null;
        String username = null;
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            token = header.substring(7);
            try {
                username = jwtTokenProvider.getUsername(token);
            } catch (ExpiredJwtException e) {
                log.debug("Время жизни токена истекло");
            } catch (SignatureException e) {
                log.debug("Неверная подпись");
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username, null, jwtTokenProvider.getRoles(token)
                    .stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList())
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }
}
