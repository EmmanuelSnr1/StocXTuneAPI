package com.stocxtune.api.dao;

import com.stocxtune.api.model.stock.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetDAO extends JpaRepository<Asset, String> {
}
