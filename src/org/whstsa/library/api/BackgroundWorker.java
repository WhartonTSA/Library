package org.whstsa.library.api;

import javafx.beans.property.ReadOnlyListProperty;
import org.whstsa.library.util.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BackgroundWorker extends Thread {

    private static final BackgroundWorker singleton;

    private List<TickedOperation> tickedOperationList = new ArrayList<>();
    private Logger logger = new Logger("BackgroundWorker");

    static {
        singleton = new BackgroundWorker();
    }

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
        try {
            this.tickedOperationList.forEach(TickedOperation::tick);
        } catch (Exception ex) {
            this.logger.error("Error occurred during tick!");
            ex.printStackTrace();
        }
    }

    public List<TickedOperation> getTickOperations() {
        return Collections.unmodifiableList(this.tickedOperationList);
    }

    public void registerOperation(TickedOperation tickedOperation) {
        this.tickedOperationList.add(tickedOperation);
    }

    public interface TickedOperation {
        void tick();
    }
}
