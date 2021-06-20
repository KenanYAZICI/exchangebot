package com.chaintech.exchangebot.services;

import java.util.List;

import com.chaintech.exchangebot.dto.CandleDto;

public interface CandleService {

	List<CandleDto> saveAllCandles(List<CandleDto> candleDtos);
	CandleDto saveCandle(CandleDto candleDto);
}
