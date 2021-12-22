package com.github.mangila.springbootrestfulservice.web.resource;

import com.github.mangila.springbootrestfulservice.web.dto.CustomerDto;
import com.github.mangila.springbootrestfulservice.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
@Tag(name = "Customer", description = "endpoints for customer operations.")
public class CustomerResource {

    private final CustomerService service;

    @Autowired
    public CustomerResource(CustomerService service) {
        this.service = service;
    }

    @Operation(description = "Find all customers.")
    @ApiResponse(responseCode = "200",
            description = "Returns the customer json array")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerDto>> findAll() {
        return ResponseEntity.ok(this.service.findAll());
    }

    @Operation(description = "Find a customer by its id.")
    @ApiResponse(responseCode = "200",
            description = "Returns the customer json object.")
    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> findById(@PathVariable String id) {
        final CustomerDto c = this.service.findById(id);
        return ResponseEntity.ok(c);
    }

    @Operation(description = "Creates a new customer.")
    @ApiResponse(responseCode = "201",
            headers = @Header(name = HttpHeaders.LOCATION, description = "contains a URI to the new resource."),
            description = "New customer created")
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody CustomerDto customerDto,
                                    HttpServletRequest request) {
        final String id = this.service.insert(customerDto);
        final URI location = URI.create(request.getRequestURI() + "/" + id);
        return ResponseEntity.created(location).build();
    }

    @Operation(description = "Update an already existing customer or creates a new one.")
    @ApiResponse(responseCode = "204",
            headers = @Header(name = HttpHeaders.CONTENT_LOCATION, description = "contains a URI to the resource."),
            description = "Updated a customer or creates a new one")
    @PutMapping(value = "{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable String id,
                                    @Valid @RequestBody CustomerDto customerDto,
                                    HttpServletRequest request) {
        this.service.update(id, customerDto);
        final URI location = URI.create(request.getRequestURI());
        return ResponseEntity.noContent().header(CONTENT_LOCATION, location.toString()).build();
    }

    @Operation(description = "Deletes a customer by its id.")
    @ApiResponse(responseCode = "204",
            description = "Customer is deleted.")
    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        this.service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
