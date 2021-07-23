package com.github.mangila.springbootrestfulservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("order")
@Data
@NoArgsConstructor
public class OrderDocument {

    @Id
    private String id;
    private List<String> products;
    private Integer amount;
    private Address address;
}
