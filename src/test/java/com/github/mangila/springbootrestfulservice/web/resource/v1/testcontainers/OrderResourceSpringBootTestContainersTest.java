package com.github.mangila.springbootrestfulservice.web.resource.v1.testcontainers;

import com.github.mangila.springbootrestfulservice.domain.v1.Address;
import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import com.github.mangila.springbootrestfulservice.web.dto.v1.OrderDto;
import com.github.mangila.springbootrestfulservice.service.CustomerService;
import com.github.mangila.springbootrestfulservice.service.OrderService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("testcontainers")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class OrderResourceSpringBootTestContainersTest extends SeededMongoDBTestContainer {

    private final String orderId = "2cdf9886-f565-4d5f-8bbc-2561806b2052";

    private final String customerId = "c8127464-3559-45ca-a70e-51f9c3a6d1c0";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private OrderService orderService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private CustomerService customerService;

    @Order(100)
    @Test
    void findAll() {
        Assertions.assertEquals(11, this.orderService.findAll().size());
        Assertions.assertFalse(this.orderService.findAll().isEmpty());
    }

    @Order(200)
    @Test
    void findById() {
        OrderDto o = this.orderService.findById(this.orderId);
        Assertions.assertEquals(750, o.getAmount());
        Assertions.assertEquals("Minas Tirith road 101", o.getAddress().getStreet());
        Assertions.assertEquals("Gondor", o.getAddress().getCity());
    }

    @Order(300)
    @Test
    void insert() {
        OrderDto o = new OrderDto();
        Address a = new Address();
        a.setCity("Dwarf Street");
        a.setCity("Moria");
        o.setAddress(a);
        o.setAmount(300);
        o.setProducts(Lists.newArrayList("Axe", "Mithril"));
        this.orderService.insert(this.customerId, o);
        Assertions.assertEquals(12, this.orderService.findAll().size());
        Assertions.assertFalse(this.orderService.findAll().isEmpty());
        CustomerDto c = this.customerService.findById(this.customerId);
        Assertions.assertEquals(4, c.getOrderHistory().size());
    }

    @Order(400)
    @Test
    void deleteById() {
        this.orderService.deleteById(this.orderId);
        Assertions.assertEquals(11, this.orderService.findAll().size());
    }


}
