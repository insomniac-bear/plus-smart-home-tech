package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.model.Scenario;

import java.util.List;
import java.util.Optional;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

    List<Scenario> findAllByHubId(String hubId);

    Optional<Scenario> findByHubIdAndName(String hubId, String name);

    void deleteByName(String name);

    Boolean existsByName(String name);

    @Query("SELECT COUNT(c) FROM Scenario as s JOIN s.conditions as c WHERE KEY(c) = :sensorId")
    long getCountConditionsBySensorId(@Param("sensorId") String sensorId);

    @Query("SELECT COUNT(a) FROM Scenario as s JOIN s.actions as a WHERE KEY(a) = :sensorId")
    long getCountActionsBySensorId(@Param("sensorId") String sensorId);
}
