package ua.nevmerzhytska.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nevmerzhytska.entities.AccessRequest;
import ua.nevmerzhytska.exception.HighRiskException;
import ua.nevmerzhytska.model.LoanRequest;
import ua.nevmerzhytska.repositories.AccessRequestRepository;
import ua.nevmerzhytska.repositories.LoanRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class LoanServiceImpl implements LoanService {

    public static int MAX_REQUEST_NUMBER_PER_DAY = 3;
    public static int MAX_AMOUNT = 1000000;
    public static LocalTime RISK_TIME_START = LocalTime.MIDNIGHT;
    public static LocalTime RISK_TIME_END = LocalTime.of(6, 0);

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccessRequestRepository accessRequestRepository;

    @Override
    public void applyForLoan(String requestorIp, LoanRequest loanRequest) {
        evalueateRisk(requestorIp, loanRequest);
    }

    @Override
    public void extendLoan(String requestorIp, String loanId) {

    }

    private void evalueateRisk(String requestorIp, LoanRequest loanRequest) {
        LocalDateTime now = LocalDateTime.now();
        accessRequestRepository.save(new AccessRequest(requestorIp, now));

        if(loanRequest.getLoanAmount() >= MAX_AMOUNT
                && now.toLocalTime().isAfter(RISK_TIME_START) && now.toLocalTime().isBefore(RISK_TIME_END)) {
            throw new HighRiskException(HighRiskException.RISKY_REQUEST);
        }

        if(accessRequestRepository.findByIpAndAccessTimeBetween(requestorIp, now.minusDays(1), now)
            .size() > MAX_REQUEST_NUMBER_PER_DAY) {
            throw new HighRiskException(HighRiskException.TOO_MANY_ATTEMPTS_PER_DAY);
        }
    }
}
