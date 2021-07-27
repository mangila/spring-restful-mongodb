package com.github.mangila.springbootrestfulservice.web.resource.v1;

import com.github.mangila.springbootrestfulservice.web.exception.ResourceNotFoundException;
import com.github.mangila.springbootrestfulservice.web.model.v1.dto.OrderDto;
import com.github.mangila.springbootrestfulservice.web.service.v1.OrderService;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RequestMapping("v1/order")
@RestController
public class OrderResource {

    private final OrderService service;

    public OrderResource(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> findAll() {
        return ResponseEntity.ok(this.service.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable String id) {
        try {
            val o = this.service.findById(id);
            return ResponseEntity.ok(o);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("'%s' not found", id));
        }
    }

    @PostMapping("{customerId}")
    public ResponseEntity<?> insertNewOrder(@PathVariable String customerId,
                                            @Valid @RequestBody OrderDto orderDto,
                                            HttpServletRequest request) {
        try {
            val orderId = this.service.insert(customerId, orderDto);
            val headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, request.getRequestURL().append("/").append(orderId).toString());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("'%s' not found", customerId));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        try {
            this.service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("'%s' not found", id));
        }
    }
}
