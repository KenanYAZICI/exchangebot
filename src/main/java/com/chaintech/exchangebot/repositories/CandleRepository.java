package com.chaintech.exchangebot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chaintech.exchangebot.entities.Candle;

@Repository
public interface CandleRepository extends JpaRepository<Candle, Long> {

}
