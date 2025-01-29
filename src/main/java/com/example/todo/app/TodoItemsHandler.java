package com.example.todo.app;

import com.example.todo.database.TodoListDatabase;

import java.util.*;

public class TodoItemsHandler {
    private List<TodoItem> todoItemList;
    private TodoListDatabase database;


    // Constructor to initialize the database and load todo items.
    public TodoItemsHandler() {
        initializeDatabase();
        this.todoItemList = readTodoListData();
    }

    // Constructor to initialize the database with a predefined list of todo items
    public TodoItemsHandler(List<TodoItem> todoItemList) {
        initializeDatabase();
        this.todoItemList = new ArrayList<>(todoItemList);
    }

    public List<TodoItem> getTodoItemList() {
        return todoItemList;
    }

    public void setTodoItemList(List<TodoItem> todoItemList) {
        this.todoItemList = todoItemList;
    }


    // Adds a new todo item to the list and database
    public void addTodoItem(TodoItem todoItem) {
        if (todoItem == null) {
            throw new IllegalArgumentException("TodoItem cannot be null");
        }
        todoItem.setId(generateID());
        this.todoItemList.add(todoItem);
        database.insertTodo(todoItem);
    }

    // Retrieves a todo item by its ID
    public TodoItem getTodoItem(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        return todoItemList.stream()
                .filter(todo -> id.equals(todo.getId()))
                .findFirst()
                .orElse(null);
    }

    public void removeToDo(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        TodoItem todoToRemove = getTodoItem(id);
        if (todoToRemove != null) {
            todoItemList.remove(todoToRemove);
            database.deleteTodo(id);
        }
    }

    // Updates a todo item in the database
    public void updateTodo(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        TodoItem todoToUpdate = getTodoItem(id);
        if (todoToUpdate != null) {
            database.deleteTodo(id);
            database.insertTodo(todoToUpdate);
        }
    }

    // Reads all todo items from the database
    private List<TodoItem> readTodoListData() {
        return database.selectAllTodos();
    }

    // Initializes the database
    private void initializeDatabase() {
        this.database = new TodoListDatabase();
        database.initializeDatabase();
    }


    // Returns a list of all unique tags from all todo items
    public List<String> getAllUniqueTags() {
        Set<String> tags = new HashSet<>();
        for (TodoItem todoItem : todoItemList) {
            tags.addAll(todoItem.getTags());
        }
        return new ArrayList<>(tags);
    }


    /**
     * Generates a unique ID for a TodoItem.
     * The ID starts with "TODO-" followed by 5 random digits (0-9).
     * Ensures the generated ID is unique by checking against existing IDs in the todoItemList.
     */
    public String generateID() {
        String generatedId;
        do {
            generatedId = generateRandomId();
        } while (isDuplicateId(generatedId));
        return generatedId;
    }

    // Generates a random ID for a TodoItem starts with "TODO-" followed by 5 random digits (0-9)
    private String generateRandomId() {
        Random random = new Random();
        StringBuilder id = new StringBuilder("TODO-");
        for (int i = 0; i < 5; i++) {
            id.append(random.nextInt(10)); // Append a random digit (0-9)
        }
        return id.toString();
    }

    // Checks if a given ID already exists in the todoItemList

    private boolean isDuplicateId(String id) {
        if (todoItemList == null || todoItemList.isEmpty()) {
            return false; // No duplicates if the list is empty
        }
        Set<String> existingIds = getExistingIds();
        return existingIds.contains(id);
    }


    // Retrieves a set of all existing IDs from the todoItemList
    private Set<String> getExistingIds() {
        Set<String> existingIds = new HashSet<>();
        for (TodoItem todo : todoItemList) {
            existingIds.add(todo.getId());
        }
        return existingIds;
    }

}