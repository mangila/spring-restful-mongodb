package com.github.mangila.springbootrestfulservice.repository;

import com.github.mangila.springbootrestfulservice.domain.v1.CustomerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<CustomerDocument, String> {
}
