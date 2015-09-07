package ua.nevmerzhytska.services;

import ua.nevmerzhytska.model.LoanRequest;

public interface LoanService {

    void applyForLoan(String requestorIp, LoanRequest loanRequest);

    void extendLoan(String requestorIp, String loanId);
}
