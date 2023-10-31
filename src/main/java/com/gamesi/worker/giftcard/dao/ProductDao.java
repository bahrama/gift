package com.gamesi.worker.giftcard.dao;

import com.gamesi.worker.giftcard.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<ProductEntity,Long> {

}
