package org.fola.data.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Option {
    private String id;
    private String text;
    private boolean isCorrect;
}
