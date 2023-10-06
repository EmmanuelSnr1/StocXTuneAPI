package com.stocxtune.api.dao;

import com.stocxtune.api.model.Investment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentDao extends JpaRepository<Investment,Integer> {
}
