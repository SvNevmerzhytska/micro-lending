package ua.nevmerzhytska.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import ua.nevmerzhytska.entities.AccessRequest;
import ua.nevmerzhytska.entities.Loan;
import ua.nevmerzhytska.exception.HighRiskException;
import ua.nevmerzhytska.exception.LoanNotFoundException;
import ua.nevmerzhytska.model.LoanRequest;
import ua.nevmerzhytska.repositories.AccessRequestRepository;
import ua.nevmerzhytska.repositories.LoanRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class LoanServiceTest {

    private static String TEST_USER_ID = "testUser";
    private static String TEST_IP = "196.102.1.11";
    private static int TEST_AMOUNT = 1000;
    private static LocalDate TEST_DUE_DATE = LocalDate.now().plusDays(1);

    private static String TEST_LOAN_ID = "test_loan";
    private static String TEST_EXTENDED_LOAN_ID = "extended_loan";

    private static double TEST_INTEREST = 0.05;
    private static double TEST_INTEREST_INCREASE = 1.5;
    private static int TEST_MAX_REQUESTS_PER_DAY = 3;
    private static int TEST_MAX_AMOUT = 1000000;
    private static LocalTime TEST_RISK_PERIOD_START = LocalTime.MIDNIGHT;
    private static LocalTime TEST_RISK_PERIOD_END = LocalTime.parse("06:00");

    private static Properties properties = new Properties();
    static {
        properties.setProperty("initial.interest.rate", "0.05");
        properties.setProperty("interest.increase.factor", "1.5");
        properties.setProperty("max.requests.per.day", "3");
        properties.setProperty("max.amount", "1000000");
        properties.setProperty("risk.period.begin", "00:00");
        properties.setProperty("risk.period.end", "06:00");
    }

    @InjectMocks
    private LoanService loanService = new LoanServiceImpl(properties);

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private AccessRequestRepository accessRequestRepository;

    private List<AccessRequest> accessRequests;

    @Captor
    private ArgumentCaptor<String> argumentCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        accessRequests = new ArrayList<>();

        when(accessRequestRepository.save(any(AccessRequest.class))).then(invocationOnMock -> {
            AccessRequest request = (AccessRequest)invocationOnMock.getArguments()[0];
            accessRequests.add(request);
            return request;
        });
        when(accessRequestRepository.findByIpAndAccessTimeBetween(argumentCaptor.capture(), any(), any()))
                .then(invocationOnMock -> {
                    return accessRequests.stream()
                            .filter(accessRequest -> accessRequest.getIp().equals(argumentCaptor.getValue()))
                            .collect(Collectors.toList());
                });
        when(loanRepository.save(any(Loan.class))).thenReturn(createLoan());
        when(loanRepository.findOne(anyString())).thenReturn(createLoan());
    }

    @Test
    public void testApplyForLoan() {
        String savedLoan = loanService.applyForLoan(TEST_IP, createLoanRequest());

        verify(accessRequestRepository, times(1)).save(any(AccessRequest.class));
        verify(loanRepository, times(1)).save(any(Loan.class));
        assertThat(savedLoan, is(TEST_LOAN_ID));
    }

    /*@Test
    public void testApplyForLoanRisky() {
        LoanRequest loanRequest = createLoanRequest();
        loanRequest.setLoanAmount(TEST_MAX_AMOUT);
        // TODO: fake system time

        try {
            loanService.applyForLoan(TEST_IP, loanRequest);
            fail("HighRiskException should be thrown");
        } catch (HighRiskException ex){

        }

        verify(accessRequestRepository, times(1)).save(any(AccessRequest.class));
        verify(loanRepository, times(0)).save(any(Loan.class));
    }*/

    @Test
    public void testExtendLoan() {
        loanService.extendLoan(TEST_IP, TEST_LOAN_ID);

        verify(accessRequestRepository, times(1)).save(any(AccessRequest.class));
        verify(loanRepository, times(2)).save(any(Loan.class));
    }

    @Test
    public void testExtendLoanNotFound() {
        when(loanRepository.findOne(anyString())).thenReturn(null);

        try {
            loanService.extendLoan(TEST_IP, TEST_LOAN_ID);
            fail("LoanNotFoundException should be thrown");
        } catch (LoanNotFoundException ex) {}

        verify(accessRequestRepository, times(1)).save(any(AccessRequest.class));
        verify(loanRepository, times(0)).save(any(Loan.class));
    }

    @Test
    public void testApplyAndExtendTooManyTimes() {
        loanService.applyForLoan(TEST_IP, createLoanRequest());
        loanService.applyForLoan(TEST_IP + "1", createLoanRequest());
        loanService.extendLoan(TEST_IP, TEST_LOAN_ID);
        loanService.extendLoan(TEST_IP, TEST_LOAN_ID);
        try {
            loanService.extendLoan(TEST_IP, TEST_LOAN_ID);
            fail("HighRiskException should be thrown");
        } catch (HighRiskException ex) {}
    }

    //TODO: test getLoanHistory

    private LoanRequest createLoanRequest() {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setDueDate(TEST_DUE_DATE);
        loanRequest.setLoanAmount(TEST_AMOUNT);
        loanRequest.setUserId(TEST_USER_ID);
        return loanRequest;
    }

    private Loan createLoan() {
        Loan loan = new Loan(TEST_AMOUNT, TEST_INTEREST, TEST_DUE_DATE, TEST_USER_ID);
        loan.setId(TEST_LOAN_ID);
        return loan;
    }
}
