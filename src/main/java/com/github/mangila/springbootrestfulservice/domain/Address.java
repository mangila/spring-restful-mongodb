package com.github.mangila.springbootrestfulservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class Address implements Serializable {
    @NotNull
    private String street;
    @NotNull
    private String city;
}
