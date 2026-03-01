package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyzerRunner implements CommandLineRunner {
    final HubEventStarter hubEventStarter;
    final SnapshotStarter snapshotStarter;

    @Override
    public void run(String ...args) throws Exception {
        Thread hubEventsThread = new Thread(hubEventStarter);
        hubEventsThread.setName("HubEventHandlerThread");
        hubEventsThread.start();

        snapshotStarter.start();
    }
}
