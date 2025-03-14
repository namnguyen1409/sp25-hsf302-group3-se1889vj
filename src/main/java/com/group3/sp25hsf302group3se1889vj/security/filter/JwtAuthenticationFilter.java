package com.group3.sp25hsf302group3se1889vj.security.filter;


import com.group3.sp25hsf302group3se1889vj.security.service.CustomUserDetailsService;
import com.group3.sp25hsf302group3se1889vj.security.token.JwtTokenProvider;
import com.group3.sp25hsf302group3se1889vj.security.token.RefreshTokenProvider;
import com.group3.sp25hsf302group3se1889vj.security.userdetails.CustomUserDetails;
import com.group3.sp25hsf302group3se1889vj.service.MessageService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.auth.login.AccountLockedException;
import java.io.IOException;

@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenProvider refreshTokenProvider;

    private final CustomUserDetailsService customUserDetailsService;

    private final MessageService messageService;


    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        log.info("doFilterInternal");
        try {
            var contextPath = request.getContextPath();
            var requestURI = request.getRequestURI();

            if (requestURI.startsWith(contextPath + "/error")) {
                filterChain.doFilter(request, response);
                return;
            }

            var jwt = getJwtFromRequest(request);
            var refreshToken = getRefreshTokenFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                log.info("validateToken");
                Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
                var userDetails = customUserDetailsService.loadUserById(userId);
                if (userDetails != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (!userDetails.isAccountNonLocked()) {
                        log.info("user is locked");
                        throw new AccountLockedException(messageService.getMessage("user.locked", ((CustomUserDetails) userDetails).getUser().getLockReason()));
                    }

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else if (StringUtils.hasText(refreshToken) && refreshTokenProvider.validateRefreshToken(refreshToken)) {
                var key = refreshTokenProvider.getKeyFromRefreshToken(refreshToken);
                var customUserDetails = customUserDetailsService.loadUserByRefreshToken(key);
                if (customUserDetails != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (!customUserDetails.isAccountNonLocked()) {
                        throw new AccountLockedException(messageService.getMessage("user.locked", customUserDetails.getUser().getLockReason()));
                    }

                    var newJwtToken = jwtTokenProvider.generateToken(customUserDetails);
                    var jwtCookie = new Cookie("jwtToken", newJwtToken);
                    jwtCookie.setPath("/");
                    jwtCookie.setHttpOnly(true);
                    jwtCookie.setMaxAge(Integer.parseInt(Long.parseLong(jwtTokenProvider.getExpiration()) / 1000L + ""));
                    response.addCookie(jwtCookie);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            customUserDetails, null, customUserDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } else {
                SecurityContextHolder.clearContext();
            }
        } catch (Exception e) {
            log.error("Failed on set user authentication", e);
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        var bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        var cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String getRefreshTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
