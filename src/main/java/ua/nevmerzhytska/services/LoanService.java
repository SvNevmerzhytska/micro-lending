package ua.nevmerzhytska.services;

import ua.nevmerzhytska.entities.Loan;
import ua.nevmerzhytska.model.LoanRequest;

import java.util.List;

public interface LoanService {

    String applyForLoan(String requestorIp, LoanRequest loanRequest);

    String extendLoan(String requestorIp, String loanId);

    List<Loan> getLoanHistory(String user);
}
