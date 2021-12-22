package com.github.mangila.springbootrestfulservice.persistence.repository;

import com.github.mangila.springbootrestfulservice.persistence.domain.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<OrderDocument, String> {
}
