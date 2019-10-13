package io.fourfinanceit.rest;

import io.fourfinanceit.model.Loan;
import io.fourfinanceit.model.LoanExtension;
import io.fourfinanceit.service.LoanService;
import io.fourfinanceit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("userapi/loans")
public class UserRestController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Loan> addUserLoan(@RequestBody @Valid Loan loan, BindingResult bindingResult, HttpServletRequest request) throws Exception {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (loan == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<Loan>(loan, headers, HttpStatus.BAD_REQUEST);
        }
        loan.setIpaddress(request.getRemoteAddr());
        loan = this.loanService.saveLoanAfterValidation(loan);
        return new ResponseEntity<Loan>(loan, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Collection<Loan>> getUserLoans() throws Exception  {

        Collection<Loan> loans = this.loanService.findAllLoansByUserName(userService.getCurrentUser().getUsername());
        if (loans.isEmpty()) {
            return new ResponseEntity<Collection<Loan>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Collection<Loan>>(loans, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<LoanExtension>> getLoanWithExtension(@PathVariable("id") Integer locationId) throws Exception  {

        List<LoanExtension> loanExtensions = this.loanService.findExtensionsById(locationId);
        if (loanExtensions.isEmpty()) {
            return new ResponseEntity<List<LoanExtension>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<LoanExtension>>(loanExtensions, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<LoanExtension> addLoanExtension(@PathVariable("id") Integer locationId) throws Exception  {

        LoanExtension loanExtension = loanService.saveLoanExtensionAfterValidation(locationId);

        return new ResponseEntity<LoanExtension>(loanExtension, HttpStatus.CREATED);
    }
}
