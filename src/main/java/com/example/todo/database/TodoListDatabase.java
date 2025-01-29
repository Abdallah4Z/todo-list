package com.example.todo.database;

import com.example.todo.app.Task;
import com.example.todo.app.TodoItem;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TodoListDatabase {
    private static final String DB_FILE = "todo.db";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH );

    // Load the SQLite JDBC driver
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load the SQLite JDBC driver\nError message: "+ e);

        }
    }

    // Connect to the SQLite database
    private Connection connect() throws SQLException {
        String url = "jdbc:sqlite:" + DB_FILE;
        return DriverManager.getConnection(url);
    }

    // Initialize the database (create tables if they don't exist)
    public void initializeDatabase() {
        String createTodosTable = "CREATE TABLE IF NOT EXISTS todos (" +
                "id TEXT PRIMARY KEY, " +
                "title TEXT NOT NULL, " +
                "subtitle TEXT, " +
                "memo TEXT, " +
                "dueDate TEXT, " +
                "tags TEXT" +
                ");";

        String createTasksTable = "CREATE TABLE IF NOT EXISTS tasks (" +
                "id TEXT PRIMARY KEY, " +
                "todo_id TEXT, " +
                "name TEXT NOT NULL, " +
                "status INTEGER, " +
                "FOREIGN KEY (todo_id) REFERENCES todos(id)" +
                ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            // Execute SQL statements
            stmt.execute(createTodosTable);
            stmt.execute(createTasksTable);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to initialize the database \nError message: "+ e);
        }
    }

    // Insert a new todo
    public void insertTodo(TodoItem todo) {
        String sql = "INSERT INTO todos(id, title, subtitle, memo, dueDate, tags) VALUES(?,?,?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, todo.getId());
            pstmt.setString(2, todo.getTitle());
            pstmt.setString(3, todo.getSubTitle());
            pstmt.setString(4, todo.getMemo());
            pstmt.setString(5, DATE_FORMAT.format(todo.getDate()));
            pstmt.setString(6, String.join(",", todo.getTags()));
            pstmt.executeUpdate();
            System.out.println("Todo inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to insert the todo with id: "+ todo.getId() +"\nError message: "+ e);
        }
    }

    // Select all todos
    public List<TodoItem> selectAllTodos() {
        String sql = "SELECT id, title, subtitle, memo, dueDate, tags FROM todos";
        List<TodoItem> todos = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Convert tags from comma-separated string to List<String>
                List<String> tags = Arrays.asList(rs.getString("tags").split(","));

                // Parse the dueDate string into a Date object
                Date dueDate =  DATE_FORMAT.parse(rs.getString("dueDate"));

                TodoItem todo = new TodoItem(
                        rs.getString("title"),
                        rs.getString("subtitle"),
                        rs.getString("memo"),
                        tags,
                        selectTasksByTodoId(rs.getString("id")), // Tasks will be loaded separately
                        dueDate,
                        rs.getString("id")
                );
                todos.add(todo);
            }
        } catch (SQLException | ParseException e) {
            System.out.println("Failed to update the return the todoList data\nError message: "+ e);
        }
        return todos;
    }

    // Delete a todo
    public void deleteTodo(String id) {
        String sql = "DELETE FROM todos WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            System.out.println("Todo deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to delete the todo with id: "+ id +"\nError message: "+ e);
        }
    }

    // Select all tasks for a specific todo
    public List<Task> selectTasksByTodoId(String todoId) {
        String sql = "SELECT id, name, status FROM tasks WHERE todo_id = ?";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, todoId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                boolean status = rs.getInt("status") == 1;
                Task task = new Task(
                        rs.getString("name"),
                        status,
                        rs.getString("id").replace(todoId, "")
                );
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.out.println("Failed to get tasks for todo id: "+ todoId +"\nError message: "+ e);

        }
        return tasks;
    }
}