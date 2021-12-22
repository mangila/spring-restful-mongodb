package com.github.mangila.springbootrestfulservice.service.mapstruct;

import com.github.mangila.springbootrestfulservice.persistence.domain.OrderDocument;
import com.github.mangila.springbootrestfulservice.web.dto.OrderDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    OrderDto toDto(OrderDocument orderDocument);

    List<OrderDto> toDto(List<OrderDocument> list);

    OrderDocument toDocument(OrderDto orderDto);

    List<OrderDocument> toDocument(List<OrderDto> list);
}
