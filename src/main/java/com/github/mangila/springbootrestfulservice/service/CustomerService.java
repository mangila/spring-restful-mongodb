package com.github.mangila.springbootrestfulservice.service;


import com.github.mangila.springbootrestfulservice.domain.v1.CustomerDocument;
import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import com.github.mangila.springbootrestfulservice.mapstruct.CustomerMapper;
import com.github.mangila.springbootrestfulservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;

@Service
@CacheConfig(cacheNames = {"customer"})
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Autowired
    public CustomerService(CustomerRepository repository, CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CustomerDto> findAll() {
        return this.mapper.toDto(this.repository.findAll());
    }

    @Cacheable(key = "#id")
    public CustomerDto findById(final String id) throws MissingResourceException {
        final CustomerDocument c = this.repository.findById(id).orElseThrow(() -> {
            throw new MissingResourceException("Not Found", CustomerDto.class.getName(), id);
        });
        return this.mapper.toDto(c);
    }

    public String insert(final CustomerDto customerDto) {
        final CustomerDocument c = this.mapper.toDocument(customerDto);
        c.setRegistration(LocalDate.now());
        c.setOrderHistory(new ArrayList<>());
        return this.repository.insert(c).getId();
    }

    @CacheEvict(key = "#id")
    public void deleteById(String id) throws MissingResourceException {
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
        } else {
            throw new MissingResourceException("Not Found", CustomerDto.class.getName(), id);
        }
    }


    @CacheEvict(key = "#result")
    public String update(String id, CustomerDto customerDto) {
        if (this.repository.existsById(id)) {
            customerDto.setId(id);
            return this.repository.save(this.mapper.toDocument(customerDto)).getId();
        } else {
            return this.insert(customerDto);
        }
    }
}
