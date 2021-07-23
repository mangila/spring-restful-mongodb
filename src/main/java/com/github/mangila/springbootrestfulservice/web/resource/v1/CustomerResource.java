package com.github.mangila.springbootrestfulservice.web.resource.v1;

import com.github.mangila.springbootrestfulservice.web.model.v1.dto.CustomerDto;
import com.github.mangila.springbootrestfulservice.web.service.v1.CustomerService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Validated
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
        if (this.service.existsById(id)) {
            return ResponseEntity.ok(this.service.findById(id));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("'%s' not found", id));
        }
    }

    @PostMapping
    public ResponseEntity<?> insertNewCustomer(@Valid @RequestBody CustomerDto customerDto,
                                               HttpServletRequest request) {
        val id = this.service.insertNewCustomer(customerDto);
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, request.getRequestURL().append("/").append(id).toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable String id,
                                            @Valid @RequestBody CustomerDto customerDto,
                                            HttpServletRequest request) {
        var headers = new HttpHeaders();
        if (this.service.existsById(id)) {
            this.service.updateCustomer(customerDto);
            headers.add(HttpHeaders.LOCATION, request.getRequestURL().append("/").append(id).toString());
            return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
        } else {
            val newId = this.service.insertNewCustomer(customerDto);
            headers.add(HttpHeaders.LOCATION, request.getRequestURL().append("/").append(newId).toString());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        if (this.service.existsById(id)) {
            this.service.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("'%s' not found", id));
        }
    }

}
