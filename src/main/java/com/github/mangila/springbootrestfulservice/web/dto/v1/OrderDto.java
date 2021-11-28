package com.github.mangila.springbootrestfulservice.web.dto.v1;

import com.github.mangila.springbootrestfulservice.domain.Address;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDto implements Serializable {

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
