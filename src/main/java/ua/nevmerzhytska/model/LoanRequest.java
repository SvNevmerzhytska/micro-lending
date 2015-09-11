package ua.nevmerzhytska.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import ua.nevmerzhytska.constrants.Future;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class LoanRequest {

    @NotEmpty
    private String userId;

    @Min(0)
    private int loanAmount;

    @NotNull
    @Future
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dueDate;
}
