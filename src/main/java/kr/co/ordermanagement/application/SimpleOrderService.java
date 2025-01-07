package kr.co.ordermanagement.application;

import kr.co.ordermanagement.domain.order.Order;
import kr.co.ordermanagement.domain.order.OrderRepository;
import kr.co.ordermanagement.domain.order.OrderedProduct;
import kr.co.ordermanagement.domain.order.State;
import kr.co.ordermanagement.domain.product.Product;
import kr.co.ordermanagement.domain.product.ProductRepository;
import kr.co.ordermanagement.presentation.dto.ChangeStateRequestDto;
import kr.co.ordermanagement.presentation.dto.OrderRequestDto;
import kr.co.ordermanagement.presentation.dto.OrderResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SimpleOrderService {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    @Autowired
    public SimpleOrderService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public OrderResponseDto order(List<OrderRequestDto> orderRequestDto) {
        List<OrderedProduct> orderedProducts = makeOrderedProducts(orderRequestDto);
        decreaseProductAmount(orderedProducts);

        Order order = new Order(orderedProducts);
        orderRepository.add(order);

        return OrderResponseDto.toDto(order);
    }

    public OrderResponseDto changeOrderState(Long orderId, ChangeStateRequestDto changeStateRequestDto) {
        Order order = orderRepository.findById(orderId);
        State state = changeStateRequestDto.getState();

        order.setState(state);
        orderRepository.update(order);

        return OrderResponseDto.toDto(order);
    }

    public OrderResponseDto findOrderByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId);
        return OrderResponseDto.toDto(order);
    }

    public List<OrderResponseDto> findOrderByState(State state) {
        List<Order> orders = orderRepository.findByState(state);
        List<OrderResponseDto> orderResponseDtos = orders.stream()
                .map(order -> OrderResponseDto.toDto(order))
                .toList();

        return orderResponseDtos;
    }

    public OrderResponseDto cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId);
        order.cancel();
        orderRepository.update(order);

        return OrderResponseDto.toDto(order);
    }

    private List<OrderedProduct> makeOrderedProducts(List<OrderRequestDto> orderRequestDtos) {
        return orderRequestDtos.stream()
                .map(orderRequestDto -> {
                    Long productId = orderRequestDto.getId();
                    Product product = productRepository.findById(productId);

                    Integer orderedAmount = orderRequestDto.getAmount();
                    product.checkEnoughAmount(orderedAmount);

                    return new OrderedProduct(
                            productId,
                            product.getName(),
                            product.getPrice(),
                            orderRequestDto.getAmount()
                    );
                }).toList();
    }

    private void decreaseProductAmount(List<OrderedProduct> orderedProducts) {
        orderedProducts.stream()
                .forEach(orderedProduct -> {
                    Long productId = orderedProduct.getId();
                    Product product = productRepository.findById(productId);

                    Integer orderedAmount = orderedProduct.getAmount();
                    product.decreaseAmount(orderedAmount);

                    productRepository.update(product);
                });
    }
}
