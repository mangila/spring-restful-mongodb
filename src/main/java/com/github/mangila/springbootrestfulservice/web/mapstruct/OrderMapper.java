package com.github.mangila.springbootrestfulservice.web.mapstruct;

import com.github.mangila.springbootrestfulservice.domain.OrderDocument;
import com.github.mangila.springbootrestfulservice.web.dto.v1.OrderDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    OrderDto toDto(OrderDocument orderDocument);

    List<OrderDto> toDto(List<OrderDocument> list);

    OrderDocument toDocument(OrderDto orderDto);

    List<OrderDocument> toDocument(List<OrderDto> list);
}
