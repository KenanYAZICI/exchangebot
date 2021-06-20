package com.chaintech.exchangebot.controller;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.impl.BinanceApiRestClientImpl;
import com.chaintech.exchangebot.async.CandleStickAsync;
import com.chaintech.exchangebot.config.BinanceConfig;
import com.chaintech.exchangebot.dto.CandleDto;
import com.chaintech.exchangebot.enums.EchangeBotEnums.ExchangeMarketType;
import com.chaintech.exchangebot.repositories.CandleMovingAverageRepository;
import com.chaintech.exchangebot.repositories.CandleRepository;
import com.chaintech.exchangebot.services.CandleService;
import com.chaintech.exchangebot.services.PriceService;
import com.chaintech.exchangebot.util.DecimalFormatUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(AveragesRestController.ENDPOINT)
@Api(produces = MediaType.APPLICATION_JSON_VALUE, tags = "Average")
@AllArgsConstructor
public class AveragesRestController {

	public static final String ENDPOINT = "/api/v1/averages";
	
	public static final String ENDPOINT_SYMBOL_INTERVAL = "/{symbol}/{interval}/{capital}/{feeRate}/{investPercent}/{candleLimit}";
	
	public static final String PATH_VARIABLE_SYMBOL = "symbol";

//	private static final String API_PARAM_SYMBOL = "symbol";

	public static final String PATH_VARIABLE_INTERVAL = "interval";

	public static final String PATH_VARIABLE_CAPITAL = "capital";

	public static final String PATH_VARIABLE_FEE_RATE = "feeRate";

	public static final String PATH_VARIABLE_INVEST_PERCENT = "investPercent";	
	
	public static final String PATH_VARIABLE_CANDLE_LIMIT = "candleLimit";
	
	@Autowired
	private BinanceConfig binanceConfig;
	
	@Autowired
	private CandleStickAsync candleStickAsync;
	
	@Autowired
	private CandleService candleService;
	
	@Autowired
	private CandleMovingAverageRepository candleMovingAverageRepository;
	
	@ApiOperation("Get & Save Averages ")
	@GetMapping(ENDPOINT_SYMBOL_INTERVAL)
	public ResponseEntity<String> getAverages(@ApiParam(name = PATH_VARIABLE_SYMBOL, required = true, defaultValue = "HOTUSDT") @PathVariable(PATH_VARIABLE_SYMBOL) final String symbol,
			@ApiParam(name = PATH_VARIABLE_INTERVAL, required = true, defaultValue = "DAILY") @PathVariable(PATH_VARIABLE_INTERVAL) final String interval,
			@ApiParam(name = PATH_VARIABLE_CAPITAL, required = true, defaultValue = "1000") @PathVariable(PATH_VARIABLE_CAPITAL) final String capital,
			@ApiParam(name = PATH_VARIABLE_FEE_RATE, required = true, defaultValue = "0.001") @PathVariable(PATH_VARIABLE_FEE_RATE) final String feeRate,
			@ApiParam(name = PATH_VARIABLE_INVEST_PERCENT, required = true, defaultValue = "10") @PathVariable(PATH_VARIABLE_INVEST_PERCENT) final String investPercent,
			@ApiParam(name = PATH_VARIABLE_CANDLE_LIMIT, required = true, defaultValue = "1000") @PathVariable(PATH_VARIABLE_CANDLE_LIMIT) final String candleLimit
			){

		BinanceApiRestClientImpl apiRestClientImpl = new BinanceApiRestClientImpl(
				binanceConfig.getApiKey(),
				binanceConfig.getSecret());
		CandlestickInterval candlestickInterval = CandlestickInterval.valueOf(interval);
		Integer candleLimitInt = Integer.parseInt(candleLimit);
		List<Candlestick> candlesticks = apiRestClientImpl.getCandlestickBars(symbol, candlestickInterval,
				candleLimitInt,null,null);
		candlesticks.sort((p1, p2) -> p1.getCloseTime().compareTo(p2.getCloseTime()));
		List<CandleDto> candleDtos = new ArrayList<>();
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
			candleDto.setSymbol(symbol);
			candleDto
					.setTakerBuyBaseAssetVolume(DecimalFormatUtil.parseBigDecimal(cs.getTakerBuyBaseAssetVolume()));
			candleDto.setTakerBuyQuoteAssetVolume(
					DecimalFormatUtil.parseBigDecimal(cs.getTakerBuyQuoteAssetVolume()));
			candleDto.setInterval(candlestickInterval.toString());
			candleDtos.add(candleDto);
		}
		
		candleDtos = candleService.saveAllCandles(candleDtos);
		
		List<Integer> trendIntervals = new ArrayList<>();
		trendIntervals.add(3);
		trendIntervals.add(20);
		trendIntervals.add(50);
//		for (Integer integer : trendIntervals) {
//			
//		}
		for (int i = 2; i < candleDtos.size(); i++) {
			BigDecimal movingCloseAverage = BigDecimal.ZERO;
			BigDecimal divider =  BigDecimal.ZERO;
			for (int j = 0; j < 3; j++) {
				CandleDto candleDto = candleDtos.get(i-j);
				BigDecimal weight = BigDecimal.valueOf(3-j);
				movingCloseAverage = movingCloseAverage.add(candleDto.getClose()).multiply(weight);
				divider = divider.add(weight);
			}
			movingCloseAverage = movingCloseAverage.divide(divider);
		}
		
		
		return ResponseEntity.ok().body("symbol:" + symbol + "-interval:" + interval);
	}
}
