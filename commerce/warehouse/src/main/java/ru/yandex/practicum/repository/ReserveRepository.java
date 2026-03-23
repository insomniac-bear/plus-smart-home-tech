package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.entity.Reserve;

import java.util.List;
import java.util.UUID;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    List<Reserve> findByCartId(UUID cartId);
}
