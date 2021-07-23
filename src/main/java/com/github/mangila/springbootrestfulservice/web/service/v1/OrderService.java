package com.github.mangila.springbootrestfulservice.web.service.v1;


import com.github.mangila.springbootrestfulservice.web.mapstruct.OrderMapper;
import com.github.mangila.springbootrestfulservice.web.model.v1.dto.OrderDto;
import com.github.mangila.springbootrestfulservice.web.repository.v1.OrderRepository;
import lombok.val;
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
        val c = this.repository.findById(id).orElseThrow(RuntimeException::new);
        return this.mapper.toDto(c);
    }

    public String insertNewOrder(String customerId, OrderDto orderDto) {
        val o = this.mapper.toDocument(orderDto);
        val orderId = this.repository.insert(o).getId();
        val c = this.customerService.findById(customerId);
        c.getOrderHistory().add(orderId);
        this.customerService.updateCustomer(c);
        return orderId;
    }

    public boolean existsById(String id) {
        return this.repository.existsById(id);
    }

    public void deleteById(String id) {
        this.repository.deleteById(id);
    }
}
