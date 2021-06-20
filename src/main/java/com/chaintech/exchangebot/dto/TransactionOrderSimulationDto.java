package com.chaintech.exchangebot.dto;

import java.math.BigDecimal;

import com.chaintech.exchangebot.enums.EchangeBotEnums.ExchangeMarketType;

import lombok.Data;


@Data
public class TransactionOrderSimulationDto {
	
	private long id;
	
	private ExchangeMarketType exchangeMarket;
	
	private String symbol;
	
	private BigDecimal coinAmount;
	
	private BigDecimal coinAmountShort;
	
	private BigDecimal currencyAmount;
	
	private BigDecimal currencyAmountShort;
	
	private BigDecimal capitalAmount;
	
	private BigDecimal feeAmount;
	
	private BigDecimal feeAmountShort;
	
	private BigDecimal closeValue;
	
	private BigDecimal feeRate;
	
	private String orderType;
	
	private long candleT0Id;
	
	private long candleT1Id;
	
	private long candleT2Id;
	
	private long candleT3Id;
	
	private String candleInterval;
}
