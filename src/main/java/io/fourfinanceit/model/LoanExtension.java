package io.fourfinanceit.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_extensions")
public class LoanExtension extends BaseEntity{

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "interest_rate")
    private Double interestRate;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
