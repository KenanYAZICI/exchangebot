package com.chaintech.exchangebot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chaintech.exchangebot.entities.TransactionOrderSimulation;

public interface TransactionOrderSimulationRepository extends JpaRepository<TransactionOrderSimulation, Long>  {

}
