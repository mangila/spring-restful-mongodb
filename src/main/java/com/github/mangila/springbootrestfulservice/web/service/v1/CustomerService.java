package com.github.mangila.springbootrestfulservice.web.service.v1;


import com.github.mangila.springbootrestfulservice.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import com.github.mangila.springbootrestfulservice.web.exception.ResourceNotFoundException;
import com.github.mangila.springbootrestfulservice.web.mapstruct.CustomerMapper;
import com.github.mangila.springbootrestfulservice.web.repository.v1.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        return this.mapper.toDto(this.repository.findAll());
    }

    public CustomerDto findById(final String id) throws ResourceNotFoundException {
        final CustomerDocument c = this.repository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException(id);
        });
        return this.mapper.toDto(c);
    }

    public String insert(final CustomerDto customerDto) {
        final CustomerDocument c = this.mapper.toDocument(customerDto);
        c.setRegistration(LocalDate.now());
        c.setOrderHistory(new ArrayList<>());
        return this.repository.insert(c).getId();
    }

    public void deleteById(String id) throws ResourceNotFoundException {
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    public String update(String id, CustomerDto customerDto) {
        if (this.repository.existsById(id)) {
            customerDto.setId(id);
            return this.repository.save(this.mapper.toDocument(customerDto)).getId();
        } else {
            return this.insert(customerDto);
        }
    }
}
