package com.github.mangila.springbootrestfulservice.web.repository.v1;

import com.github.mangila.springbootrestfulservice.db.CustomerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<CustomerDocument,String> {
    CustomerDocument findByName(String name);
}
