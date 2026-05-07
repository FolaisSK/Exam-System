package org.fola.data.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Option {
    private String id;
    private String text;
    @JsonProperty("isCorrect")
    private boolean correct;
}
