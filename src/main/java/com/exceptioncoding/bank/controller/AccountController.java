package com.exceptioncoding.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exceptioncoding.bank.dto.AccountDTO;
import com.exceptioncoding.bank.dto.ResponseTransferDTO;
import com.exceptioncoding.bank.dto.TransferDTO;
import com.exceptioncoding.bank.service.AccountService;

@RestController
@RequestMapping("/home-baking")
public class AccountController {

	@Autowired
	private AccountService service;
	
	@PostMapping("/transfer")
	public ResponseEntity<ResponseTransferDTO> transfer(TransferDTO transferDTO) throws Exception{
		return this.service.transferAmount(transferDTO);
	}
	
	@PostMapping("/create")
	public ResponseEntity<String> create(AccountDTO accountDTO){
		return this.service.createAccount(accountDTO);
	}
	
}
