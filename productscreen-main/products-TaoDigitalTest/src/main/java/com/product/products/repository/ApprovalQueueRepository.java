package com.product.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.products.entity.ApprovalQueueEntity;
import com.product.products.entity.ProductEntity;

@Repository
public interface ApprovalQueueRepository extends JpaRepository<ApprovalQueueEntity, Long> {

    public ApprovalQueueEntity findByproductEntity(ProductEntity productEntity);

    public ApprovalQueueEntity deleteByproductEntity(ProductEntity productEntity);
}
