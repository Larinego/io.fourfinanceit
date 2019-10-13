package io.fourfinanceit.repository.springdatajpa;

import io.fourfinanceit.model.LoanExtension;
import io.fourfinanceit.repository.LoanExtensionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.Repository;

@Profile("spring-data-jpa")
public interface SpringDataLoanExtensionRepository extends LoanExtensionRepository, Repository<LoanExtension, Integer>  {

}
