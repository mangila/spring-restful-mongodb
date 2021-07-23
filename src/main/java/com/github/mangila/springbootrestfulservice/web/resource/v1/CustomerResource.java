package com.github.mangila.springbootrestfulservice.web.resource.v1;

import com.github.mangila.springbootrestfulservice.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.web.service.v1.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<List<CustomerDocument>> findAll() {
        return ResponseEntity.ok(this.service.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomerDocument> findById(@PathVariable String id) {
        var c = this.service.findById(id);
        if (c.isPresent()) {
            return ResponseEntity.ok(c.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("'%s' not found", id));
        }
    }

    @GetMapping("find")
    public ResponseEntity<CustomerDocument> findByName(@RequestParam(name = "name") String name) {
        var c = this.service.findByName(name);
        if (c != null) {
            return ResponseEntity.ok(c);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("'%s' not found", name));
        }
    }

}
