package ua.nevmerzhytska.repositories;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.nevmerzhytska.config.MicroLendingAppSpringConfiguration;
import ua.nevmerzhytska.entities.Loan;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MicroLendingAppSpringConfiguration.class})
public class LoanRepositoryTest {

    @Autowired
    private LoanRepository loanRepository;

    private List<String> testLoanIds = new ArrayList<>();

    @After
    public void cleanUp() {
        testLoanIds.forEach(id -> loanRepository.delete(id));
        testLoanIds.clear();
    }

    @Test
    public void testSaveLoan() {
        Loan loan = new Loan();
        loan.setRequestedAmount(1000);
        loan.setDueDate(LocalDate.of(2015, Month.NOVEMBER, 20));
        loan.setUser("testUser");

        Loan savedLoan = loanRepository.save(loan);

        assertThat(savedLoan, is(notNullValue()));
        assertThat(savedLoan.getId(), is(notNullValue()));
        testLoanIds.add(savedLoan.getId());
        System.out.println(savedLoan);
    }
}
