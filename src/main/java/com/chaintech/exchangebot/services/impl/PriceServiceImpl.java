package com.chaintech.exchangebot.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chaintech.exchangebot.dto.PriceDto;
import com.chaintech.exchangebot.entities.Price;
import com.chaintech.exchangebot.mapper.SelmaMapper;
import com.chaintech.exchangebot.repositories.PriceRepository;
import com.chaintech.exchangebot.services.PriceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class PriceServiceImpl implements PriceService {

	private final PriceRepository priceRepository;

	@Override
	public List<Price> saveAllPrices(List<PriceDto> priceDtos) {

		return priceRepository.saveAll(SelmaMapper.INSTANCE.asPrices(priceDtos));
//		 System.out.println(prices);

	}

	@Override
	public Price savePrice(PriceDto priceDto) {

		return priceRepository.save(SelmaMapper.INSTANCE.asPrice(priceDto));
//		 System.out.println(prices);

	}

}
