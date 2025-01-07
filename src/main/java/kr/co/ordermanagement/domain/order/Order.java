package kr.co.ordermanagement.domain.order;

import kr.co.ordermanagement.domain.exception.NotCancellableStateException;
import kr.co.ordermanagement.domain.product.Product;

import java.util.List;

import static kr.co.ordermanagement.domain.order.State.CANCELED;
import static kr.co.ordermanagement.domain.order.State.CREATED;

public class Order {
    private Long id;
    private List<Product> orderedProducts;
    private Integer totalPrice;
    private State state;

    public Order(List<Product> orderedProducts) {
        this.orderedProducts = orderedProducts;
        this.totalPrice = calculateTotalPrice(orderedProducts);
        this.state = CREATED;
    }

    public Long getId() {
        return id;
    }

    public List<Product> getOrderedProducts() {
        return orderedProducts;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public State getState() {
        return state;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Integer calculateTotalPrice(List<Product> orderedProducts) {
        return orderedProducts.stream()
                .mapToInt(orderedProduct -> orderedProduct.getPrice() * orderedProduct.getAmount())
                .sum();
    }

    public void cancel() {
        this.state.checkCancellable();
        this.state = CANCELED;
    }

    public Boolean sameId(Long id) {
        return this.id.equals(id);
    }
}
