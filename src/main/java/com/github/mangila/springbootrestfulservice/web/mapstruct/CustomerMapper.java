package com.github.mangila.springbootrestfulservice.web.mapstruct;

import com.github.mangila.springbootrestfulservice.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.web.model.v1.dto.CustomerDto;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    CustomerDto toDto();

    CustomerDocument toDocument();
}
