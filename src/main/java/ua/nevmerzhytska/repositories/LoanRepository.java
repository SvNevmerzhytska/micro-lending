package ua.nevmerzhytska.repositories;

import org.springframework.data.repository.CrudRepository;
import ua.nevmerzhytska.entities.Loan;

import java.util.List;

public interface LoanRepository extends CrudRepository<Loan, String> {

    List<Loan> findByUser(String user);
}
