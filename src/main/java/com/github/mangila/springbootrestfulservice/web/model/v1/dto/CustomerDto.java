package com.github.mangila.springbootrestfulservice.web.model.v1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomerDto {

    @Null
    private String id;
    @NotBlank
    private String name;
    @PastOrPresent
    private LocalDate registration;
    private List<String> orderHistory;
}
