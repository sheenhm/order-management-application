package kr.co.ordermanagement.presentation.controller;

import kr.co.ordermanagement.application.SimpleOrderService;
import kr.co.ordermanagement.application.SimpleProductService;
import kr.co.ordermanagement.domain.order.Order;
import kr.co.ordermanagement.domain.order.State;
import kr.co.ordermanagement.domain.product.Product;
import kr.co.ordermanagement.presentation.dto.ChangeStateRequestDto;
import kr.co.ordermanagement.presentation.dto.OrderRequestDto;
import kr.co.ordermanagement.presentation.dto.OrderResponseDto;
import kr.co.ordermanagement.presentation.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductRestController {

    private SimpleProductService simpleProductService;
    private SimpleOrderService simpleOrderService;

    @Autowired
    ProductRestController(SimpleProductService simpleProductService, SimpleOrderService simpleOrderService) {
        this.simpleProductService = simpleProductService;
        this.simpleOrderService = simpleOrderService;
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public List<ProductDto> findProducts() {
        return simpleProductService.findAll();
    }

    // 1. 상품 주문 API
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity<OrderResponseDto> order(@RequestBody List<OrderRequestDto> orderRequestDto) {
        OrderResponseDto orderResponseDto = simpleOrderService.order(orderRequestDto);
        return ResponseEntity.ok(orderResponseDto);
    }

    // 2. 주문상태 강제 변경 API
    @RequestMapping(value = "/orders/{orderId}", method = RequestMethod.PATCH)
    public ResponseEntity<OrderResponseDto> changeOrderState(@PathVariable Long orderId,
                                                             @RequestBody ChangeStateRequestDto changeStateRequestDto) {
        OrderResponseDto orderResponseDto = simpleOrderService.changeOrderState(orderId, changeStateRequestDto);
        return ResponseEntity.ok(orderResponseDto);
    }

    // 3. 주문번호로 조회 API
    @RequestMapping(value = "/orders/{orderId}", method = RequestMethod.GET)
    public ResponseEntity<OrderResponseDto> findOrderByOrderId(@PathVariable Long orderId) {
        OrderResponseDto orderResponseDto = simpleOrderService.findOrderByOrderId(orderId);
        return ResponseEntity.ok(orderResponseDto);
    }

    // 4. 주문상태로 조회 API
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity<List<OrderResponseDto>> findOrderByState(@RequestParam State state) {
        List<OrderResponseDto> orderResponseDtos = simpleOrderService.findOrderByState(state);
        return ResponseEntity.ok(orderResponseDtos);
    }

    // 5. 주문 취소 API
    @RequestMapping(value = "/orders/{orderId}/cancel", method = RequestMethod.PATCH)
    public ResponseEntity<OrderResponseDto> cancelOrder(@PathVariable Long orderId) {
        OrderResponseDto orderResponseDto = simpleOrderService.cancelOrder(orderId);
        return ResponseEntity.ok(orderResponseDto);
    }


}
