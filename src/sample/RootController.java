package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

abstract class RootController {

    @FXML
    Stage stage;

    void initStage(Parent root, String cssPath, String title) throws Exception {
        stage.setTitle(title);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass()
                .getResource(cssPath).toExternalForm());
        stage.setScene(scene);
    }

    abstract void initMainWindow();

    Label getCalendarGridCellLabel(Node node) {
        return ((Label) ((StackPane) getCalendarGridCell(node).getChildren().get(0)).getChildren().get(0));
    }

    VBox getCalendarGridCellEventsBox(Node node) {
        return ((VBox) (((ScrollPane) (getCalendarGridCell(node).getChildren().get(1))).getContent()));
    }

    private VBox getCalendarGridCell(Node node) {
        return ((VBox) (((StackPane) node).getChildren().get(0)));
    }

    Label getPanelCellLabel(Node node, boolean second) {
        return ((Label) getCalendarGridCell(node).getChildren().get(second ? 1 : 0));
    }
}
