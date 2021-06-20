package com.chaintech.exchangebot.async;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.binance.api.client.domain.market.CandlestickInterval;
import com.chaintech.exchangebot.dto.CandleDto;
import com.chaintech.exchangebot.dto.PriceDto;
import com.chaintech.exchangebot.dto.TransactionOrderSimulationDto;
import com.chaintech.exchangebot.enums.EchangeBotEnums.ExchangeMarketType;
import com.chaintech.exchangebot.enums.EchangeBotEnums.OrderType;
import com.chaintech.exchangebot.services.TransactionOrderSimulationService;

@Component
public class TransactionOrderAsync {

	@Autowired
	private TransactionOrderSimulationService transactionOrderSimulationService;
	
	@Async
	public Future<Void> transactionOrderProcess(CandlestickInterval candlestickInterval, PriceDto priceDto, List<CandleDto> candleDtos,
			BigDecimal capitalValue, BigDecimal investPercentValue, BigDecimal feeRateValue)
			throws InterruptedException {
//		BigDecimal capitalValue = DecimalFormatUtil.parseBigDecimal(capital);
//		BigDecimal investPercentValue = DecimalFormatUtil.parseBigDecimal(investPercent);
//		BigDecimal feeRateValue = DecimalFormatUtil.parseBigDecimal(feeRate);
		if (!CollectionUtils.isEmpty(candleDtos) && candleDtos.size() > 3) {

			BigDecimal multiplier1 = BigDecimal.valueOf(0.5);
			BigDecimal multiplier2 = BigDecimal.valueOf(0.75);
			OrderType orderType = OrderType.SELL; // sat ile başla
			TransactionOrderSimulationDto transactionOrderSimulationDto = null;
			for (int i = 3; i < candleDtos.size(); i++) {
				OrderType previousOrderType = orderType;
				BigDecimal x01 = candleDtos.get(i - 2).getClose().add(candleDtos.get(i - 1).getClose()
						.subtract(candleDtos.get(i - 2).getClose()).multiply(multiplier1));

				BigDecimal x02 = candleDtos.get(i - 3).getClose().add(candleDtos.get(i - 1).getClose()
						.subtract(candleDtos.get(i - 3).getClose()).multiply(multiplier2));

				BigDecimal x03 = candleDtos.get(i).getClose();

				BigDecimal x04 = x01.compareTo(x02) > -1 ? x01 : x02;

				if (x03.compareTo(x04) > -1) {
					orderType = OrderType.BUY;
				} else {
					orderType = OrderType.SELL;
				}

				// Eğer pozisyon değiştiyse
				if (previousOrderType != orderType) {
					System.out.println(MessageFormat.format(
							"Pozisyon değişti. ordertype : {0} - index : {1} candle id : {2} - candleCloseTime : {3} - candleClosePrice : {4}",
							orderType, i, candleDtos.get(i).getId(), candleDtos.get(i).getCloseTime(),
							candleDtos.get(i).getClose()));

					if (OrderType.BUY.equals(orderType)) {
						TransactionOrderSimulationDto previousTransactionOrderSimulationDto = transactionOrderSimulationDto;
						transactionOrderSimulationDto = new TransactionOrderSimulationDto();
						if (previousTransactionOrderSimulationDto != null) {// öneden short
																			// pozisyona
																			// girmiş ise short u
																			// boz
							BigDecimal exchangeValueShort = candleDtos.get(i).getClose()
									.multiply(previousTransactionOrderSimulationDto.getCoinAmountShort());
							BigDecimal feeAmountShort = exchangeValueShort.multiply(feeRateValue);
							transactionOrderSimulationDto.setFeeAmountShort(feeAmountShort);
							exchangeValueShort = exchangeValueShort.subtract(feeAmountShort);
							transactionOrderSimulationDto.setCurrencyAmountShort(exchangeValueShort);

							capitalValue = capitalValue.add(exchangeValueShort);
						} else {
							transactionOrderSimulationDto.setFeeAmountShort(BigDecimal.ZERO);
							transactionOrderSimulationDto.setCurrencyAmountShort(BigDecimal.ZERO);
						}
						transactionOrderSimulationDto.setCoinAmountShort(BigDecimal.ZERO);
						// long al

						BigDecimal investValue = capitalValue.divide(BigDecimal.valueOf(100))
								.multiply(investPercentValue);
						// kalan anapara
						capitalValue = capitalValue.subtract(investValue);

						// komisyonu kes
						BigDecimal feeAmount = investValue.multiply(feeRateValue);
						transactionOrderSimulationDto.setFeeAmount(feeAmount);
						investValue = investValue.subtract(feeAmount);
						transactionOrderSimulationDto.setCurrencyAmount(investValue);
						BigDecimal exchangeValue = candleDtos.get(i).getClose();
						BigDecimal coinAmount = investValue.divide(exchangeValue, RoundingMode.DOWN);
						transactionOrderSimulationDto.setCoinAmount(coinAmount);

						transactionOrderSimulationDto.setCapitalAmount(capitalValue);

					} else {
						// long u sat
						TransactionOrderSimulationDto previousTransactionOrderSimulationDto = transactionOrderSimulationDto;

						transactionOrderSimulationDto = new TransactionOrderSimulationDto();
						BigDecimal exchangeValue = candleDtos.get(i).getClose()
								.multiply(previousTransactionOrderSimulationDto.getCoinAmount());
						BigDecimal feeAmount = exchangeValue.multiply(feeRateValue);
						transactionOrderSimulationDto.setFeeAmount(feeAmount);
						exchangeValue = exchangeValue.subtract(feeAmount);
						transactionOrderSimulationDto.setCurrencyAmount(exchangeValue);
						transactionOrderSimulationDto.setCoinAmount(BigDecimal.ZERO);
						capitalValue = capitalValue.add(exchangeValue);
						if (previousTransactionOrderSimulationDto != null) {// öneden Long pozisyona
							// girmiş ise short al
							BigDecimal investValueShort = capitalValue.divide(BigDecimal.valueOf(100))
									.multiply(investPercentValue);
							capitalValue = capitalValue.subtract(investValueShort);
							BigDecimal feeAmountShort = investValueShort.multiply(feeRateValue);
							transactionOrderSimulationDto.setFeeAmountShort(feeAmountShort);
							investValueShort = investValueShort.subtract(feeAmountShort);
							transactionOrderSimulationDto.setCurrencyAmountShort(investValueShort);
							BigDecimal coinAmountShort = investValueShort.divide(candleDtos.get(i).getClose(),
									RoundingMode.DOWN);
							transactionOrderSimulationDto.setCoinAmountShort(coinAmountShort);
						}
						transactionOrderSimulationDto.setCapitalAmount(capitalValue);
					}
					transactionOrderSimulationDto.setFeeRate(feeRateValue);
					transactionOrderSimulationDto.setExchangeMarket(ExchangeMarketType.BINANCE);
					transactionOrderSimulationDto.setSymbol(priceDto.getSymbol());
					transactionOrderSimulationDto.setOrderType(orderType.toString());
					transactionOrderSimulationDto.setCandleT0Id(candleDtos.get(i).getId());
					transactionOrderSimulationDto.setCandleT1Id(candleDtos.get(i - 1).getId());
					transactionOrderSimulationDto.setCandleT2Id(candleDtos.get(i - 2).getId());
					transactionOrderSimulationDto.setCandleT3Id(candleDtos.get(i - 3).getId());
					transactionOrderSimulationDto.setCloseValue(candleDtos.get(i).getClose());
					transactionOrderSimulationDto.setCandleInterval(candlestickInterval.toString());
					transactionOrderSimulationService.saveTransactionOrderSimulation(transactionOrderSimulationDto);

				}

			}
		}

		return new AsyncResult<Void>(null);
	}

}
