package io.fourfinanceit.repository;

import io.fourfinanceit.model.LoanExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanExtensionRepository {

    void save(LoanExtension loanExtension) throws DataAccessException;

    @Query(value = "SELECT l FROM LoanExtension l WHERE l.loan.id = :id")
    public List<LoanExtension> findExtensionsById(Integer id) throws DataAccessException;
}
