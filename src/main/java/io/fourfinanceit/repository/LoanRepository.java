package io.fourfinanceit.repository;

import io.fourfinanceit.model.Loan;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface LoanRepository {

    void save(Loan loan) throws DataAccessException;

    public Collection<Loan> findAll() throws DataAccessException;

    public Optional<Loan> findById(Integer id) throws DataAccessException;

    @Query(value = "SELECT l FROM Loan l WHERE l.user.username = :username")
    public Collection<Loan> findByUserName(String username) throws DataAccessException;


    @Query(value = "SELECT count(ipaddress) FROM Loan l WHERE l.createdOn between :start and :end and l.ipaddress = :ipaddress and l.status='ACCEPTED'")
    public Integer countOfIpAddresses(String ipaddress, LocalDateTime start, LocalDateTime end) throws DataAccessException;

}
