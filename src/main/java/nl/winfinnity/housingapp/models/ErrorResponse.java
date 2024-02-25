package nl.winfinnity.housingapp.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableErrorResponse.class)
@JsonSerialize(as = ImmutableErrorResponse.class)
public interface ErrorResponse {
    String getErrorMessage();
}
