package it.unicam.cs.pa.jbudget105057;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 *
 * Classe che rappresenta la gui ed il suo punto di inizio dove richiama il file fxml.
 */
public class GUIView extends Application implements View {


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Menu.fxml"));
        primaryStage.setTitle("JBudget");
        primaryStage.setScene(new Scene(root, 682, 450));
        primaryStage.resizableProperty().setValue(false);
        primaryStage.show();
    }


    @Override
    public void open() {
    }

    @Override
    public void close() {
    }

}
