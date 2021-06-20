package com.chaintech.exchangebot.async;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.impl.BinanceApiRestClientImpl;
import com.chaintech.exchangebot.config.BinanceConfig;
import com.chaintech.exchangebot.dto.CandleDto;
import com.chaintech.exchangebot.dto.PriceDto;
import com.chaintech.exchangebot.enums.EchangeBotEnums.ExchangeMarketType;
import com.chaintech.exchangebot.services.CandleService;
import com.chaintech.exchangebot.util.DecimalFormatUtil;

@Component
public class CandleStickAsync {


	@Autowired
	private CandleService candleService;
	
	@Autowired
	private TransactionOrderAsync transactionOrderAsync;
	
	@Autowired
	private BinanceConfig binanceConfig;

	@Async
	public Future<Void> candleStickProcess(CandlestickInterval candlestickInterval, PriceDto priceDto,BigDecimal capitalValue, BigDecimal investPercentValue, BigDecimal feeRateValue) throws InterruptedException {

		BinanceApiRestClientImpl apiRestClientImpl = new BinanceApiRestClientImpl(
				binanceConfig.getApiKey(),
				binanceConfig.getSecret());
		System.out.println("Processing symbol:" + priceDto.getSymbol() + " with:" + candlestickInterval.toString());
		List<Candlestick> candlesticks = apiRestClientImpl.getCandlestickBars(priceDto.getSymbol(), candlestickInterval,
				1000,null,null);

		List<CandleDto> candleDtos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(candlesticks)) {

			for (Candlestick cs : candlesticks) {
				CandleDto candleDto = new CandleDto();
				candleDto.setClose(DecimalFormatUtil.parseBigDecimal(cs.getClose()));
				candleDto.setCloseTime(Instant.ofEpochMilli(cs.getCloseTime()));
				candleDto.setExchangeMarket(ExchangeMarketType.BINANCE);
				candleDto.setHigh(DecimalFormatUtil.parseBigDecimal(cs.getHigh()));
				candleDto.setLow(DecimalFormatUtil.parseBigDecimal(cs.getLow()));
				candleDto.setNumberOfTrades(cs.getNumberOfTrades());
				candleDto.setOpen(DecimalFormatUtil.parseBigDecimal(cs.getOpen()));
				candleDto.setOpenTime(Instant.ofEpochMilli(cs.getOpenTime()));
				candleDto.setQuoteAssetVolume(DecimalFormatUtil.parseBigDecimal(cs.getQuoteAssetVolume()));
				candleDto.setSymbol(priceDto.getSymbol());
				candleDto
						.setTakerBuyBaseAssetVolume(DecimalFormatUtil.parseBigDecimal(cs.getTakerBuyBaseAssetVolume()));
				candleDto.setTakerBuyQuoteAssetVolume(
						DecimalFormatUtil.parseBigDecimal(cs.getTakerBuyQuoteAssetVolume()));
				candleDto.setInterval(candlestickInterval.toString());
				candleDtos.add(candleDto);

				
			}
			
			candleDtos = candleService.saveAllCandles(candleDtos);
			
			transactionOrderAsync.transactionOrderProcess(candlestickInterval, priceDto, candleDtos, capitalValue, investPercentValue, feeRateValue);
			
//			BigDecimal capitalValue = DecimalFormatUtil.parseBigDecimal(capital);
//			BigDecimal investPercentValue = DecimalFormatUtil.parseBigDecimal(investPercent);
//			BigDecimal feeRateValue = DecimalFormatUtil.parseBigDecimal(feeRate);
			
//			Thread.sleep(5000);
			System.out.println("Processed symbol:" + priceDto.getSymbol() + " with:" + candlestickInterval.toString());


		}
		return new AsyncResult<Void>(null);
	}
}
