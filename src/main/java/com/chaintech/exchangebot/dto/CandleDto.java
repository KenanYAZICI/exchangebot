package com.chaintech.exchangebot.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.chaintech.exchangebot.enums.EchangeBotEnums.ExchangeMarketType;

import lombok.Data;

@Data
public class CandleDto {

	private long id;

	private String symbol;

	private BigDecimal open;

	private BigDecimal high;

	private BigDecimal low;	

	private BigDecimal close;

	private Instant openTime;

	private Instant closeTime;

	private BigDecimal quoteAssetVolume;

	private Long numberOfTrades;

	private BigDecimal takerBuyBaseAssetVolume;

	private BigDecimal takerBuyQuoteAssetVolume;

	private ExchangeMarketType exchangeMarket;
	
	private String  interval;
}
