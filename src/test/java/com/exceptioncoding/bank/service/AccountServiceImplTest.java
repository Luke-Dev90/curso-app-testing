package com.exceptioncoding.bank.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.exceptioncoding.bank.dto.AccountDTO;
import com.exceptioncoding.bank.dto.ResponseTransferDTO;
import com.exceptioncoding.bank.dto.TransferDTO;
import com.exceptioncoding.bank.model.Account;
import com.exceptioncoding.bank.repository.AccountRepository;
import com.exceptioncoding.bank.utils.TestUtils;

@SpringBootTest
public class AccountServiceImplTest {

	@Mock
	private AccountRepository repository;
	
	@InjectMocks
	private AccountServiceImpl accountService;
	
	@Test
	public void findByTest() throws Exception {
		when(this.repository.findByCbuOrAlias( "", "")).thenReturn( Optional.of(TestUtils.getAccount()));
		Account account = this.accountService.getAccountByCbuOrAlias("", "");
		verify(this.repository, times(1)).findByCbuOrAlias("", "");
		assertTrue(account.getIsActive());
		assertThat(account.getAlias()).isEqualTo("arroz.mockito");
	}
	
	@Test
	public void saveAccountTest() {
		when(this.repository.save( null)).thenReturn(TestUtils.getAccount());
		Account account = this.accountService.saveAccount(null);
		assertTrue(account.getIsActive());
		assertThat(account.getAlias()).isEqualTo("arroz.mockito");
	}
	
	@Test
	public void createAccount() {
		when(this.repository.save(null)).thenReturn(null);
		ResponseEntity<String> create = this.accountService.createAccount(new AccountDTO("pepe.mockito"));
		assertThat(create.getStatusCodeValue()).isEqualTo(201);
		assertThat(create.getBody()).isEqualTo("CREATED");
	}
	
	@Test
	public void transferAmount() throws Exception {
		String origin = "00001111";
		String destination = "00001112";
		when(this.repository.findByCbuOrAlias(origin, null)).thenReturn(Optional.of(TestUtils.getAccount()));
		when(this.repository.findByCbuOrAlias(destination, null)).thenReturn(Optional.of(TestUtils.getAccountB()));
		ResponseEntity<ResponseTransferDTO> resp = this.accountService.transferAmount(TestUtils.trasfer());
		verify(this.repository, times(1)).findByCbuOrAlias(origin, null);
		verify(this.repository, times(1)).findByCbuOrAlias(destination, null);
		
		assertThat(resp.getStatusCodeValue()).isEqualTo(200);
		assertThat(resp.getBody().getAmount()).isEqualTo(new BigDecimal(100));
	}
	
	@Test
	public void checkAccountTest() throws Exception {
		Boolean active = this.accountService.checkActiveAccount(TestUtils.getAccount(), "origin");
		assertTrue(active);
	}
	
	@Test
	public void checkAccountInactived() throws Exception{
		Account account = TestUtils.getAccount();
		account.setIsActive(false);
		assertThrows(ResponseStatusException.class, () -> this.accountService.checkActiveAccount(account, "origin"));
	}
	
	@Test
	public void accountNotFound() throws Exception {
		when(this.repository.findByCbuOrAlias("", "")).thenReturn(Optional.of(new Account()));
		assertThrows(ResponseStatusException.class, () -> this.accountService.getAccountByCbuOrAlias("asd", "as"));
	}
	
	@Test
	public void insufficientAmount() {
		when(this.repository.findByCbuOrAlias("00001111", null)).thenReturn(Optional.of(TestUtils.getAccount()));
		when(this.repository.findByCbuOrAlias("00001112", null)).thenReturn(Optional.of(TestUtils.getAccountB()));
		
		TransferDTO transfer = TestUtils.trasfer();
		transfer.setAmount(new BigDecimal(5000));
		assertThrows(ResponseStatusException.class, () -> this.accountService.transferAmount(transfer));
	}
	
	@Test
	public void accountNotEquals() {
		Account account = TestUtils.getAccount();
		Account accountB = TestUtils.getAccountB();
		assertFalse(account.equals(accountB));
	}
	
	@Test
	public void checkToString() {
		assertTrue(TestUtils.getAccount().toString().contains("arroz.mockito"));
	}
}
