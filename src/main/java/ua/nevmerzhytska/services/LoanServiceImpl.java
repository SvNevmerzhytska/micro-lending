package ua.nevmerzhytska.services;

import org.springframework.stereotype.Service;
import ua.nevmerzhytska.model.LoanRequest;

@Service
public class LoanServiceImpl implements LoanService {
    @Override
    public void applyForLoan(String requestorIp, LoanRequest loanRequest) {

    }

    @Override
    public void extendLoan(String requestorIp, String loanId) {

    }

    private void evalueateRisk(String requestorIp) {

    }
}
