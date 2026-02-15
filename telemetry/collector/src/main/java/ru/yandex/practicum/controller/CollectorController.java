package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.HubEvent;
import ru.yandex.practicum.dto.SensorEvent;
import ru.yandex.practicum.mapper.HubEventMapper;
import ru.yandex.practicum.mapper.SensorEventMapper;
import ru.yandex.practicum.service.ProducerService;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class CollectorController {
    private final HubEventMapper hubEventMapper;
    private final SensorEventMapper sensorEventMapper;
    private final ProducerService service;

    @PostMapping("/sensors")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> collectSensor(@Valid @RequestBody SensorEvent event) {
        log.info("POST /events/sensors event: {}", event);
        service.sendSensorEvent(sensorEventMapper.toAvro(event));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/hubs")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> collectHub(@Valid @RequestBody HubEvent event) {
        log.info("POST /events/hubs event: {}", event);
        service.sendHubEvent(hubEventMapper.toAvro(event));
        return ResponseEntity.ok().build();
    }
}
