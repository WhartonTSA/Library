package org.whstsa.library.api;

import javafx.application.Platform;
import org.whstsa.library.util.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BackgroundWorker extends Thread {

    private static final BackgroundWorker singleton;

    static {
        singleton = new BackgroundWorker();
    }

    private List<Runnable> tickedOperationList = new ArrayList<>();
    private Logger logger = new Logger("BackgroundWorker");

    private BackgroundWorker() {
        this.logger.log("Background worker has loaded.");
    }

    public static BackgroundWorker getBackgroundWorker() {
        return singleton;
    }

    @Override
    public void run() {
        this.logger.log("Background worker has started.");
        while (true) {
            this.tick();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                this.logger.warn("Background worker has been stopped abruptly.");
            }
        }
    }

    public void tick() {
        this.tickedOperationList.forEach(Platform::runLater);
    }

    public List<Runnable> getTickOperations() {
        return Collections.unmodifiableList(this.tickedOperationList);
    }

    public void registerOperation(Runnable operation) {
        this.tickedOperationList.add(operation);
    }
}
