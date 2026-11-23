package com.tenthouse.repository;
import com.tenthouse.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCustomerIdOrderByEventDateDesc(Long customerId);
}