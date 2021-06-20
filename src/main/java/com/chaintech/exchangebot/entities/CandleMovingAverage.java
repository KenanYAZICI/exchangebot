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
@Table(name = "candleMovingAverage")
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class CandleMovingAverage extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "symbol", nullable = false)
	@NotBlank
	private String symbol;
	
	@Column(name = "average", nullable = false,precision = 30, scale=8)
	@NotNull
	private BigDecimal average;
	
	@Column(name = "interval", nullable = false)
	@NotNull
	private String  interval;
	
	@Column(name = "closeTime", nullable = false)
	@NotNull
	private Instant closeTime;
	
	@Column(name = "trendInterval", nullable = false)
	@NotNull
	private Integer trendInterval;
	
//	
//	private Candle candle;

}
