package com.spark.test.config;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@Slf4j
public class IpFilter extends OncePerRequestFilter {

    @Value("${allowed.ips}")
    private String[] allowedIps;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {

        if (allowedIps == null || allowedIps.length == 0) {
            log.error("No allowed IPs configured. Denying all requests.");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: no allowed IPs configured.");
            return;
        }

        String clientIp = request.getRemoteAddr();
        log.info("Incoming request from IP: {}", clientIp);

        if (Arrays.stream(allowedIps).noneMatch(clientIp::equals)) {
            log.warn("Access denied for IP: {}", clientIp);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied from IP: " + clientIp);
            return;
        }

        log.info("Access granted for IP: {}", clientIp);
        filterChain.doFilter(request, response);
    }
}
