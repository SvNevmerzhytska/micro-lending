package ua.nevmerzhytska.services;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nevmerzhytska.entities.AccessRequest;
import ua.nevmerzhytska.entities.Loan;
import ua.nevmerzhytska.exception.HighRiskException;
import ua.nevmerzhytska.exception.LoanNotFoundException;
import ua.nevmerzhytska.model.LoanRequest;
import ua.nevmerzhytska.repositories.AccessRequestRepository;
import ua.nevmerzhytska.repositories.LoanRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private AccessRequestRepository accessRequestRepository;

    public int maxRequestsPerDay;
    public int maxAmount;
    public LocalTime riskPeriodBegin;
    public LocalTime riskPeriodEnd;
    public double initialInterest;
    public double increaseFactor;

    @Autowired
    public LoanServiceImpl(Properties properties) {
        maxRequestsPerDay = Integer.valueOf(properties.getProperty("max.requests.per.day"));
        maxAmount = Integer.valueOf(properties.getProperty("max.amount"));
        riskPeriodBegin = LocalTime.parse(properties.getProperty("risk.period.begin"));
        riskPeriodEnd = LocalTime.parse(properties.getProperty("risk.period.end"));
        initialInterest = Double.valueOf(properties.getProperty("initial.interest.rate"));
        increaseFactor = Double.valueOf(properties.getProperty("interest.increase.factor"));
    }

    @Override
    public String applyForLoan(String requestorIp, LoanRequest loanRequest) {
        accessRequestRepository.save(new AccessRequest(requestorIp, LocalDateTime.now()));

        evalueateRiskByTimeAndMount(loanRequest);
        evaluateRiskByRequestNumber(requestorIp);

        Loan loan = loanRepository.save(
                new Loan(loanRequest.getLoanAmount(), initialInterest, loanRequest.getDueDate(), loanRequest.getUserId()));
        return loan.getId();
    }

    @Override
    /*@Transactional*/
    public String extendLoan(String requestorIp, String loanId) {
        accessRequestRepository.save(new AccessRequest(requestorIp, LocalDateTime.now()));

        evaluateRiskByRequestNumber(requestorIp);

        Loan loan = loanRepository.findOne(loanId);
        if (loan == null) {
            throw new LoanNotFoundException(loanId);
        }
        Loan extendedLoan = loanRepository.save(
                new Loan(loan.getRequestedAmount(), loan.getInterestRate()*increaseFactor,
                        loan.getDueDate().plusWeeks(1), loan.getUser()));
        loan.setExtensionLoan(extendedLoan.getId());
        loanRepository.save(loan);
        return extendedLoan.getId();
    }

    @Override
    public List<Loan> getLoanHistory(String user) {
        if (user != null && !user.isEmpty()) {
            return loanRepository.findByUser(user);
        } else {
            return Lists.newArrayList(loanRepository.findAll());
        }
    }

    //throws an error if risk is high
    private void evalueateRiskByTimeAndMount(LoanRequest loanRequest) {
        LocalDateTime now = LocalDateTime.now();
        if(loanRequest.getLoanAmount() >= maxAmount
                && now.toLocalTime().isAfter(riskPeriodBegin) && now.toLocalTime().isBefore(riskPeriodEnd)) {
            throw new HighRiskException(HighRiskException.RISKY_REQUEST);
        }
    }

    private void evaluateRiskByRequestNumber(String requestorIp) {
        LocalDateTime now = LocalDateTime.now();
        if(accessRequestRepository.findByIpAndAccessTimeBetween(requestorIp, now.minusDays(1), now)
                .size() > maxRequestsPerDay) {
            throw new HighRiskException(HighRiskException.TOO_MANY_ATTEMPTS_PER_DAY);
        }
    }
}
