package com.github.mangila.springbootrestfulservice.web.service.v1;


import com.github.mangila.springbootrestfulservice.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.web.exception.ResourceNotFoundException;
import com.github.mangila.springbootrestfulservice.web.mapstruct.CustomerMapper;
import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import com.github.mangila.springbootrestfulservice.web.repository.v1.CustomerRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return this.mapper.toDto(this.repository.findAll());
    }

    public CustomerDocument findByName(String name) {
        return this.repository.findByName(name);
    }

    public CustomerDto findById(String id) {
        val c = this.repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return this.mapper.toDto(c);
    }

    public String insert(CustomerDto customerDto) {
        val c = this.mapper.toDocument(customerDto);
        return this.repository.insert(c).getId();
    }

    public void deleteById(String id) {
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public String update(String id, CustomerDto customerDto) {
        if (this.repository.existsById(id)) {
            return this.repository.save(this.mapper.toDocument(customerDto)).getId();
        } else {
            return this.insert(customerDto);
        }
    }
}
