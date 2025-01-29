package com.example.todo.GUI;

import com.example.todo.app.interfaces.ProgressValuePasser;
import com.example.todo.app.Task;
import com.example.todo.app.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskPaneController implements ProgressValuePasser {

    // FXML components
    @FXML private Label title;
    @FXML private Label date;
    @FXML private Label memo;
    @FXML private ProgressBar progressBar;
    @FXML private Label subTitle;
    @FXML private VBox tasksPane; // VBox to display the list of tasks
    @FXML private HBox tagsBox; // HBox to display the tags associated with the task

    // Class fields
    private TodoItem todo ;
    private ProgressValuePasser progressValuePasser;  // Callback to pass progress updates

    public void setTaskData(TodoItem todo, ProgressValuePasser progressValuePasser) {
        this.todo = todo;
        this.progressValuePasser  = progressValuePasser;

        // Initialize UI components
        title.setText(todo.getTitle());
        subTitle.setText(todo.getSubTitle());
        memo.setText(todo.getMemo());
        progressBar.setProgress(progressValue(todo.getTasks()));
        date.setText(formattedDate(todo.getDate()));

        // Populate the tasks and tags
        setTasks(todo.getTasks());
        displayTags();
    }

    // Formats the given date into a readable string
    public String formattedDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MMMM, yyyy", Locale.ENGLISH);
        return dateFormat.format(date);
    }

    // Populates the tasksPane with checkbox for each task
    public void setTasks(List<Task> tasks){
        tasksPane.getChildren().clear();

        for (Task task: tasks){
            CheckBox checkBox = new CheckBox(task.getTitle());
            checkBox.setUserData(task.getId()); // Store the task ID in the checkbox
            checkBox.setSelected(task.isComplete());

            checkBox.setOnAction(event -> handleCheckboxClick(checkBox));
            tasksPane.getChildren().add(checkBox);
        }
    }

    // Handles the event when a checkbox is clicked
    private void handleCheckboxClick(CheckBox checkBox) {
        String taskId = checkBox.getUserData().toString();
        Task task = todo.getTask(taskId);

        if (task != null) {
            task.setStatue(checkBox.isSelected());
            progressBar.setProgress(progressValue(todo.getTasks()));
            produceData(); // Notify the progressValuePasser of the updated progress
        }
    }

    // Counts the number of completed tasks in the given list and return it
    public int countCompletedTasks(List<Task> tasks) {
        return (int) tasks.stream().filter(Task::isComplete).count();
    }

    // Calculates the percentage of completed tasks -> progress value
    public double progressValue(List<Task> tasks){
        if (!tasks.isEmpty())  return (double) countCompletedTasks(tasks) / tasks.size();
        else return 0;
    }

    // Displays the tags into tags box
    public void displayTags() {
        tagsBox.getChildren().clear();
        todo.getTags().stream()
                .map(tag -> {
                    Label label = new Label(tag);
                    label.getStyleClass().add("tag");
                    return label;
                })
                .forEach(tagsBox.getChildren()::add);
    }

    // Notifies the progressValuePasser of the updated progress value
    public void produceData(){
        double progress = progressValue(todo.getTasks());
        progressValuePasser.progressValue(progress);
    }

    // This method is called when the progress value needs to be updated
    @Override
    public void progressValue(double value) {
        produceData();
    }

}
