package com.github.mangila.springbootrestfulservice.web.service.v1;


import com.github.mangila.springbootrestfulservice.domain.OrderDocument;
import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import com.github.mangila.springbootrestfulservice.web.dto.v1.OrderDto;
import com.github.mangila.springbootrestfulservice.web.exception.ResourceNotFoundException;
import com.github.mangila.springbootrestfulservice.web.mapstruct.OrderMapper;
import com.github.mangila.springbootrestfulservice.web.repository.v1.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public OrderDto findById(String id) {
        final OrderDocument c = this.repository.findById(id).orElseThrow(ResourceNotFoundException::new);
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

    public void deleteById(String id) throws ResourceNotFoundException {
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException();
        }
    }
}
