package io.fourfinanceit.repository.springdatajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.Repository;
import io.fourfinanceit.model.Loan;
import io.fourfinanceit.repository.LoanRepository;

@Profile("spring-data-jpa")
public interface SpringDataLoanRepository extends LoanRepository, Repository<Loan, Integer>  {

}
