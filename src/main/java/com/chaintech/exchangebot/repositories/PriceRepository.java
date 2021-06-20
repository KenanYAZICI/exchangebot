package com.chaintech.exchangebot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chaintech.exchangebot.entities.Price;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

}
