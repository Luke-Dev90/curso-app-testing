package com.exceptioncoding.bank.utils;

import java.math.BigDecimal;

import com.exceptioncoding.bank.dto.TransferDTO;
import com.exceptioncoding.bank.model.Account;

public class TestUtils {
	public static Account getAccountNotActive() {
		Account account = new Account();
		account.setIdAccount(1L);
		account.setIsActive(false);
		account.setCbu("00001111");
		account.setAlias("arroz.mockito");
		account.setBalance(new BigDecimal(1000));
		return account;
	}
	public static Account getAccount() {
		Account account = new Account();
		account.setIdAccount(1L);
		account.setIsActive(true);
		account.setCbu("00001111");
		account.setAlias("arroz.mockito");
		account.setBalance(new BigDecimal(1000));
		return account;
	}
	
	public static Account getAccountB() {
		Account account = new Account();
		account.setIdAccount(2L);
		account.setIsActive(true);
		account.setCbu("00001112");
		account.setAlias("papas.mockito");
		account.setBalance(new BigDecimal(2000));
		return account;
	}
	
	public static TransferDTO trasfer() {
		TransferDTO transfer = new TransferDTO();
		transfer.setAccountOrigin("00001111");
		transfer.setAccountDestination("00001112");
		transfer.setAliasDestination(null);
		transfer.setAmount(new BigDecimal(100));
		transfer.setReason("viaje");
		return transfer;
	}
}
