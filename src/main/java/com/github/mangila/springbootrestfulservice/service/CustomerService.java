package com.github.mangila.springbootrestfulservice.service;


import com.github.mangila.springbootrestfulservice.common.ApplicationException;
import com.github.mangila.springbootrestfulservice.persistence.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.persistence.repository.CustomerRepository;
import com.github.mangila.springbootrestfulservice.service.mapstruct.CustomerMapper;
import com.github.mangila.springbootrestfulservice.web.dto.CustomerDto;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = {"customer"})
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public List<CustomerDto> findAll() {
        return this.mapper.toDto(this.repository.findAll());
    }

    @Cacheable(key = "#id")
    public CustomerDto findById(final String id) {
        final CustomerDocument c = this.repository.findById(id).orElseThrow(() -> {
            throw new ApplicationException(String.format("ID: %s - Not Found", id));
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
    public void deleteById(String id) {
        this.repository.findById(id).ifPresentOrElse(repository::delete, () -> {
            throw new ApplicationException(String.format("ID: %s - Not Found", id));
        });
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
