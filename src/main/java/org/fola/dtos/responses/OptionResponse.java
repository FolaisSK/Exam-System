package org.fola.dtos.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OptionResponse {
    private String id;
    private String text;
    private Boolean isCorrect;
}
