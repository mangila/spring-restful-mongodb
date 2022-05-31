package com.github.mangila.springbootrestfulservice.service;


import com.github.mangila.springbootrestfulservice.common.ApplicationException;
import com.github.mangila.springbootrestfulservice.persistence.domain.OrderDocument;
import com.github.mangila.springbootrestfulservice.persistence.repository.OrderRepository;
import com.github.mangila.springbootrestfulservice.service.mapstruct.OrderMapper;
import com.github.mangila.springbootrestfulservice.web.dto.CustomerDto;
import com.github.mangila.springbootrestfulservice.web.dto.OrderDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final CustomerService customerService;
    private final OrderMapper mapper;

    public List<OrderDto> findAll() {
        return this.mapper.toDto(this.repository.findAll());
    }

    public OrderDto findById(String id) {
        final OrderDocument c = this.repository.findById(id).orElseThrow(() -> {
            throw new ApplicationException(String.format("ID: %s - Not Found", id));
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

    public void deleteById(String id) {
        this.repository.findById(id).ifPresentOrElse(repository::delete, () -> {
            throw new ApplicationException(String.format("ID: %s - Not Found", id));
        });
    }
}
