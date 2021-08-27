package com.github.mangila.springbootrestfulservice.web.dto.v1;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomerDto {

    @Null
    private String id;
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;
    @PastOrPresent
    @Null
    private LocalDate registration;
    @Null
    private List<String> orderHistory;
}
