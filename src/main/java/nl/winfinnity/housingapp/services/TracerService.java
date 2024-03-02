package nl.winfinnity.housingapp.services;

import brave.Tracer;
import org.springframework.stereotype.Service;

@Service
public class TracerService{

    private final Tracer tracer;
    public TracerService(Tracer tracer) {
        this.tracer = tracer;
    }
    public String getTraceId() {
        return tracer.currentSpan().context().traceIdString();
    }

}
