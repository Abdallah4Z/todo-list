package com.example.todo.GUI;

import com.example.todo.app.interfaces.DatePasser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class CalenderController implements Initializable {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd", Locale.ENGLISH);

    @FXML private ComboBox<String> monthComboBox;
    @FXML private ComboBox<Integer> yearComboBox;
    @FXML private GridPane calendarGrid; // Grid to display the calendar days
    private DatePasser datePasser; // Interface to pass the selected date back to the TodoList Controller

    /**
     * Initializes the calendar view when the FXML is loaded.
     * Sets up the month and year dropdowns and updates the calendar display.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTopBar();
        updateCalendar();
    }


    // Initializes the top bar of the calendar, which includes the month and year dropdowns.
    private void initializeTopBar() {
        yearComboBox.getItems().addAll(IntStream.range(2020, 2051).boxed().toList());
        yearComboBox.setValue(LocalDate.now().getYear());

        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        monthComboBox.getItems().addAll(months);
        monthComboBox.setValue(months[LocalDate.now().getMonthValue() - 1]);

        yearComboBox.setOnAction(event -> updateCalendar());
        monthComboBox.setOnAction(event -> updateCalendar());
    }

    // Updates the calendar grid to display the days of the selected month and year
    private void updateCalendar() {
        calendarGrid.getChildren().clear();
        int selectedYear = yearComboBox.getValue();
        int selectedMonth = monthComboBox.getSelectionModel().getSelectedIndex() + 1;

        YearMonth yearMonth = YearMonth.of(selectedYear, selectedMonth);
        int daysInMonth = yearMonth.lengthOfMonth();
        int firstDayOfWeek = yearMonth.atDay(1).getDayOfWeek().getValue() % 7;

        updateDays(firstDayOfWeek, daysInMonth, selectedYear, selectedMonth);
    }

    // Updates the calendar grid with the days of the month.
    private void updateDays(int firstDayOfWeek, int daysInMonth, int selectedYear, int selectedMonth) {
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < dayNames.length; i++) {
            Label dayLabel = new Label(dayNames[i]);
            dayLabel.getStyleClass().add("day-label");
            calendarGrid.add(dayLabel, i, 0);
        }

        updateDayCells(firstDayOfWeek, daysInMonth, selectedYear, selectedMonth);
    }

    // Updates the calendar grid with the day cells for the selected month
    private void updateDayCells(int firstDayOfWeek, int daysInMonth, int selectedYear, int selectedMonth) {
        int row = 1;
        int col = firstDayOfWeek;
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = LocalDate.of(selectedYear, selectedMonth, day);
            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.getStyleClass().add("day-cell");
            StackPane dayCell = new StackPane(dayLabel);
            dayCell.getStyleClass().add("day-box");

            dayCell.setOnMouseClicked(event -> handleDayClick(date));

            calendarGrid.add(dayCell, col, row);

            if (++col == 7) {
                col = 0;
                row++;
            }
        }
    }

    // Handles a click event on a day cell.
    private void handleDayClick(LocalDate date) {
        String formattedDate = date.format(DATE_FORMATTER);
        datePasser.sendDate(formattedDate); // pass the date back to TodoList Controller

        // Close the calendar window
        Stage stage = (Stage) calendarGrid.getScene().getWindow();
        stage.close();
    }

    // Sets the DatePasser interface to allow communication with TodoListController
    public void setPasser(DatePasser dp) {
        datePasser = dp;
    }
}