package com.exceptioncoding.bank.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
		return Optional.of(this.accountRepository.findByCbuOrAlias(cbu, alias))
				.orElseThrow(() -> new Exception("Account not found"));
	}

	@Override
	public ResponseEntity<ResponseTransferDTO> transferAmount(TransferDTO transferDTO) throws Exception { 
	
		Account accountOrigin = this.getAccountByCbuOrAlias(transferDTO.getAccountOrigin(), null);
		Account accountDestination = this.getAccountByCbuOrAlias(transferDTO.getAccountDestination(), transferDTO.getAliasDestination());
		
		checkActiveAccount(accountOrigin, "origen");
		checkActiveAccount(accountDestination ,"destino");
		
		if (accountOrigin.getBalance().compareTo(transferDTO.getAmount()) == -1) {
			throw new Exception("Saldo insuficiente");
		}

		accountOrigin.setBalance(accountOrigin.getBalance().subtract(transferDTO.getAmount()));
		accountDestination.setBalance(accountDestination.getBalance().add(transferDTO.getAmount()));
		
		String destino = (transferDTO.getAccountDestination().isEmpty()) ? transferDTO.getAccountDestination() :transferDTO.getAliasDestination();
		ResponseTransferDTO response = new ResponseTransferDTO(transferDTO.getAccountOrigin(), destino, transferDTO.getReason(), transferDTO.getAmount());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public Account saveAccount(Account account) {
		return this.accountRepository.save(account);

	}

	public void checkActiveAccount(Account account, String origen) throws Exception {
		if(!account.getIsActive()) {
			String detalleCuenta = (account.getCbu().isEmpty()) ? account.getCbu() : account.getAlias();
			throw new Exception("La cuenta de " + origen + ": " +detalleCuenta +" esta desactivada");
		}
	}

	@Override
	public ResponseEntity<String> createAccount(AccountDTO accountDTO) {
		Account account = new Account();
		account.setAlias(accountDTO.getAlias());
		account.setBalance(new BigDecimal(0));
		
		account.setCbu( UUID.randomUUID().toString() );
		account.setIsActive(true);
		
		this.saveAccount(account);
		return new ResponseEntity<>("CREATED", HttpStatus.CREATED);
	}
}
