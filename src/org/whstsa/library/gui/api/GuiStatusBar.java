package org.whstsa.library.gui.api;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.whstsa.library.World;
import org.whstsa.library.gui.components.LabelElement;
import org.whstsa.library.gui.factories.GuiUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GuiStatusBar extends HBox {

    private Image icon;
    private LabelElement statusLabel;
    private LabelElement dateLabel;

    public GuiStatusBar() {
        this.icon = new Image("file:LibraryManagerIcon.png");
        ImageView imageView = new ImageView(this.icon);
        imageView.setFitHeight(10);
        imageView.setPreserveRatio(true);

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

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    private void liveDate() {
        Runnable runnable = () -> {
            while (true) {
                String dateString = simpleDateFormat.format(World.getDate());
                this.dateLabel.setText(dateString);

                if (!World.getDate().equals(new Date())) {
                    this.dateLabel.setTextFill(Color.web("#0056ad"));
                    this.dateLabel.setTooltip(new Tooltip("This is the date simulated by the Library Manager, not today's date."));
                }
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t = new Thread(runnable);
        t.start();
    }

    private void liveSavedStatus() {
        Runnable runnable = () -> {
            while (true) {
                String savedString = isSaved() ? "The library is saved." : "The library is unsaved. Exiting will erase all progress since last save.";
                this.statusLabel.setText(savedString);
                try {
                    Thread.sleep(5000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t = new Thread(runnable);
        t.start();
    }

    public void setStatusLabel(String newLabel) {
        this.statusLabel = GuiUtils.createLabel(newLabel);
    }

    private boolean isSaved() {
        return false;//TODO Check if JSON file has been changed
    }


}
