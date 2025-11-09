package org.example.quanlycongviec.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class TaskRequest {
    private String title;
    private String description;
    private LocalDate dueDate;
    private String status;
}