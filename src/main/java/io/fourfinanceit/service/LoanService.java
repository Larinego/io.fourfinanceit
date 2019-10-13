package io.fourfinanceit.service;

import io.fourfinanceit.model.Loan;
import io.fourfinanceit.model.LoanExtension;
import org.springframework.dao.DataAccessException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface LoanService {

    Loan saveLoanAfterValidation(Loan loan) throws Exception;

    Collection<Loan> findAllLoans()  throws Exception;

    public Collection<Loan> findAllLoansByUserName(String username) throws DataAccessException;

    public Integer countOfIpAddresses(String ipaddress, LocalDateTime start, LocalDateTime end) throws DataAccessException;

    public List<LoanExtension> findExtensionsById(Integer id) throws DataAccessException;

    public LoanExtension saveLoanExtensionAfterValidation(Integer id) throws Exception;
}
