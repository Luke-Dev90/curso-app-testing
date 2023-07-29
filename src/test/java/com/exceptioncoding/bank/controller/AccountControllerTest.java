package com.exceptioncoding.bank.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.exceptioncoding.bank.dto.AccountDTO;
import com.exceptioncoding.bank.dto.TransferDTO;
import com.exceptioncoding.bank.repository.AccountRepository;
import com.exceptioncoding.bank.utils.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountRepository repository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void transferTest200OK() throws Exception {

		when(this.repository.findByCbuOrAlias("00001111", null)).thenReturn(Optional.of(TestUtils.getAccount()));
		when(this.repository.findByCbuOrAlias("00001112", null)).thenReturn(Optional.of(TestUtils.getAccountB()));

		TransferDTO transfer = TestUtils.trasfer();

		mockMvc.perform(post("/home-banking/transfer").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transfer))).andExpect(status().isOk())
				.andExpect(jsonPath("cbuOrigin").value("00001111")).andExpect(jsonPath("reason").value("viaje"))
				.andExpect(jsonPath("amount").value("100"));
	}

	@Test
	public void transferTest500OK() throws Exception {

		when(this.repository.findByCbuOrAlias("00001111", null)).thenReturn(Optional.of(TestUtils.getAccount()));
		when(this.repository.findByCbuOrAlias("00001112", null)).thenReturn(Optional.of(TestUtils.getAccountB()));

		TransferDTO transfer = TestUtils.trasfer();
		transfer.setAmount(new BigDecimal(2000));

		mockMvc.perform(post("/home-banking/transfer").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transfer)))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason("Saldo insuficiente"));
	}
	
	@Test
	public void notFoundAccount() throws JsonProcessingException, Exception {
	
		TransferDTO transfer = TestUtils.trasfer();
		transfer.setAccountDestination("asd");
		transfer.setAliasDestination("asdd");

		mockMvc.perform(post("/home-banking/transfer").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transfer)))
				.andExpect(status().isNotFound())
				.andExpect(status().reason("Account not found"));
		
	}
	
	@Test
	public void notActiveAccount() throws JsonProcessingException, Exception {
		when(this.repository.findByCbuOrAlias("00001111", null)).thenReturn(Optional.of(TestUtils.getAccountNotActive()));
		when(this.repository.findByCbuOrAlias("00001112", null)).thenReturn(Optional.of(TestUtils.getAccountB()));
		TransferDTO transfer = TestUtils.trasfer();

		mockMvc.perform(post("/home-banking/transfer").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transfer)))
				.andExpect(status().isBadRequest())
				.andExpect(status().reason("La cuenta de origen: 00001111 esta desactivada"));
		
	}
	
	@Test
	public void createAccount() throws JsonProcessingException, Exception {
		AccountDTO accountDTO = new AccountDTO("alias.mockito");
		
		when(this.repository.save(null)).thenReturn(TestUtils.getAccount());
		
		mockMvc.perform(post("/home-banking/create").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(accountDTO)))
				.andExpect(status().isCreated())
				.andDo(print());
	}
}
