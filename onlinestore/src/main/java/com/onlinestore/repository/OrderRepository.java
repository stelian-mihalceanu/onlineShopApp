package com.onlinestore.repository;

import com.onlinestore.model.Order;
import com.onlinestore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select distinct o from Order o left join fetch o.items i left join fetch i.product where o.user = :user order by o.createdAt desc")
    List<Order> findByUserWithItems(@Param("user") User user);
}
