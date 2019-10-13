package io.fourfinanceit.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import io.fourfinanceit.model.Loan;
import io.fourfinanceit.service.LoanService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("adminapi/loans")
public class AdminRestController {

    @Autowired
    private LoanService loanService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Collection<Loan>> getAllLoans() throws Exception  {
        Collection<Loan> loans = this.loanService.findAllLoans();
        if (loans.isEmpty()) {
            return new ResponseEntity<Collection<Loan>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Collection<Loan>>(loans, HttpStatus.OK);
    }
}
