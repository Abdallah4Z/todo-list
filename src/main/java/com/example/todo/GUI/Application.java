package com.example.todo.GUI;

import com.example.todo.app.utility.ConfigLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        String todoListViewPath = ConfigLoader.getPath("todoListView");
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(todoListViewPath));
        Scene scene = new Scene(fxmlLoader.load(), 975, 600);
        stage.setTitle("TODO List");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}