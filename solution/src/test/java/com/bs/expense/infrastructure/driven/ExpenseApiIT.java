package com.bs.expense.infrastructure.driven;

import com.bs.expense.domain.ExpenseService;
import com.bs.expense.infrastructure.ExpenseAssembler;
import com.bs.expense.infrastructure.driver.ExpenseController;
import com.jayway.jsonpath.JsonPath;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import static com.bs.expense.infrastructure.driven.TestHelper.readFromClassPath;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExpenseApiIT {

    @ClassRule
    public static PostgreSQLContainer postgresDb = new PostgreSQLContainer();

    private static final String URI = "/v1/expense";

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private HikariDataSource dataSource;

    @Autowired
    private ExpenseAssembler expenseAssembler;

    private MockMvc mockMvc;
    private ExpenseController expenseController;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        expenseController = new ExpenseController(expenseService, expenseAssembler);
        jdbcTemplate.execute("delete from expense");
        mockMvc = standaloneSetup(expenseController).build();
    }

    @Test
    public void shouldPersistAndFetchAnExpense() throws Exception {
        final ResultActions createdExpense = post("expenseRequest.json");

        final Integer id = getExpenseId(createdExpense);
        assertThat(id, notNullValue());
        final ResultActions foundExpense = fetch(id);
        verifyExpense(foundExpense, "12/02/2018", "322.32", "monthly travel card");
    }

    @Test
    public void shouldReturn404WhenTheExpenseIsNotFound() throws Exception {
        mockMvc.perform(get(String.format("%s/%s", URI, "422"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void shouldReturn404WhenThePayloadIsIncorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                .content(readFromClassPath("expenseRequestWrong.json"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnAllOfTheExpenses() throws Exception {
        post("expenseRequest.json");
        post("expenseRequest.json");
        post("expenseRequest.json");

        ResultActions response = mockMvc.perform(get(URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        response.andExpect(jsonPath("$.length()", is(3)));
        verifyExpenseJson("0", response);
        verifyExpenseJson("1", response);
        verifyExpenseJson("2", response);
    }

    private ResultActions verifyExpenseJson(final String id, ResultActions response) throws Exception {
        return response.andExpect(jsonPath("$[" + id + "].id", notNullValue()))
                .andExpect(jsonPath("$[0].date", is("12/02/2018")))
                .andExpect(jsonPath("$[0].amount", is("322.32")))
                .andExpect(jsonPath("$[0].reason", is("monthly travel card")));
    }


    private ResultActions post(String request) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(URI)
                .content(readFromClassPath(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private ResultActions fetch(final Integer expenseId) throws Exception {
        return mockMvc.perform(get(String.format("%s/%s", URI, expenseId))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private Integer getExpenseId(final ResultActions response) throws UnsupportedEncodingException {
        final String json = response.andReturn().getResponse().getContentAsString();
        Integer read = JsonPath.read(json, "$.id");
        return read;
    }


    private void verifyExpense(final ResultActions action, String date, String value, String reason) throws Exception {
        action
                .andExpect(jsonPath("id", notNullValue()))
                .andExpect(jsonPath("date", is(date)))
                .andExpect(jsonPath("amount", is(value)))
                .andExpect(jsonPath("reason", is(reason)));
    }

    @Configuration
    @ComponentScan(basePackages = "com.bs.expense")
    static class TestDbConfig {
        @Bean
        @Primary
        public HikariDataSource dataSource() throws SQLException {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(postgresDb.getJdbcUrl());
            hikariConfig.setUsername(postgresDb.getUsername());
            hikariConfig.setPassword(postgresDb.getPassword());
            HikariDataSource ds = new HikariDataSource(hikariConfig);
            return new HikariDataSource(ds);
        }
    }
}