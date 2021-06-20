package com.chaintech.exchangebot.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.chaintech.exchangebot.dto.TransactionOrderSimulationDto;
import com.chaintech.exchangebot.entities.TransactionOrderSimulation;
import com.chaintech.exchangebot.mapper.SelmaMapper;
import com.chaintech.exchangebot.repositories.TransactionOrderSimulationRepository;
import com.chaintech.exchangebot.services.TransactionOrderSimulationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class TransactionOrderSimulationServiceImpl implements TransactionOrderSimulationService {

	private final TransactionOrderSimulationRepository transactionOrderSimulationRepository;

	@Override
	public List<TransactionOrderSimulationDto> saveAllTransactionOrderSimulations(
			List<TransactionOrderSimulationDto> transactionOrderSimulationDtos) {
		List<TransactionOrderSimulation> transactionOrderSimulations = transactionOrderSimulationRepository
				.saveAll(SelmaMapper.INSTANCE.asTransactionOrderSimulations(transactionOrderSimulationDtos));
		if (!CollectionUtils.isEmpty(transactionOrderSimulations)) {
			return SelmaMapper.INSTANCE.asTransactionOrderSimulationDtos(transactionOrderSimulations);
		}
		return null;
	}

	@Override
	public TransactionOrderSimulationDto saveTransactionOrderSimulation(
			TransactionOrderSimulationDto transactionOrderSimulationDto) {
		TransactionOrderSimulation transactionOrderSimulation = transactionOrderSimulationRepository.save(SelmaMapper.INSTANCE.asTransactionOrderSimulation(transactionOrderSimulationDto));
		if(transactionOrderSimulation != null) {
			return SelmaMapper.INSTANCE.asTransactionOrderSimulationDto(transactionOrderSimulation);
		}
		return null;
	}

}
