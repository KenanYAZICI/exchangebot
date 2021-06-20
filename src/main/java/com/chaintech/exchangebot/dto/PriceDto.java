package com.chaintech.exchangebot.dto;

import java.math.BigDecimal;

import com.chaintech.exchangebot.enums.EchangeBotEnums.ExchangeMarketType;

import lombok.Data;

@Data
public class PriceDto {

	private long id;

	private String symbol;

	private BigDecimal price;

	private ExchangeMarketType exchangeMarket;
}
