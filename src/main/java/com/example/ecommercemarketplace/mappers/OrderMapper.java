package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.OrderResponseDto;
import com.example.ecommercemarketplace.models.MerchantOrder;
import com.example.ecommercemarketplace.models.Order;
import com.example.ecommercemarketplace.models.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {


    @Mapping(source = "deliveryData.method", target = "deliveryMethod")
    @Mapping(source = "merchantOrders", target = "totalQuantity", qualifiedByName = "calculateTotalQuantity")
    OrderResponseDto toResponseDto(Order order);

    @Named("calculateTotalQuantity")
    default int calculateTotalQuantity(List<MerchantOrder> merchantOrders){
        return merchantOrders.stream()
                .flatMap(merchantOrder -> merchantOrder.getOrderItems().stream()
                        .map(OrderItem::getQuantity))
                .reduce(0, Integer::sum);
    }
}
