package com.github.mangila.springbootrestfulservice.persistence.repository;

import com.github.mangila.springbootrestfulservice.persistence.domain.CustomerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<CustomerDocument, String> {
}
