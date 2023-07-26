package com.exceptioncoding.bank.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDTO {
	private String alias;
	private String cbu;
	private BigDecimal balance;
}
