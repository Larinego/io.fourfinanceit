package io.fourfinanceit.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fourfinanceit.model.Loan;
import io.fourfinanceit.model.LoanExtension;
import io.fourfinanceit.model.User;
import io.fourfinanceit.service.LoanService;
import io.fourfinanceit.service.UserService;
import io.fourfinanceit.service.ApplicationTestConfig;
import io.fourfinanceit.util.ApplicationConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationTestConfig.class)
@WebAppConfiguration
public class UserRestControllerTests {

    @Mock
    private UserService userService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserRestController userRestController;

    private MockMvc mockMvc;

    @Before
    public void init()  {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userRestController)
            .setControllerAdvice(new ExceptionControllerAdvice()).build();
    }

    private static String newLoan1AsJSON;
    private static String newLoan2AsJSON;
    private static String newLoan3AsJSON;
    private static String newLoan4AsJSON;
    private static String newLoan5AsJSON;
    private static String newLoan6AsJSON;

    private static Integer loanId;

    @Before
    public void initLoans() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setRole("ROLE_USER");

        Loan loan1 = new Loan();
        loan1.setAmount(new BigDecimal(12.5));
        loan1.setTerm(3);
        loan1.setIpaddress("127.0.0.2");
        newLoan1AsJSON = mapper.writeValueAsString(loan1);

        Loan loan2 = new Loan();
        loan2.setAmount(new BigDecimal(20.0));
        loan2.setTerm(5);
        loan2.setId(125);
        loan2.setIpaddress("127.0.0.2");
        newLoan2AsJSON = mapper.writeValueAsString(loan2);

        Loan loan3 = new Loan();
        loan3.setAmount(new BigDecimal(21.12));
        loan3.setTerm(18);
        loan3.setId(125);
        loan3.setIpaddress("127.0.0.2");
        newLoan3AsJSON = mapper.writeValueAsString(loan3);

        Loan loan4 = new Loan();
        loan4.setAmount(new BigDecimal(310000.1255555));
        loan4.setTerm(1);
        loan4.setIpaddress("127.0.0.3");
        newLoan4AsJSON = mapper.writeValueAsString(loan4);

        Loan loan5= new Loan();
        loan5.setAmount(new BigDecimal(1));
        loan5.setTerm(1);
        loan5.setIpaddress("127.0.0.2");
        newLoan5AsJSON = mapper.writeValueAsString(loan5);

        Loan loan6= new Loan();
        loan6.setAmount(new BigDecimal(12.5));
        loan6.setTerm(3);
        loan6.setIpaddress("192.0.0.5");
        newLoan6AsJSON = mapper.writeValueAsString(loan6);

        LoanExtension loanExtension1 = new LoanExtension();

        LoanExtension loanExtension2 = new LoanExtension();

        this.loanService.saveLoanAfterValidation(loan1);
        this.loanService.saveLoanAfterValidation(loan2);
        this.loanService.saveLoanAfterValidation(loan3);
        this.loanService.saveLoanAfterValidation(loan4);
        this.loanService.saveLoanAfterValidation(loan5);
        loanId = loan4.getId();
        this.loanService.saveLoanExtensionAfterValidation(loanId);
        this.loanService.saveLoanExtensionAfterValidation(loanId);


    }

    @Test
    @WithMockUser(username = "testuser", roles={"USER"})
    public void testCreateLoanWithUserRole() throws Exception {
        this.mockMvc.perform(post("/userapi/loans")
            .content(newLoan1AsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "testadmin", roles={"ADMIN"})
    public void testCreateLoanWithAdminRole() throws Exception {
        this.mockMvc.perform(post("/userapi/loans")
            .content(newLoan1AsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser", roles={"USER"})
    public void testCreateLoanResponse () throws Exception {
        this.mockMvc.perform(post("/userapi/loans")
            .content(newLoan6AsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.amount").value(12.5))
            .andExpect(jsonPath("$.term").value(3))
            .andExpect(jsonPath("$.status").value("ACCEPTED"))
            .andExpect(jsonPath("$.interestRate").value(ApplicationConstants.INTEREST_RATE))
            .andExpect(jsonPath("$.createdOn").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "testuser", roles={"USER"})
    public void testGetLoansResponse () throws Exception {
        this.mockMvc.perform(get("/userapi/loans")
            .content(newLoan3AsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.[0].amount").value(12.5))
            .andExpect(jsonPath("$.[0].term").value(3))
            .andExpect(jsonPath("$.[0].status").value(ApplicationConstants.STATUS_ACCEPTED))
            .andExpect(jsonPath("$.[0].interestRate").value(ApplicationConstants.INTEREST_RATE))
            .andExpect(jsonPath("$.[0].createdOn").isNotEmpty())
            .andExpect(jsonPath("$.[1].amount").value(20.0))
            .andExpect(jsonPath("$.[1].term").value(5))
            .andExpect(jsonPath("$.[1].status").value(ApplicationConstants.STATUS_ACCEPTED))
            .andExpect(jsonPath("$.[1].interestRate").value(ApplicationConstants.INTEREST_RATE))
            .andExpect(jsonPath("$.[1].createdOn").isNotEmpty())
            .andExpect(jsonPath("$.[2].amount").value(21.12))
            .andExpect(jsonPath("$.[2].term").value(18))
            .andExpect(jsonPath("$.[2].status").value(ApplicationConstants.STATUS_ACCEPTED))
            .andExpect(jsonPath("$.[2].interestRate").value(ApplicationConstants.INTEREST_RATE))
            .andExpect(jsonPath("$.[2].createdOn").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "testuser", roles={"USER"})
    public void testValidators() throws Exception {
        this.mockMvc.perform(get("/userapi/loans")
            .content(newLoan1AsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.[0].status").value(ApplicationConstants.STATUS_ACCEPTED))
            .andExpect(jsonPath("$.[1].status").value(ApplicationConstants.STATUS_ACCEPTED))
            .andExpect(jsonPath("$.[2].status").value(ApplicationConstants.STATUS_ACCEPTED))
            .andExpect(jsonPath("$.[3].status").value(ApplicationConstants.STATUS_REJECTED))
            .andExpect(jsonPath("$.[4].status").value(ApplicationConstants.STATUS_REJECTED));
    }

    @Test
    @WithMockUser(username = "testuser", roles={"USER"})
    public void testSaveLoanExtensions () throws Exception {
        this.mockMvc.perform(post("/userapi/loans/1")
            .content(newLoan1AsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.loan.id").value(1))
            .andExpect(jsonPath("$.createdOn").isNotEmpty())
            .andExpect(jsonPath("$.interestRate").value(ApplicationConstants.EXTEND_INTEREST_RATE));

    }

/*    @Test
    @WithMockUser(username = "testuser", roles={"USER"})
    public void testGetLoanExtensions() throws Exception {
        this.mockMvc.perform(get("/userapi/loans/"+loan6Id)
            .content(newLoan1AsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.[0].loan.id").value(loan6Id))
            .andExpect(jsonPath("$.[0].createdOn").isNotEmpty())
            .andExpect(jsonPath("$.[0].interestRate").value(ApplicationConstants.EXTEND_INTEREST_RATE))
            .andExpect(jsonPath("$.[0].loan.id").value(loan6Id))
            .andExpect(jsonPath("$.[0].createdOn").isNotEmpty())
            .andExpect(jsonPath("$.[0].interestRate").value(ApplicationConstants.EXTEND_INTEREST_RATE));
    }*/


}
