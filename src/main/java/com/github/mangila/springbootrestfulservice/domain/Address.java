package com.github.mangila.springbootrestfulservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @NotNull
    private String street;
    @NotNull
    private String city;
}
