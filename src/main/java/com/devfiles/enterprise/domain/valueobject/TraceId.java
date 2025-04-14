package com.devfiles.enterprise.domain.valueobject;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.jboss.logging.MDC;
import org.springframework.amqp.core.MessageProperties;

import java.util.UUID;

@Getter
public class TraceId {
    public static final String TRACE_ID_HTTP_HEADER = "X-Trace-ID";
    public static final String TRACE_ID_BROKER_HEADER = "x-trace-id";
    public static final String TRACE_ID_MDC_KEY = "traceId";

    private final String id;

    public TraceId (String id) {
        if (id == null || id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }
    }

    public void registerMdcLog() {
        if (MDC.get(TRACE_ID_MDC_KEY) != null) {
            return;
        }

        MDC.put(TRACE_ID_MDC_KEY, id);
    }

    public void registerHttpHeader(HttpServletResponse response) {
        response.setHeader(TRACE_ID_HTTP_HEADER, id);
    }

    public void registerMessageBrokerHeader(MessageProperties messageProperties) {
        messageProperties.setHeader(TRACE_ID_BROKER_HEADER, id);
    }
}
