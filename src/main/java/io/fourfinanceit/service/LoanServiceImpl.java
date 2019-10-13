package io.fourfinanceit.service;

import io.fourfinanceit.model.Loan;
import io.fourfinanceit.model.LoanExtension;
import io.fourfinanceit.repository.LoanExtensionRepository;
import io.fourfinanceit.repository.LoanRepository;
import io.fourfinanceit.util.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanExtensionRepository loanExtensionRepository;

    @Autowired
    private UserService userService;


    @Override
    @Transactional
    public Loan saveLoanAfterValidation(Loan loan) throws Exception {
        if(ValidateAmount(loan.getAmount()) && ValidateIpRisks(loan.getIpaddress()) && ValidateTimeRisks(loan.getAmount())){
            loan.setStatus(ApplicationConstants.STATUS_ACCEPTED);
        }
        else{
            loan.setStatus(ApplicationConstants.STATUS_REJECTED);
        }
        loan.setUser(userService.getCurrentUser());
        loan.setCreatedOn(LocalDateTime.now());
        loan.setInterestRate(ApplicationConstants.INTEREST_RATE);
        loanRepository.save(loan);
        return loan;
    }

    @Override
    @Transactional
    public LoanExtension saveLoanExtensionAfterValidation(Integer id) throws Exception {
        Optional<Loan> loan = loanRepository.findById(id);
        if (!loan.isPresent()){
            throw new Exception("Loan with " + id + " doesn't exist");
        }
        LoanExtension loanExtension = new LoanExtension();
        loanExtension.setLoan(loan.get());
        loanExtension.setCreatedOn(LocalDateTime.now());
        loanExtension.setInterestRate(ApplicationConstants.EXTEND_INTEREST_RATE);
        loanExtensionRepository.save(loanExtension);
        return loanExtension;
    }

    @Override
    public Collection<Loan> findAllLoans()  throws Exception{
        return loanRepository.findAll();
    }

    @Override
    public List<LoanExtension> findExtensionsById(Integer id) throws DataAccessException{
        return loanExtensionRepository.findExtensionsById(id);
    }

    public Collection<Loan> findAllLoansByUserName(String username) throws DataAccessException{
        return loanRepository.findByUserName(username);
    }

    public Integer countOfIpAddresses(String ipaddress, LocalDateTime start, LocalDateTime end) throws DataAccessException{
        return loanRepository.countOfIpAddresses(ipaddress, start, end);
    }

    private boolean ValidateIpRisks(String ipaddress){
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusHours(ApplicationConstants.HOURS_PERIOD_IP_LIMIT);
        if (countOfIpAddresses(ipaddress,start,end) < ApplicationConstants.COUNT_OF_IP_LIMIT){
            return true;
        }
        return false;
    }

    private boolean ValidateTimeRisks(BigDecimal amount){
        int currentHour = LocalDateTime.now().getHour();
        if (amount.longValue() >= ApplicationConstants.MAX_POSSIBLE_AMOUNT.longValue() && currentHour >= ApplicationConstants.START_TIME_RISKS && currentHour <= ApplicationConstants.END_TIME_RISKS){
            return false;
        }
        return true;
    }

    private boolean ValidateAmount(BigDecimal amount){
        if (amount.longValue() > ApplicationConstants.MAX_POSSIBLE_AMOUNT.longValue()){
            return false;
        }
        return true;
    }
}
