package org.whstsa.library.gui.api;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.whstsa.library.World;
import org.whstsa.library.api.BackgroundWorker;
import org.whstsa.library.gui.components.LabelElement;
import org.whstsa.library.gui.factories.GuiUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GuiStatusBar extends HBox {

    private static final String LIBRARY_SAVED = "The library is saved.";
    private static final String LIBRARY_UNSAVED = "The library is unsaved. Exiting will erase all progress since last save.";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    private LabelElement statusLabel;
    private LabelElement dateLabel;
    private boolean saved;
    private boolean statusOverride;//Used to override the normal status and replace with more urgent message
    private String currentStatus = "";

    public GuiStatusBar() {
        Image icon = new Image("file:LibraryManagerIcon.png");
        ImageView imageView = new ImageView(icon);
        imageView.setFitHeight(10);
        imageView.setPreserveRatio(true);

        this.statusOverride = false;
        this.saved = true;
        this.statusLabel = GuiUtils.createLabel("");
        this.statusLabel.setFont(Font.font(12));
        this.statusLabel.setTextFill(Color.web("#3d3d3d"));

        HBox mainSpacer = GuiUtils.createSpacer();

        this.dateLabel = GuiUtils.createLabel("");
        this.dateLabel.setFont(Font.font(12));


        this.getChildren().addAll(imageView, this.statusLabel, mainSpacer, this.dateLabel);
        this.setHeight(8);
        this.setSpacing(4);
        this.setPadding(new Insets(1, 1, 1, 1));
        this.setAlignment(Pos.CENTER_LEFT);
        this.setBackground(new Background(new BackgroundFill(Color.web("#f2f2f2"), null, null)));

        liveSavedStatus();
        liveDate();
    }

    private void liveDate() {
        BackgroundWorker.getBackgroundWorker().registerOperation(() -> {
            String dateString = SIMPLE_DATE_FORMAT.format(World.getDate());
            this.dateLabel.setText(dateString);

            if (!SIMPLE_DATE_FORMAT.format(World.getDate())
                    .equals(SIMPLE_DATE_FORMAT.format(new Date()))) {//Check if today's date matches simulated date
                this.dateLabel.setTextFill(Color.web("#0056ad"));
                this.dateLabel.setTooltip(new Tooltip("This is the date simulated by the Library Manager, not today's date."));

                this.statusOverride = true;
                this.statusLabel.setTextFill(Color.web("#0056ad"));
                this.statusLabel.setText("The data you are seeing has been simulated. You may not want to save this data.");
            } else {
                this.dateLabel.setTextFill(Color.web("#3d3d3d"));
                this.dateLabel.setTooltip(null);
                this.statusOverride = false;
                this.statusLabel.setTextFill(Color.web("#3d3d3d"));
            }
        });
    }

    private void liveSavedStatus() {
        BackgroundWorker.getBackgroundWorker().registerOperation(() -> {
            if (this.statusOverride) {
                return;
            }
            String newValue = this.saved ? LIBRARY_SAVED : LIBRARY_UNSAVED;
            if (this.statusLabel.getID().equals(newValue)) {
                return;
            }
            setStatusLabel(newValue);
        });
    }

    private void setStatusLabel(String newLabel) {
        this.statusLabel.setID(newLabel);
        this.statusLabel.setText(newLabel);
    }

    public boolean getSaved() {
        return this.saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

}
