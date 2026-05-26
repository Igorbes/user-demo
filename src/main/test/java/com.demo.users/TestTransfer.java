package com.demo.users;

import com.demo.users.api.request.Request;
import com.demo.users.dao.UserDao;
import com.demo.users.om.Account;
import com.demo.users.om.User;
import com.demo.users.om.other.FilterUserRequest;
import com.demo.users.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
public class TestTransfer {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Test
    public void testTransfer() {
        BigDecimal transfer = new BigDecimal(500, MathContext.DECIMAL64);
        List<User> users = userDao.filter(FilterUserRequest.builder().build()).getUsers();
        Account from = users.get(0).getAccount();
        Account to = users.get(1).getAccount();
        userService.transfer(from.getUserId(), to.getUserId(), transfer);

        List<User> assertUsers = userDao.filter(FilterUserRequest.builder().build()).getUsers();
        Account fromAssert = assertUsers.get(0).getAccount();
        Account toAssert = assertUsers.get(1).getAccount();

        Assert.assertEquals(toAssert.getBalance(), to.getBalance().add(transfer));
        Assert.assertEquals(fromAssert.getBalance(), from.getBalance().add(transfer.multiply(new BigDecimal(-1), MathContext.DECIMAL64)));
    }

    @Test
    public void testMvc() throws Exception {
        Request.Filter filter = new Request.Filter();
        filter.setId(1L);
        mvc.perform(
                post("/api/user/v1/filter")
                .content(new ObjectMapper().writeValueAsString(filter))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users[0].name", is("Иван")));
    }
}
