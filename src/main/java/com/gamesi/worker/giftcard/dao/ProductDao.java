package com.gamesi.worker.giftcard.dao;

import com.gamesi.worker.giftcard.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductDao extends JpaRepository<ProductEntity,Long> {

    @Query(value = "select p from product p where p.productName like %?1% or p.description like %?1%")
     List<ProductEntity> customFind(String name);
}
