package com.chaintech.exchangebot.entities;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.chaintech.exchangebot.entities.base.BaseEntity;
import com.chaintech.exchangebot.enums.EchangeBotEnums.ExchangeMarketType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "candle")
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class Candle extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "symbol", nullable = false)
	@NotBlank
	private String symbol;
	
	@Column(name = "open", nullable = false,precision = 30, scale=8)
	@NotNull
	private BigDecimal open;
	
	@Column(name = "high", nullable = false,precision = 30, scale=8)
	@NotNull
	private BigDecimal high;
	
	@Column(name = "low", nullable = false,precision = 30, scale=8)
	@NotNull
	private BigDecimal low;
	
	@Column(name = "close", nullable = false,precision = 30, scale=8)
	@NotNull
	private BigDecimal close;
	
	@Column(name = "openTime", nullable = false)
	@NotNull
	private Instant openTime;
	
	@Column(name = "closeTime", nullable = false)
	@NotNull
	private Instant closeTime;
	
	@Column(name = "quoteAssetVolume", nullable = false,precision = 40, scale=8)
	@NotNull
	private BigDecimal quoteAssetVolume;
	
	@Column(name = "numberOfTrades", nullable = false)
	@NotNull
	private Long numberOfTrades;
	
	@Column(name = "takerBuyBaseAssetVolume", nullable = false,precision = 40, scale=8)
	@NotNull
	private BigDecimal takerBuyBaseAssetVolume;
	
	@Column(name = "takerBuyQuoteAssetVolume", nullable = false,precision = 40, scale=8)
	@NotNull
	private BigDecimal takerBuyQuoteAssetVolume;
	
	@Column(name = "exchangeMarket", nullable = false)
	@NotNull
	private ExchangeMarketType exchangeMarket;
	
	@Column(name = "interval", nullable = false)
	@NotNull
	private String  interval;
	
}
