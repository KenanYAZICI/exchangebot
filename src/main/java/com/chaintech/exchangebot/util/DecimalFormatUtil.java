package com.chaintech.exchangebot.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.util.NumberUtils;

public class DecimalFormatUtil {

	private static final Logger LOGGER = Logger.getLogger(DecimalFormatUtil.class);

//	private static final DecimalFormat DECIMAL_FORMAT;
//	static {
//		DECIMAL_FORMAT = (DecimalFormat) DecimalFormat.getInstance();
//		DECIMAL_FORMAT.setMaximumFractionDigits(8);
//		DECIMAL_FORMAT.setMinimumFractionDigits(8);
//		DECIMAL_FORMAT.setParseBigDecimal(true);
//		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
//		decimalFormatSymbols.setDecimalSeparator('.');
//		decimalFormatSymbols.setGroupingSeparator(',');
//		DECIMAL_FORMAT.setDecimalFormatSymbols(decimalFormatSymbols);
//	}

	public static BigDecimal parseBigDecimal(String value) {

		return NumberUtils.parseNumber(value, BigDecimal.class).setScale(8, RoundingMode.DOWN);// ((BigDecimal)
																// DECIMAL_FORMAT.parse(value)).setScale(8,
																// RoundingMode.DOWN);

	}
}
