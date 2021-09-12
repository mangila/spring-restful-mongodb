package com.github.mangila.springbootrestfulservice.web.resource.v1;

import com.github.mangila.springbootrestfulservice.web.dto.v1.OrderDto;
import com.github.mangila.springbootrestfulservice.web.service.v1.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("v1/order")
@RestController
@Tag(name = "Order", description = "endpoints for order operations.")
public class OrderResource {

    private final OrderService service;

    public OrderResource(OrderService service) {
        this.service = service;
    }

    @Operation(description = "Find all orders.")
    @ApiResponse(responseCode = "200",
            description = "Returns the order json array.")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDto>> findAll() {
        return ResponseEntity.ok(this.service.findAll());
    }

    @Operation(description = "Find a order by its id.")
    @ApiResponse(responseCode = "200",
            description = "Returns the order json object.")
    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDto> findById(@PathVariable String id) {
        final OrderDto o = this.service.findById(id);
        return ResponseEntity.ok(o);
    }

    @Operation(description = "Create a new order.")
    @ApiResponse(responseCode = "201",
            headers = @Header(name = HttpHeaders.LOCATION, description = "contains a URI to new the resource."),
            description = "Creates a new order and map it with the customers order history.")
    @PostMapping(value = "{customerId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@PathVariable String customerId,
                                    @Valid @RequestBody OrderDto orderDto,
                                    HttpServletRequest request) {
        final String orderId = this.service.insert(customerId, orderDto);
        final URI location = URI.create(request.getRequestURI()).resolve(orderId);
        return ResponseEntity.created(location).build();
    }

    @Operation(description = "Delete a order by its id.")
    @ApiResponse(responseCode = "204",
            description = "Order is deleted.")
    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        this.service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
