import com.camon.dailylog.Starter;
import com.camon.dailylog.accounts.domain.Account;
import com.camon.dailylog.accounts.domain.AccountDto;
import com.camon.dailylog.accounts.service.AccountService;
import com.camon.dailylog.articles.model.Article;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    @Test
    public void createAccountTest() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("jooyong");
        createDto.setPassword("password");
        ResultActions result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));
        result.andDo(print());
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.username", is("jooyong")));
    }

    @Test
    public void createAccount_username_BadRequest() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("     ");
        createDto.setPassword("password");
        ResultActions result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));
        result.andDo(print());
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void createAccount_password_BadRequest() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("jooyong");
        createDto.setPassword("1");
        ResultActions result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));
        result.andDo(print());
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void createAccount_duplicated_username_BadRequest() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("jooyong");
        createDto.setPassword("password");
        ResultActions result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));
        ResultActions result2 = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        result2.andDo(print());
        result2.andExpect(status().isBadRequest());
        result2.andExpect(jsonPath("$.code", is("duplicated.username.exception")));
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
        updateDto.setPassword("changed_password");
        ResultActions result = mockMvc.perform(put("/accounts/" + account.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(updateDto)));

        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.fullName", is("jooyong sung")));
    }

    @Test
    public void deleteAccount_notFound() throws Exception {
        ResultActions result = mockMvc.perform(delete("/accounts/999"));
        result.andDo(print());
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void deleteAccount() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("jooyong");
        createDto.setPassword("password");
        Account account = service.createAccount(createDto);

        ResultActions result = mockMvc.perform(delete("/accounts/" + account.getId())
            .with(httpBasic(createDto.getUsername(), createDto.getPassword())));
        result.andDo(print());
        result.andExpect(status().isNoContent());
    }

    @Test
    public void aaaaa() {
        Article article = new Article();
        Account account = new Account();
        article.setContent("내용");
        account.setId(1L);
        account.setUsername("jy");
        article.setAccount(account);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(article);
        System.out.println(jsonStr);
    }

}
