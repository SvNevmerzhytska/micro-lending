package ua.nevmerzhytska.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanRequest {

    private String userId;

    private int loanAmount;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dueDate;
}
