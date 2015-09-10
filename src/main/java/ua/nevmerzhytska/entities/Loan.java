package ua.nevmerzhytska.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "LOANS", indexes = {@Index(columnList = "USER")})
@Data
@NoArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "ID")
    private String id;

    @Min(0)
    @Column(name = "REQUESTED_AMOUNT")
    private int requestedAmount;

    @Min(0)
    @Max(100)
    @Column(name = "INTEREST_RATE")
    private double interestRate;

    @NotNull
    @Column(name = "DUE_DATE")
    private LocalDate dueDate;

    @Column(name = "EXTENSION_LOAN")
    private String extensionLoan;

    @NotEmpty
    @Column(name = "USER")
    private String user;

    public Loan(int requestedAmount, double interestRate, LocalDate dueDate, String user) {
        this.requestedAmount = requestedAmount;
        this.interestRate = interestRate;
        this.dueDate = dueDate;
        this.user = user;
    }
}
