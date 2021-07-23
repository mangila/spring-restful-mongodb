package com.github.mangila.springbootrestfulservice.web.service.v1;


import com.github.mangila.springbootrestfulservice.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.web.repository.v1.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    @Autowired
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public List<CustomerDocument> findAll() {
        return this.repository.findAll();
    }

    public CustomerDocument findByName(String name) {
        return this.repository.findByName(name);
    }

    public Optional<CustomerDocument> findById(String id) {
        return this.repository.findById(id);
    }
}
