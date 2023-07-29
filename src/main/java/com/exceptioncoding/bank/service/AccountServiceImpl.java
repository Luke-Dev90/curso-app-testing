package com.exceptioncoding.bank.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.exceptioncoding.bank.dto.AccountDTO;
import com.exceptioncoding.bank.dto.ResponseTransferDTO;
import com.exceptioncoding.bank.dto.TransferDTO;
import com.exceptioncoding.bank.model.Account;
import com.exceptioncoding.bank.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Account getAccountByCbuOrAlias(String cbu, String alias) throws Exception {
		Optional<Account> account = this.accountRepository.findByCbuOrAlias(cbu, alias);

		if(!account.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found");
		}

		return account.get();
	}

	@Override
	public ResponseEntity<ResponseTransferDTO> transferAmount(TransferDTO transferDTO) throws Exception { 
	
		Account accountOrigin = this.getAccountByCbuOrAlias(transferDTO.getAccountOrigin(), null);
		Account accountDestination = this.getAccountByCbuOrAlias(transferDTO.getAccountDestination(), transferDTO.getAliasDestination());

		checkActiveAccount(accountOrigin, "origen");
		checkActiveAccount(accountDestination ,"destino");
		
		
		if (accountOrigin.getBalance().compareTo(transferDTO.getAmount()) == -1) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Saldo insuficiente");
		}

		accountOrigin.setBalance(accountOrigin.getBalance().subtract(transferDTO.getAmount()));
		accountDestination.setBalance(accountDestination.getBalance().add(transferDTO.getAmount()));
		
		this.saveAccount(accountOrigin);
		this.saveAccount(accountDestination);
		
		String destino = (transferDTO.getAccountDestination() != null || !transferDTO.getAccountDestination().isEmpty()) ? transferDTO.getAccountDestination() : transferDTO.getAliasDestination();
		System.out.println(destino);
		ResponseTransferDTO response = new ResponseTransferDTO(transferDTO.getAccountOrigin(), destino, transferDTO.getReason(), transferDTO.getAmount());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public Account saveAccount(Account account) {
		return this.accountRepository.save(account);

	}

	public Boolean checkActiveAccount(Account account, String origen) throws Exception {
		if(!account.getIsActive()) {
			String detalleCuenta = (!account.getCbu().isEmpty()) ? account.getCbu() : account.getAlias();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La cuenta de " + origen + ": " +detalleCuenta +" esta desactivada");
		}
		return true;
	}

	@Override
	public ResponseEntity<String> createAccount(AccountDTO accountDTO) {
		System.out.println("AccountDTO: "+ accountDTO);
		Account account = new Account();
		account.setAlias(accountDTO.getAlias());
		account.setBalance(new BigDecimal(0));
		
		account.setCbu( UUID.randomUUID().toString() );
		account.setIsActive(true);
		
		this.saveAccount(account);
		System.out.println("AccountSAVE: " + account);
		return new ResponseEntity<>("CREATED", HttpStatus.CREATED);
	}
}
