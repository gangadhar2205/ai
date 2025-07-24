package com.mortgages.ai.authentication.config;

import com.mortgages.ai.authentication.request.UserReq;
import com.mortgages.ai.authentication.service.AuthUserDetailsService;
import com.mortgages.ai.authentication.service.JwtService;
import com.mortgages.ai.authentication.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        System.out.println("Request URI "+path);

        // Skip filter for public endpoints
        if (path.equals("/api/v1/user/login") || path.equals("/api/v1/user/register")
         ||  path.equals("/api/v1/mortgage/enquiry")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            userName = jwtService.extractUserName(token);
        }

        // If token is present, validate it
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                System.out.println("In tHE FILTER CLASS");
                UserDetails userDetails = applicationContext.getBean(AuthUserDetailsService.class).loadUserByUsername(userName);

                if (jwtService.validateToken(token, userDetails)) {
                    System.out.println("Jwt services ");
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    System.out.println("user authorised");
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    System.out.println( SecurityContextHolder.getContext().getAuthentication());
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                System.out.println(e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
