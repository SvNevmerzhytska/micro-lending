package ua.nevmerzhytska.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanResponse {

    private int loanAmount;
    private LocalDate dueDate;
}
