package com.example.todo.GUI;

import com.example.todo.app.interfaces.TaskIDPasser;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;

public class TaskBoxController {

    // FXML components
    @FXML private CheckBox checkBox;
    @FXML private Label id;
    @FXML private Label memo;
    @FXML private ProgressBar progressBar;
    @FXML private Label subTitle;
    @FXML private Label title;

    private TaskIDPasser taskIDPasser;


    //Method to set task data
    public void setTaskData(String taskId, String taskTitle, String taskSubTitle, String taskMemo, double taskProgress, boolean isCompleted, TaskIDPasser td) {
        id.setText(taskId);
        title.setText(taskTitle);
        subTitle.setText(taskSubTitle);
        memo.setText(taskMemo);
        progressBar.setProgress(taskProgress);
        checkBox.setSelected(isCompleted);
        taskIDPasser = td;
    }

    //Action listener for mouse clicking on task box
    @FXML
    void printId(MouseEvent event) {
        produceData();
    }


    // Method to pass task id to TodoListController
    public void produceData() {
        taskIDPasser.sendTaskID(id.getText());
    }
}
