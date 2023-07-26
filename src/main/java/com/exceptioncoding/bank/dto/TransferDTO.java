package com.exceptioncoding.bank.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {
	private Long id;
	private BigDecimal amount;
	private String accountOrigin;
	private String accountDestination;
	private String aliasDestination;
	private String reason;
}
