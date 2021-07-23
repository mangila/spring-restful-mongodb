package com.github.mangila.springbootrestfulservice.web.service.v1;


import com.github.mangila.springbootrestfulservice.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.web.exception.ResourceNotFoundException;
import com.github.mangila.springbootrestfulservice.web.mapstruct.CustomerMapper;
import com.github.mangila.springbootrestfulservice.web.model.v1.dto.CustomerDto;
import com.github.mangila.springbootrestfulservice.web.repository.v1.CustomerRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Autowired
    public CustomerService(CustomerRepository repository, CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CustomerDto> findAll() {
        val l = new ArrayList<CustomerDto>();
        this.repository.findAll()
                .forEach(c -> l.add(this.mapper.toDto(c)));
        return l;
    }

    public CustomerDocument findByName(String name) {
        return this.repository.findByName(name);
    }

    public CustomerDto findById(String id) {
        val c = this.repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return this.mapper.toDto(c);
    }

    public String insertNewCustomer(CustomerDto customerDto) {
        val c = this.mapper.toDocument(customerDto);
        return this.repository.insert(c).getId();
    }

    public boolean existsById(String id) {
        return this.repository.existsById(id);
    }

    public void deleteById(String id) {
        this.repository.deleteById(id);
    }

    public void updateCustomer(CustomerDto customerDto) {
        val c = this.mapper.toDocument(customerDto);
        this.repository.save(c);
    }
}
