package com.assignement.test.orderprocessing.dto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {
    @NotNull
    private Long customerId;


    @NotEmpty
    private List<OrderItemDto> items;
}
