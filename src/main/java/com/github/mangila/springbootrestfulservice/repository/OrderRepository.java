package com.github.mangila.springbootrestfulservice.repository;

import com.github.mangila.springbootrestfulservice.domain.v1.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<OrderDocument, String> {
}
