package com.example.todo.GUI;

import com.example.todo.app.*;
import com.example.todo.app.interfaces.DatePasser;
import com.example.todo.app.interfaces.ProgressValuePasser;
import com.example.todo.app.interfaces.TaskIDPasser;
import com.example.todo.app.utility.ConfigLoader;
import com.example.todo.app.utility.DataOrganizer;
import com.example.todo.app.utility.Loader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TodoListController implements TaskIDPasser, ProgressValuePasser, DatePasser {

    // FXML components

    @FXML private AnchorPane taskPane;
    @FXML private Button calenderButton;
    @FXML private Button leftButton;
    @FXML private Button rightButton;
    @FXML private MenuButton tagsMenu;
    @FXML private VBox tasksBox;
    @FXML private MenuButton tasksMenu;
    @FXML private Label toDoListDateLabel;
    @FXML private VBox rightSidePane;
    private String selectedID ;
    private Date date ;
    private TodoItemsHandler todoItemsHandler;


    @FXML
    public void initialize() {
        this.date = new Date();
        toDoListDateLabel.setText(dateLabelData());

        todoItemsHandler = new TodoItemsHandler();
        loadTasks(todoItemsHandler.getTodoItemList());
        fillMenu(tagsMenu, todoItemsHandler.getAllUniqueTags(), this::handleTagAction);

        fillMenu(tasksMenu, new ArrayList<>(Arrays.asList("Day", "Week", "Month", "Year")), this::handleTaskAction);
    }

    @Override
    public void sendTaskID(String id) {
        System.out.println(id);
        selectedID = id;
        loadTaskPane(todoItemsHandler.getTodoItem(id));

        tasksBox.getChildren().forEach(box -> {
            boolean isSelected = box.getUserData().equals(id);
            if (isSelected && box.getStyleClass().isEmpty()) box.getStyleClass().add("selected");
            else box.getStyleClass().remove("selected");
        });
    }

    private void fillMenu(MenuButton menuButton, List<String> items, EventHandler<ActionEvent> handler) {
        for (String item : items) {
            MenuItem menuItem = new MenuItem(item);
            menuItem.setOnAction(handler);
            menuButton.getItems().add(menuItem);
        }
    }

    private void handleTagAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String selectedTag = menuItem.getText();
        List<TodoItem> list = new DataOrganizer().containsTag(todoItemsHandler.getTodoItemList(), selectedTag);
        loadPanes(list);
    }


    private void handleTaskAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String selectedTask = menuItem.getText();
        System.out.println("Selected Task: " + selectedTask);
        List<TodoItem> list = new DataOrganizer().searchDate(todoItemsHandler.getTodoItemList(), selectedTask);
        loadPanes(list);
    }


    @FXML
    void nextDayList(ActionEvent event) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        this.date = calendar.getTime();
        List<TodoItem> list = new DataOrganizer().filterByDay(todoItemsHandler.getTodoItemList(), date);
        loadPanes(list);
        toDoListDateLabel.setText(dateLabelData());
    }

    @FXML
    void lastDayList(ActionEvent event) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        this.date = calendar.getTime();
        List<TodoItem> list = new DataOrganizer().filterByDay(todoItemsHandler.getTodoItemList(), date);
        loadPanes(list);
        toDoListDateLabel.setText(dateLabelData());

    }

    private String dateLabelData(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM", Locale.ENGLISH);
        return formatter.format(this.date);
    }

    @FXML
    void datePicker(ActionEvent event) throws IOException {
        String calenderPath = ConfigLoader.getPath("calenderView");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(calenderPath));

        // Load the FXML file first
        Parent root = loader.load();

        // Now the controller is instantiated, so you can access it
        CalenderController controller = loader.getController();
        controller.setPasser(this);

        Stage stage = new Stage();
        Scene scene = new Scene(root, 470, 400); // Use the loaded root node
        stage.setTitle("Calendar");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void sendDate(String date) {
        LocalDate LDate = LocalDate.parse(date);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.ENGLISH);

        String formattedDate = LDate.format(formatter);

        toDoListDateLabel.setText(formattedDate);
        List<TodoItem> list = new DataOrganizer().filterBySpecificDate(todoItemsHandler.getTodoItemList(), date);
        loadPanes(list);
    }


    // loaders
    private void loadTaskPane(TodoItem todoItem){
        Loader loader = new Loader(taskPane, this);
        loader.loadTaskPane(todoItem);
    }
    private void loadTasks(List<TodoItem> todoItemList){
        Loader loader = new Loader(tasksBox, this);
        loader.loadTasks(todoItemList);
    }


    public void loadPanes(List<TodoItem> list){
        loadTasks(list);
        if (!list.isEmpty()){
            loadTaskPane(list.getFirst());
        }
    }
    @Override
    public void progressValue(double value) {
        tasksBox.getChildren().stream()
                .filter(n -> selectedID.equals(n.getUserData()))
                .filter(Parent.class::isInstance)
                .map(Parent.class::cast)
                .flatMap(parent -> parent.getChildrenUnmodifiable().stream())
                .filter(HBox.class::isInstance)
                .map(HBox.class::cast)
                .flatMap(hbox -> hbox.getChildren().stream())
                .forEach(node -> {
                    if (node instanceof ProgressBar progressBar) {
                        progressBar.setProgress(value);
                    } else if (node instanceof CheckBox checkBox) {
                        checkBox.setSelected(value == 1);
                    }
                });
    }


}
