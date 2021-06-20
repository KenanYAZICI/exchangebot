package com.chaintech.exchangebot.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.client.impl.BinanceApiRestClientImpl;
import com.chaintech.exchangebot.async.CandleStickAsync;
import com.chaintech.exchangebot.config.BinanceConfig;
import com.chaintech.exchangebot.dto.CandleDto;
import com.chaintech.exchangebot.dto.PriceDto;
import com.chaintech.exchangebot.dto.TransactionOrderSimulationDto;
import com.chaintech.exchangebot.enums.EchangeBotEnums.ExchangeMarketType;
import com.chaintech.exchangebot.enums.EchangeBotEnums.OrderType;
import com.chaintech.exchangebot.enums.EchangeBotEnums.PositionType;
import com.chaintech.exchangebot.services.CandleService;
import com.chaintech.exchangebot.services.PriceService;
import com.chaintech.exchangebot.services.TransactionOrderSimulationService;
import com.chaintech.exchangebot.util.DecimalFormatUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(PriceRestController.ENDPOINT)
@Api(produces = MediaType.APPLICATION_JSON_VALUE, tags = "Price")
@AllArgsConstructor
public class PriceRestController {

	private static final Logger LOGGER = Logger.getLogger(PriceRestController.class);

	public static final String ENDPOINT = "/api/v1/prices";

	public static final String ENDPOINT_SYMBOL_INTERVAL = "/{symbol}/{interval}/{capital}/{feeRate}/{investPercent}";

	public static final String PATH_VARIABLE_SYMBOL = "symbol";

//	private static final String API_PARAM_SYMBOL = "symbol";

	public static final String PATH_VARIABLE_INTERVAL = "interval";

	public static final String PATH_VARIABLE_CAPITAL = "capital";

	public static final String PATH_VARIABLE_FEE_RATE = "feeRate";

	public static final String PATH_VARIABLE_INVEST_PERCENT = "investPercent";

//	private static final String API_PARAM_INTERVAL = "interval";

	@Autowired
	private PriceService priceService;
	
	@Autowired
	private CandleStickAsync candleStickAsync;
	
	
	@Autowired
	private BinanceConfig binanceConfig;
	
//
//	@Autowired
//	private TransactionOrderSimulationService transactionOrderSimulationService;
	
	
	

	@ApiOperation("Get & Save Prices ")
	@GetMapping(ENDPOINT_SYMBOL_INTERVAL)
	public ResponseEntity<String> getPrices(
			@ApiParam(name = PATH_VARIABLE_SYMBOL, required = true, defaultValue = "BTCUSDT") @PathVariable(PATH_VARIABLE_SYMBOL) final String symbol,
			@ApiParam(name = PATH_VARIABLE_INTERVAL, required = true, defaultValue = "FOUR_HOURLY") @PathVariable(PATH_VARIABLE_INTERVAL) final String interval,
			@ApiParam(name = PATH_VARIABLE_CAPITAL, required = true, defaultValue = "1000") @PathVariable(PATH_VARIABLE_CAPITAL) final String capital,
			@ApiParam(name = PATH_VARIABLE_FEE_RATE, required = true, defaultValue = "0.001") @PathVariable(PATH_VARIABLE_FEE_RATE) final String feeRate,
			@ApiParam(name = PATH_VARIABLE_INVEST_PERCENT, required = true, defaultValue = "10") @PathVariable(PATH_VARIABLE_INVEST_PERCENT) final String investPercent) throws InterruptedException {

		BinanceApiRestClientImpl apiRestClientImpl = new BinanceApiRestClientImpl(
				binanceConfig.getApiKey(),
				binanceConfig.getSecret());

		List<TickerPrice> tickerPrices = apiRestClientImpl.getAllPrices();

		if (!CollectionUtils.isEmpty(tickerPrices)) {
			List<PriceDto> priceDtos = new ArrayList<>();
			for (TickerPrice tickerPrice : tickerPrices) {
				final BigDecimal price = DecimalFormatUtil.parseBigDecimal(tickerPrice.getPrice());
				if (BigDecimal.ZERO.compareTo(price) != 0) {
					PriceDto priceDto = new PriceDto();
					priceDto.setExchangeMarket(ExchangeMarketType.BINANCE);
					priceDto.setSymbol(tickerPrice.getSymbol());
					priceDto.setPrice(price);
					priceDtos.add(priceDto);

				}
			}
			priceService.saveAllPrices(priceDtos);
//			HOTUSDT
//			BTTUSDT
//			XRPUSDT
			List<String> parites = new ArrayList<>();
			parites.add("HOTUSDT");
			parites.add("BTTUSDT");
			parites.add("XRPUSDT");
//			List<PriceDto> usdtPriceDtos = priceDtos.stream().filter(c -> c.getSymbol().endsWith("USDT"))
//					.collect(Collectors.toList());
			List<PriceDto> usdtPriceDtos = priceDtos.stream().filter(c -> parites.contains(c.getSymbol()))
					.collect(Collectors.toList());
			BigDecimal capitalValue = DecimalFormatUtil.parseBigDecimal(capital);
			BigDecimal investPercentValue = DecimalFormatUtil.parseBigDecimal(investPercent);
			BigDecimal feeRateValue = DecimalFormatUtil.parseBigDecimal(feeRate);
//			candleStickAsync.calculate(CandlestickInterval.FIVE_MINUTES, usdtPriceDtos.get(0));
			if (!CollectionUtils.isEmpty(usdtPriceDtos)) {
				List<CandlestickInterval> candlestickIntervals = new ArrayList<>();
//				candlestickIntervals.add(CandlestickInterval.FIVE_MINUTES);
//				candlestickIntervals.add(CandlestickInterval.FIFTEEN_MINUTES);
//				candlestickIntervals.add(CandlestickInterval.HALF_HOURLY);
//				candlestickIntervals.add(CandlestickInterval.HOURLY);
//				candlestickIntervals.add(CandlestickInterval.TWO_HOURLY);
//				candlestickIntervals.add(CandlestickInterval.FOUR_HOURLY);
				candlestickIntervals.add(CandlestickInterval.DAILY);
				for (PriceDto priceDto : usdtPriceDtos) {
					for (CandlestickInterval candlestickInterval : candlestickIntervals) {

						candleStickAsync.candleStickProcess(candlestickInterval, priceDto, capitalValue,investPercentValue, feeRateValue);
//						CandlestickInterval candlestickInterval = CandlestickInterval.valueOf(interval);

					}
				}

			}

		}
//		List<String> symbols = new ArrayList<>();
//		symbols.add("HOTUSDT");
//		symbols.add("BTTUSDT");
//		symbols.add("BTCUSDT");
//		symbols.add("AVAXUSDT");
//		symbols.add("ETHUSDT");
//		symbols.add("SXPUSDT");
//		symbols.add("NEOUSDT");
//		symbols.add("DOGEUSDT");
//		symbols.add("XRPUSDT");
		System.out.println("Process Finished");
		return ResponseEntity.ok().body("symbol:" + symbol + "-interval:" + interval);
	}

	private void getAndSaveAllPrices() {
		BinanceApiRestClientImpl apiRestClientImpl = new BinanceApiRestClientImpl(
				"53K8i1g0Qpa6eWobTrp1PU6wyVRMpR263EWUFeFUQLFGIQj8KfqNA0YXycpWcwLK",
				"gJZbBWJm1C2cT0cZ7r0KBWehHOdrvxh8eS52WcDjhZKeYwbdkLGHEtQIzBTzk3Pm");
		List<TickerPrice> prices = apiRestClientImpl.getAllPrices();
		if (!CollectionUtils.isEmpty(prices)) {
			List<PriceDto> priceDtos = new ArrayList<>();
			for (TickerPrice tp : prices) {
				final BigDecimal price = DecimalFormatUtil.parseBigDecimal(tp.getPrice());
				if (BigDecimal.ZERO.compareTo(price) != 0) {
					PriceDto priceDto = new PriceDto();
					priceDto.setExchangeMarket(ExchangeMarketType.BINANCE);
					priceDto.setSymbol(tp.getSymbol());
					priceDto.setPrice(price);
					priceDtos.add(priceDto);
				}
			}
			priceService.saveAllPrices(priceDtos);
		}
	}

}
