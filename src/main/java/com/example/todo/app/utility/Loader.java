package com.example.todo.app.utility;

import com.example.todo.GUI.TaskBoxController;
import com.example.todo.GUI.TaskPaneController;
import com.example.todo.app.Task;
import com.example.todo.app.TodoItem;
import com.example.todo.app.interfaces.ProgressValuePasser;
import com.example.todo.app.interfaces.TaskIDPasser;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

/**
 * Utility class for loading task boxes and task panes into the UI.
 * This class handles the loading of FXML views and setting up their controllers.
 */

public class Loader {
    private VBox tasksBox;
    private TaskIDPasser taskIDPasser;
    private AnchorPane taskPane;
    private ProgressValuePasser progressValuePasser;

    // Constructor for loading task boxes.
    public Loader(VBox tasksBox, TaskIDPasser taskIDPasser) {
        this.taskIDPasser = taskIDPasser;
        this.tasksBox = tasksBox;
    }

    // Constructor for loading the task pane.
    public Loader(AnchorPane taskPane, ProgressValuePasser progressValuePasser) {
        this.taskPane = taskPane;
        this.progressValuePasser = progressValuePasser;
    }

    // Loads a task box into the tasks container.
    public void loadTaskBox(TodoItem todo, double completionProgress, boolean isComplete) {
        try {
            String taskBoxPath = ConfigLoader.getPath("taskBoxView");
            FXMLLoader loader = new FXMLLoader(getClass().getResource(taskBoxPath));
            Node loadedVBox = loader.load();
            TaskBoxController controller = loader.getController();
            controller.setTaskData(todo.getId(), todo.getTitle(), todo.getSubTitle(), todo.getMemo(), completionProgress, isComplete, taskIDPasser);
            loadedVBox.setUserData(todo.getId());  // Store the task ID in the node
            tasksBox.getChildren().add(loadedVBox);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load task box", e);
        }
    }

    // Loads all tasks into the tasks container

    public void loadTasks(List<TodoItem> todoItemList) {
        if (!tasksBox.getChildren().isEmpty()) {
            tasksBox.getChildren().clear();
        }
        for (TodoItem todo : todoItemList) {
            int totalTasks = todo.getTasks().size();
            int completedTasks = totalTasks > 0 ? countCompletedTasks(todo.getTasks()) : 0;
            double completionProgress = totalTasks > 0 ? (double) completedTasks / totalTasks : 0;
            boolean isComplete = completionProgress == 1.0;

            loadTaskBox(todo, completionProgress, isComplete); // Load each task box
        }
    }

    // Counts the number of completed tasks in a list.
    public int countCompletedTasks(List<Task> tasks) {
        return (int) tasks.stream().filter(Task::isComplete).count();
    }

    // Loads the task pane with detailed information for a specific task.
    public void loadTaskPane(TodoItem todo) {
        try {
            String taskPanePath = ConfigLoader.getPath("taskPaneView");
            FXMLLoader loader = new FXMLLoader(getClass().getResource(taskPanePath));
            Node loadedVBox = loader.load();
            TaskPaneController controller = loader.getController();
            controller.setTaskData(todo, progressValuePasser);
            taskPane.getChildren().add(loadedVBox);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load task box", e);
        }
    }

}
