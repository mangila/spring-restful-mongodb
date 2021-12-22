package com.github.mangila.springbootrestfulservice.persistence.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document("customer")
@Data
@NoArgsConstructor
public class CustomerDocument {

    @Id
    private String id;
    private String name;
    private LocalDate registration;
    private List<String> orderHistory;
}
