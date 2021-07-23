package com.github.mangila.springbootrestfulservice.web.model.v1.dto;

import com.github.mangila.springbootrestfulservice.domain.Address;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDto {

    @Null
    private String id;
    @NotNull
    private List<String> products;
    @NotNull
    @Positive
    private Integer amount;
    @NotNull
    private Address address;
}
