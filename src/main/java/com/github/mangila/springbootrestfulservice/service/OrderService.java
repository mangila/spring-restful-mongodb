package com.github.mangila.springbootrestfulservice.service;


import com.github.mangila.springbootrestfulservice.persistence.domain.OrderDocument;
import com.github.mangila.springbootrestfulservice.web.dto.CustomerDto;
import com.github.mangila.springbootrestfulservice.web.dto.OrderDto;
import com.github.mangila.springbootrestfulservice.service.mapstruct.OrderMapper;
import com.github.mangila.springbootrestfulservice.persistence.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.MissingResourceException;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final CustomerService customerService;
    private final OrderMapper mapper;

    @Autowired
    public OrderService(OrderRepository repository, CustomerService customerService, OrderMapper mapper) {
        this.repository = repository;
        this.customerService = customerService;
        this.mapper = mapper;
    }

    public List<OrderDto> findAll() {
        return this.mapper.toDto(this.repository.findAll());
    }

    public OrderDto findById(String id) throws MissingResourceException {
        final OrderDocument c = this.repository.findById(id).orElseThrow(() -> {
            throw new MissingResourceException("Not Found", OrderDto.class.getName(), id);
        });
        return this.mapper.toDto(c);
    }

    public String insert(String customerId, OrderDto orderDto) {
        final OrderDocument o = this.mapper.toDocument(orderDto);
        final String orderId = this.repository.insert(o).getId();
        final CustomerDto c = this.customerService.findById(customerId);
        c.getOrderHistory().add(orderId);
        this.customerService.update(customerId, c);
        return orderId;
    }

    public void deleteById(String id) throws MissingResourceException {
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
        } else {
            throw new MissingResourceException("Not Found", OrderDto.class.getName(), id);
        }
    }
}
