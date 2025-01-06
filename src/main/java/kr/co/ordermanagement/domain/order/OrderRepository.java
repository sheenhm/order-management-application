package kr.co.ordermanagement.domain.order;

import java.util.List;

public interface OrderRepository {
    Order findById(Long id);
    List<Order> findByState(State state);
    Order add(Order order);
    void update(Order order);
}