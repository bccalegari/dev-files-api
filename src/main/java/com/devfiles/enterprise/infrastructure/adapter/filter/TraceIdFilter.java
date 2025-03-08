package com.devfiles.enterprise.infrastructure.adapter.filter;

import com.devfiles.enterprise.domain.valueobject.TraceId;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TraceIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest && response instanceof HttpServletResponse httpResponse) {
            var traceId = new TraceId(httpRequest.getHeader(TraceId.TRACE_ID_HTTP_HEADER));

            traceId.registerMdcLog();
            traceId.registerHttpHeader(httpResponse);

            try {
                chain.doFilter(request, response);
            } finally {
                MDC.clear();
            }
        }
    }
}
