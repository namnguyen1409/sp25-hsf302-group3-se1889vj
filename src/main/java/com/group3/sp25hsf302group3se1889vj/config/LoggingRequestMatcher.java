package com.group3.sp25hsf302group3se1889vj.config;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Đây là một custom RequestMatcher dùng để log ra thông tin của request
 * Mục đích để theo dõi các request đến server
 */
public class LoggingRequestMatcher implements RequestMatcher {
    private static final Logger logger = LoggerFactory.getLogger(LoggingRequestMatcher.class);

    @Override
    public boolean matches(HttpServletRequest request) {
        var time = LocalDateTime.now();
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
        var timeString = time.format(formatter);

        logger.info("{} - - [{}] \"{} {} {}\" -",
                request.getRemoteAddr(),
                timeString,
                request.getMethod(),
                request.getRequestURI(),
                request.getProtocol());
        return false;
    }
}
