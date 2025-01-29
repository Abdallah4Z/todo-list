package com.example.todo.app.utility;

import com.example.todo.app.TodoItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DataOrganizer {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public List<TodoItem> searchDate(List<TodoItem> todoItemList, String type) {
        return switch (type) {
            case "Year" -> thisYear(todoItemList);
            case "Week" -> thisWeek(todoItemList);
            case "Month" -> thisMonth(todoItemList);
            default -> thisDay(todoItemList);
        };
    }

    // Method to get tasks for the last day
    public List<TodoItem> filterByDay(List<TodoItem> todoList, Date targetDate) {
        return filterByDate(todoList, targetDate);
    }


    // Method to filter tasks by a specific date
    private List<TodoItem> filterByDate(List<TodoItem> todoList, Date date) {
        return todoList.stream().filter(todo -> isSameDate(todo.getDate(), date)).collect(Collectors.toList());
    }

    // Method to check if two dates are the same only for year, month and day
    private boolean isSameDate(Date date1, Date date2) {
        return DATE_FORMAT.format(date1).equals(DATE_FORMAT.format(date2));
    }

    // Method to get tasks for today
    private List<TodoItem> thisDay(List<TodoItem> todoList) {
        Date today = new Date(); // Current date
        return filterByDate(todoList, today);
    }

    public List<TodoItem> filterBySpecificDate(List<TodoItem> todoList, String dateString) {
        Date specificDate;

        // Parse the input date string into a Date object and Return an empty list if parsing fails
        try {
            specificDate = DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Failed to parse the string into date object " + e);
            return new ArrayList<>();
        }

        // Filter the TodoList for the specific date
        return filterByDate(todoList, specificDate);
    }

    // Method to get tasks for this week
    private List<TodoItem> thisWeek(List<TodoItem> todoList) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek()); // Start
        Date startOfWeek = calendar.getTime();
        calendar.add(Calendar.DAY_OF_WEEK, 6); // End
        Date endOfWeek = calendar.getTime();
        return filterByDateRange(todoList, startOfWeek, endOfWeek);
    }

    // Method to get tasks for this month
    private List<TodoItem> thisMonth(List<TodoItem> todoList) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1); // Start
        Date startOfMonth = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1); // End
        Date endOfMonth = calendar.getTime();
        return filterByDateRange(todoList, startOfMonth, endOfMonth);
    }

    // Method to get tasks for this year
    private List<TodoItem> thisYear(List<TodoItem> todoList) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1); // Start
        Date startOfYear = calendar.getTime();
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1); // End
        Date endOfYear = calendar.getTime();
        return filterByDateRange(todoList, startOfYear, endOfYear);
    }

    // Method to filter tasks by a specific tag
    public List<TodoItem> containsTag(List<TodoItem> todoList, String tag) {
        return todoList.stream().filter(todo -> todo.getTags() != null && todo.getTags().contains(tag)).collect(Collectors.toList());
    }


    // Method to filter tasks by a date range
    private List<TodoItem> filterByDateRange(List<TodoItem> todoList, Date startDate, Date endDate) {
        return todoList.stream().filter(todo -> isDateInRange(todo.getDate(), startDate, endDate)).collect(Collectors.toList());
    }


    // Method to check if a date is within a range
    private boolean isDateInRange(Date date, Date startDate, Date endDate) {
        return !date.before(startDate) && !date.after(endDate);
    }
}