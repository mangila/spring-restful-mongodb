package com.github.mangila.springbootrestfulservice.web.resource.v1;

import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import com.github.mangila.springbootrestfulservice.web.service.v1.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("v1/customer")
@RestController
public class CustomerResource {

    private final CustomerService service;

    @Autowired
    public CustomerResource(CustomerService service) {
        this.service = service;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerDto>> findAll() {
        return ResponseEntity.ok(this.service.findAll());
    }

    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> findById(@PathVariable String id) {
        final CustomerDto c = this.service.findById(id);
        return ResponseEntity.ok(c);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody CustomerDto customerDto,
                                    HttpServletRequest request) {
        final String id = this.service.insert(customerDto);
        final URI location = URI.create(request.getRequestURI() + "/" + id);
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable String id,
                                    @Valid @RequestBody CustomerDto customerDto,
                                    HttpServletRequest request) {
        this.service.update(id, customerDto);
        final URI location = URI.create(request.getRequestURI());
        return ResponseEntity.noContent().header(CONTENT_LOCATION, location.toString()).build();
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        this.service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
