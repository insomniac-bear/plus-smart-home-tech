package ru.yandex.practicum.grpc;

import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.model.Action;

import java.time.Instant;

@Slf4j
@Service
public class GrpcClient {

    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public GrpcClient(@net.devh.boot.grpc.client.inject.GrpcClient("hub-router")
                      HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient) {
        this.hubRouterClient = hubRouterClient;
    }

    public void sendToHubRouter(String sensorId, Action action, String hubId, String scenarioName, Instant snapshotTimestamp) {
        try {
            DeviceActionProto actionProto = DeviceActionProto.newBuilder()
                    .setSensorId(sensorId)
                    .setType(ActionTypeProto.valueOf(action.getType().name()))
                    .setValue(action.getValue() != null ? action.getValue() : 0)
                    .build();
            Timestamp timestamp = Timestamp.newBuilder()
                    .setSeconds(snapshotTimestamp.getEpochSecond())
                    .setNanos(snapshotTimestamp.getNano())
                    .build();
            DeviceActionRequest request = DeviceActionRequest.newBuilder()
                    .setHubId(hubId)
                    .setScenarioName(scenarioName)
                    .setAction(actionProto)
                    .setTimestamp(timestamp)
                    .build();

            Empty res = hubRouterClient.handleDeviceAction(request);
            log.info("Действие в хаб с id {} по сценарию {} для сенсора с id {} типа {} со значением {} отправлено",
                    hubId, scenarioName, sensorId, action.getType(), action.getValue());
        } catch (Exception e) {
            log.error("Ошибка при отправки действий для сенсора с id {} в kafka", sensorId);
        }
    }
}
