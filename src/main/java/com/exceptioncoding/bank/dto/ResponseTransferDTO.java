package com.exceptioncoding.bank.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseTransferDTO {
	private String cbuOrigin;
	private String destino;
	private String reason;
	private BigDecimal amount;
}
