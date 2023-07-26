package com.exceptioncoding.bank.service;

import org.springframework.http.ResponseEntity;

import com.exceptioncoding.bank.dto.AccountDTO;
import com.exceptioncoding.bank.dto.ResponseTransferDTO;
import com.exceptioncoding.bank.dto.TransferDTO;
import com.exceptioncoding.bank.model.Account;

public interface AccountService {
	Account getAccountByCbuOrAlias(String cbu, String alias) throws Exception;
	ResponseEntity<ResponseTransferDTO> transferAmount(TransferDTO transferDto) throws Exception;
	Account saveAccount(Account account);
	ResponseEntity<String> createAccount(AccountDTO accountDTO);
}
