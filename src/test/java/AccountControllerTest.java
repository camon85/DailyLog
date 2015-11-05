import com.camon.dailylog.Starter;
import com.camon.dailylog.accounts.domain.Account;
import com.camon.dailylog.accounts.domain.AccountDto;
import com.camon.dailylog.accounts.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by jooyong on 2015-11-02.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Starter.class)
@WebAppConfiguration
@Transactional
public class AccountControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountService service;

    private MockMvc mockMvc;


    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void createAccountTest() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("jooyong");
        createDto.setPassword("password");
        ResultActions result = mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createDto)));
        result.andDo(print());
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.username", is("jooyong")));
    }

    @Test
    public void createAccount_username_BadRequest() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("     ");
        createDto.setPassword("password");
        ResultActions result = mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createDto)));
        result.andDo(print());
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void createAccount_password_BadRequest() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("jooyong");
        createDto.setPassword("1");
        ResultActions result = mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createDto)));
        result.andDo(print());
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void createAccount_duplicated_username_BadRequest() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("jooyong");
        createDto.setPassword("password");
        ResultActions result = mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createDto)));
        result = mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.code", is("duplicated.username.exception")));
    }

    @Test
    public void list() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("jooyong");
        createDto.setPassword("password");
        AccountDto.Create createDto2 = new AccountDto.Create();
        createDto2.setUsername("doffy");
        createDto2.setPassword("passwoood");
        service.createAccount(createDto);
        service.createAccount(createDto2);

        ResultActions result = mockMvc.perform(get("/accounts"));

        result.andDo(print());
        result.andExpect(status().isOk());
    }

    @Test
    public void findById() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("jooyong");
        createDto.setPassword("password");
        Account account = service.createAccount(createDto);

        ResultActions result = mockMvc.perform(get("/accounts/" + account.getId()));
        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.username", is("jooyong")));
    }

    @Test
    public void updateAccount() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("jooyong");
        createDto.setPassword("password");
        Account account = service.createAccount(createDto);

        AccountDto.Update updateDto = new AccountDto.Update();
        updateDto.setFullName("jooyong sung");
        updateDto.setPassword("pass");

        ResultActions result = mockMvc.perform(put("/accounts/" + account.getId()).
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsBytes(updateDto)));

        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.fullName", is("jooyong sung")));

    }
}
