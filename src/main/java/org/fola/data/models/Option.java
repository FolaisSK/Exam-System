package org.fola.data.models;

import lombok.Data;

@Data
public class Option {
    private String id;
    private String text;
    private boolean isCorrect;
}
