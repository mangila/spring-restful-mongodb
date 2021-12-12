package com.github.mangila.springbootrestfulservice.mapstruct;

import com.github.mangila.springbootrestfulservice.domain.v1.CustomerDocument;
import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CustomerMapper {
    CustomerDto toDto(CustomerDocument customerDocument);

    List<CustomerDto> toDto(List<CustomerDocument> list);

    CustomerDocument toDocument(CustomerDto customerDto);

    List<CustomerDocument> toDocument(List<CustomerDto> list);
}