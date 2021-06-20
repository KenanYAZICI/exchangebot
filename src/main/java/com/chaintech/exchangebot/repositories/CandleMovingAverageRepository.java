package com.chaintech.exchangebot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chaintech.exchangebot.entities.CandleMovingAverage;

@Repository
public interface CandleMovingAverageRepository extends JpaRepository<CandleMovingAverage, Long> {

}
