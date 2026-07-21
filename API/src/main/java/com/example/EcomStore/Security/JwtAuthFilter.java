package com.example.EcomStore.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authHeader.substring(7);

    if (jwtUtil.isTokenValid(token)  && SecurityContextHolder.getContext().getAuthentication() == null) {
      String email = jwtUtil.extractEmail(token);
      String role = jwtUtil.extractRole(token);

      var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
      var authToken = new UsernamePasswordAuthenticationToken(email, null, authorities);   // no password as authenticating jwt only
      SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    System.out.println("Auth header: " + authHeader);
    System.out.println("Is authenticated after filter: " + SecurityContextHolder.getContext().getAuthentication());
    filterChain.doFilter(request, response);
  }
}