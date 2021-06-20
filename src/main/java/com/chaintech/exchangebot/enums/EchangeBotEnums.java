package com.chaintech.exchangebot.enums;

public class EchangeBotEnums {

	public enum ExchangeMarketType {
		BINANCE, KRAKEN;
	}
	
	public enum OrderType{
		BUY, SELL;
	}
	
	public enum PositionType{
		LONG("Long"), SHORT("Short");
		
		private String value;
		PositionType(String value){
			this.value = value;
		}
	}
}
