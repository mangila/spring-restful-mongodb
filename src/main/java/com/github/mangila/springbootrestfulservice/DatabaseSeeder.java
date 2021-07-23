package com.github.mangila.springbootrestfulservice;

import com.github.mangila.springbootrestfulservice.domain.Address;
import com.github.mangila.springbootrestfulservice.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.domain.OrderDocument;
import com.github.mangila.springbootrestfulservice.web.repository.v1.CustomerRepository;
import com.github.mangila.springbootrestfulservice.web.repository.v1.OrderRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

@Component
/**
 * DEVELOPMENT PURPOSE CLASS
 */
public class DatabaseSeeder implements InitializingBean {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public DatabaseSeeder(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void afterPropertiesSet() {
        Stream.of("Tor", "Freja", "Loke", "Oden")
                .forEach(name -> {
                    var c = new CustomerDocument();
                    c.setName(name);
                    c.setRegistration(LocalDate.of(
                            ThreadLocalRandom.current().nextInt(2010, 2020),
                            ThreadLocalRandom.current().nextInt(1, 12),
                            ThreadLocalRandom.current().nextInt(1, 28)
                    ));
                    c.setOrderHistory(new ArrayList<>());
                    customerRepository.insert(c);
                    for (int i = 0; i < ThreadLocalRandom.current().nextInt(5, 10); i++) {
                        var o = new OrderDocument();
                        o.setAmount(ThreadLocalRandom.current().nextInt(200, 800));
                        o.setProducts(this.getRandomProducts());
                        o.setAddress(new Address(
                                "Asgard road " + ThreadLocalRandom.current().nextInt(1, 100),
                                "Asgard"));
                        var orderId = orderRepository.insert(o).getId();
                        c = customerRepository.findByName(name);
                        c.getOrderHistory().add(orderId);
                        customerRepository.save(c);
                    }
                });
    }

    public List<String> getRandomProducts() {
        var products = List.of("T-Shirt", "Jeans", "Sweatpants",
                "Trousers", "Strawberries", "Meatballs",
                "Milk", "Watermelon", "Salomon", "Red Meat");
        var l = new ArrayList<String>();
        for (int i = 0; i <= 5; i++) {
            l.add(products.get(ThreadLocalRandom.current().nextInt(0, 9)));
        }
        return l;
    }
}
