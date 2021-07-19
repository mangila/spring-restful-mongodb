package com.github.mangila.springbootrestfulservice.web.repository.v1;

import com.github.mangila.springbootrestfulservice.db.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<OrderDocument, String> {
}
