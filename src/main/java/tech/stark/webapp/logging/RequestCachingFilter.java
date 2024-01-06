package tech.stark.webapp.logging;

import com.timgroup.statsd.StatsDClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter(filterName = "RequestCachingFilter", urlPatterns = "/*")
public class RequestCachingFilter extends OncePerRequestFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger(RequestCachingFilter.class);


    private final StatsDClient statsDClient;

    @Autowired
    public RequestCachingFilter(StatsDClient statsDClient) {
        this.statsDClient = statsDClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest(request);
        filterChain.doFilter(cachedHttpServletRequest, response);
        LOGGER.info("URI:%s %s | status: %s".formatted(cachedHttpServletRequest.getMethod(),
                cachedHttpServletRequest.getRequestURI(),response.getStatus()));

        countMetrics(cachedHttpServletRequest.getRequestURI(),cachedHttpServletRequest.getMethod());
    }

    private void countMetrics(String requestURI, String method) {
        if ("/healthz".equals(requestURI) && "GET".equals(method)) {
            statsDClient.increment("api_calls_GET_healthz");
        } else if (requestURI.startsWith("/v1/assignments")) {
            statsDClient.increment("api_calls_" + method + "_v1_assignments");
        } else {
            statsDClient.increment("api.calls.INVALID");
        }
    }
}
