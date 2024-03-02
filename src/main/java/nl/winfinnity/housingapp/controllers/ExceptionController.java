package nl.winfinnity.housingapp.controllers;

import nl.winfinnity.housingapp.exceptions.InvalidInputException;
import nl.winfinnity.housingapp.models.ErrorResponse;
import nl.winfinnity.housingapp.models.ImmutableErrorResponse;
import nl.winfinnity.housingapp.services.TracerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionController.class);
    private final TracerService tracerService;

    public ExceptionController(TracerService tracerService) {
        this.tracerService = tracerService;
    }

    @ExceptionHandler({InvalidInputException.class,
            HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleInvalidInputException(Exception e) {
        LOG.error("Invalid input: {}", e.getMessage());
        return new ResponseEntity<>(createErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        LOG.error("An exception occurred: {}", e.getMessage(),e);
        return new ResponseEntity<>(createErrorResponse("Something went wrong"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse createErrorResponse(String errorMessage) {
        return ImmutableErrorResponse.builder()
                .errorMessage(errorMessage)
                .traceId(tracerService.getTraceId())
                .build();
    }

}