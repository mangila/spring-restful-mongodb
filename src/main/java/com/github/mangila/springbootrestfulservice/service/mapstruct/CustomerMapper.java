package com.github.mangila.springbootrestfulservice.service.mapstruct;

import com.github.mangila.springbootrestfulservice.persistence.domain.CustomerDocument;
import com.github.mangila.springbootrestfulservice.web.dto.CustomerDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CustomerMapper {
    CustomerDto toDto(CustomerDocument customerDocument);

    List<CustomerDto> toDto(List<CustomerDocument> list);

    CustomerDocument toDocument(CustomerDto customerDto);

    List<CustomerDocument> toDocument(List<CustomerDto> list);
}
