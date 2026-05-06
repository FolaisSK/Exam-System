package org.fola.dtos.requests;

import lombok.Data;

@Data
public class UpdateExamRequest {
    private String title;
    private String description;
    private Boolean isTimed;
    private Integer durationMinutes;
}
