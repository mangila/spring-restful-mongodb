package com.github.mangila.springbootrestfulservice.web.resource.v1;

import com.github.mangila.springbootrestfulservice.web.exception.ResourceNotFoundException;
import com.github.mangila.springbootrestfulservice.web.model.v1.dto.CustomerDto;
import com.github.mangila.springbootrestfulservice.web.service.v1.CustomerService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RequestMapping("v1/customer")
@RestController
public class CustomerResource {

    private final CustomerService service;

    @Autowired
    public CustomerResource(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> findAll() {
        return ResponseEntity.ok(this.service.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomerDto> findById(@PathVariable String id) {
        try {
            var c = this.service.findById(id);
            return ResponseEntity.ok(c);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("'%s' not found", id));
        }
    }

    @PostMapping
    public ResponseEntity<?> insertNewCustomer(@Valid @RequestBody CustomerDto customerDto,
                                               HttpServletRequest request) {
        val id = this.service.insert(customerDto);
        val location = URI.create(request.getRequestURL().toString()).resolve(id);
        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable String id,
                                            @Valid @RequestBody CustomerDto customerDto,
                                            HttpServletRequest request) {
        val customerId = this.service.update(id, customerDto);
        val location = URI.create(request.getRequestURL().toString()).resolve(customerId);
        return ResponseEntity.created(location).build();
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
