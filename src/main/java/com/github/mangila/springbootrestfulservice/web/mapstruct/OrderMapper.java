package com.github.mangila.springbootrestfulservice.web.mapstruct;

import com.github.mangila.springbootrestfulservice.domain.OrderDocument;
import com.github.mangila.springbootrestfulservice.web.model.v1.dto.OrderDto;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {
    OrderDto toDto(OrderDocument orderDocument);

    OrderDocument toDocument(OrderDto orderDto);
}
