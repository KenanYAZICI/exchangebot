package com.chaintech.exchangebot.services;

import java.util.List;

import com.chaintech.exchangebot.dto.PriceDto;
import com.chaintech.exchangebot.entities.Price;

public interface PriceService {

	List<Price> saveAllPrices(List<PriceDto> priceDtos);
	Price savePrice(PriceDto priceDto);
}
