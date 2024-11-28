package com.example.demo.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Request'ten JWT token'ı al
        String token = getJwtFromRequest(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            // Token geçerli ise, token'dan username ve roller alınır
            String username = jwtTokenProvider.getUsernameFromToken(token);
            List<SimpleGrantedAuthority> authorities = jwtTokenProvider.getRolesFromToken(token).stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // Authentication nesnesini oluştur ve SecurityContext'e ekle
            User userDetails = new User(username, "", authorities);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Diğer filtreleri geçmesine izin ver
        filterChain.doFilter(request, response);
    }

    // Request'ten JWT token'ı almak için kullanılan metod
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // "Bearer " kısmını çıkarıyoruz
        }
        return null;
    }
}
