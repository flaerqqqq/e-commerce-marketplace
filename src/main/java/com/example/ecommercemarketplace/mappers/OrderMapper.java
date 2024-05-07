package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.OrderResponseDto;
import com.example.ecommercemarketplace.models.MerchantOrder;
import com.example.ecommercemarketplace.models.Order;
import com.example.ecommercemarketplace.models.OrderItem;
import com.example.ecommercemarketplace.models.enums.MerchantOrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "deliveryData.method", target = "deliveryMethod")
    @Mapping(source = "merchantOrders", target = "totalQuantity", qualifiedByName = "calculateTotalQuantity")
    @Mapping(source = "merchantOrders", target = "statusesOfOrders", qualifiedByName = "getStatuses")
    OrderResponseDto toResponseDto(Order order);

    @Named("calculateTotalQuantity")
    default int calculateTotalQuantity(List<MerchantOrder> merchantOrders){
        return merchantOrders.stream()
                .flatMap(merchantOrder -> merchantOrder.getOrderItems().stream()
                        .map(OrderItem::getQuantity))
                .reduce(0, Integer::sum);
    }

    @Named("getStatuses")
    default Map<Long, MerchantOrderStatus> map(List<MerchantOrder> merchantOrders) {
        return merchantOrders.stream()
                .collect(Collectors.toMap(MerchantOrder::getId, MerchantOrder::getStatus));
    }
}
