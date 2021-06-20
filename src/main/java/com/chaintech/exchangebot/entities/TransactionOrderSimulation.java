package com.chaintech.exchangebot.entities;

import java.math.BigDecimal;

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
import com.chaintech.exchangebot.enums.EchangeBotEnums.OrderType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "transactionOrderSimulation")
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class TransactionOrderSimulation extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "exchangeMarket", nullable = false)
	@NotNull
	private ExchangeMarketType exchangeMarket;
	
	@Column(name = "symbol", nullable = false)
	@NotBlank
	private String symbol;
	
	@Column(name = "coinAmount", nullable = false,precision = 19, scale=8)
	@NotNull
	private BigDecimal coinAmount;
	
	@Column(name = "coinAmountShort", nullable = false,precision = 19, scale=8)
	@NotNull
	private BigDecimal coinAmountShort;
	
	@Column(name = "currencyAmount", nullable = false,precision = 19, scale=8)
	@NotNull
	private BigDecimal currencyAmount;
	
	@Column(name = "currencyAmountShort", nullable = false,precision = 19, scale=8)
	@NotNull
	private BigDecimal currencyAmountShort;
	
	@Column(name = "capitalAmount", nullable = false,precision = 19, scale=8)
	@NotNull
	private BigDecimal capitalAmount;
	
	@Column(name = "feeAmount", nullable = false,precision = 19, scale=8)
	@NotNull
	private BigDecimal feeAmount;
	
	@Column(name = "feeAmountShort", nullable = false,precision = 19, scale=8)
	@NotNull
	private BigDecimal feeAmountShort;
	
	@Column(name = "closeValue", nullable = false,precision = 19, scale=8)
	@NotNull
	private BigDecimal closeValue;
	
	@Column(name = "feeRate", nullable = false,precision = 19, scale=8)
	@NotNull
	private BigDecimal feeRate;
	
	@Column(name = "orderType", nullable = false)
	@NotNull
	private String orderType;
	
	@Column(name = "candleT0Id", nullable = false)
	@NotNull
	private long candleT0Id;
	
	@Column(name = "candleT1Id", nullable = false)
	@NotNull
	private long candleT1Id;
	
	@Column(name = "candleT2Id", nullable = false)
	@NotNull
	private long candleT2Id;
	
	@Column(name = "candleT3Id", nullable = false)
	@NotNull
	private long candleT3Id;
	
	@Column(name = "candleInterval", nullable = false)
	@NotNull
	private String candleInterval;

}
