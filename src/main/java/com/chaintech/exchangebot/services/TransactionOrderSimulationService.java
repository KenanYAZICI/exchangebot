package com.chaintech.exchangebot.services;

import java.util.List;

import com.chaintech.exchangebot.dto.TransactionOrderSimulationDto;

public interface TransactionOrderSimulationService {

	List<TransactionOrderSimulationDto> saveAllTransactionOrderSimulations(List<TransactionOrderSimulationDto> transactionOrderSimulations);
	TransactionOrderSimulationDto saveTransactionOrderSimulation(TransactionOrderSimulationDto transactionOrderSimulationDto);
}
