package com.chaintech.exchangebot.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.chaintech.exchangebot.dto.CandleDto;
import com.chaintech.exchangebot.entities.Candle;
import com.chaintech.exchangebot.mapper.SelmaMapper;
import com.chaintech.exchangebot.repositories.CandleRepository;
import com.chaintech.exchangebot.services.CandleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class CandleServiceImpl implements CandleService {

	private final CandleRepository candleRepository;
	
	
	@Override
	public List<CandleDto> saveAllCandles(List<CandleDto> candleDtos) {
		List<Candle> candles = candleRepository.saveAll(SelmaMapper.INSTANCE.asCandles(candleDtos));
		if(!CollectionUtils.isEmpty(candles)) {
			return SelmaMapper.INSTANCE.asCandleDto(candles);
		}
		return null;
	}

	@Override
	public CandleDto saveCandle(CandleDto candleDto) {
		
		Candle candle = candleRepository.save(SelmaMapper.INSTANCE.asCandle(candleDto));
		if(candle != null) {
			return SelmaMapper.INSTANCE.asCandleDto(candle);
		}
		return null;
	}

}
