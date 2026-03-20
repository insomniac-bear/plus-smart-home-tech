package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.entity.Item;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {
}
