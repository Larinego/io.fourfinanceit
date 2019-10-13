package io.fourfinanceit.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fourfinanceit.model.User;
import io.fourfinanceit.service.UserService;
import io.fourfinanceit.service.ApplicationTestConfig;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationTestConfig.class)
@WebAppConfiguration
public class AuthRestControllerTests {

    @Mock
    private UserService userService;

    @Autowired
    private AuthRestController authRestController;

    private MockMvc mockMvc;

    @Before
    public void initVets() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(authRestController)
            .setControllerAdvice(new ExceptionControllerAdvice()).build();
    }

    @Test
    public void testAddUser() throws Exception {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        ObjectMapper mapper = new ObjectMapper();
        String newUserAsJSON = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/authapi/register/")
            .content(newUserAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "testuser", password = "aaa", roles={"USER"})
    public void testLoginUser() throws Exception {
        User user = new User();
        this.mockMvc.perform(post("/authapi/login/").param("username","testuser").param("password","aaa")
            .accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk());
    }
}
