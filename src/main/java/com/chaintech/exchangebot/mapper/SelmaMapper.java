package com.chaintech.exchangebot.mapper;

import java.util.List;

import com.chaintech.exchangebot.dto.CandleDto;
import com.chaintech.exchangebot.dto.PriceDto;
import com.chaintech.exchangebot.dto.TransactionOrderSimulationDto;
import com.chaintech.exchangebot.entities.Candle;
import com.chaintech.exchangebot.entities.Price;
import com.chaintech.exchangebot.entities.TransactionOrderSimulation;

import fr.xebia.extras.selma.IgnoreMissing;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.Selma;

@Mapper(withIgnoreMissing = IgnoreMissing.ALL)
public interface SelmaMapper {

	SelmaMapper INSTANCE = Selma.builder(SelmaMapper.class).build();

	PriceDto asPriceDto(Price project);

	List<PriceDto> asPriceDtos(List<Price> prices);

	Price asPrice(PriceDto priceDto);

	List<Price> asPrices(List<PriceDto> priceDtos);

	CandleDto asCandleDto(Candle candle);

	List<CandleDto> asCandleDto(List<Candle> candles);

	Candle asCandle(CandleDto candleDto);

	List<Candle> asCandles(List<CandleDto> candleDtos);

	TransactionOrderSimulationDto asTransactionOrderSimulationDto(
			TransactionOrderSimulation transactionOrderSimulation);

	List<TransactionOrderSimulationDto> asTransactionOrderSimulationDtos(
			List<TransactionOrderSimulation> transactionOrderSimulations);

	TransactionOrderSimulation asTransactionOrderSimulation(
			TransactionOrderSimulationDto transactionOrderSimulationDto);

	List<TransactionOrderSimulation> asTransactionOrderSimulations(
			List<TransactionOrderSimulationDto> transactionOrderSimulationDtos);

//    ProjectDto asProjectDto(Project project);
//
//    Project asProject(ProjectDto projectDto);
//
//    SdlcSystemDto asSdlcSystemDto(SdlcSystem sdlcSystem);
}
