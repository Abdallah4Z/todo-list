module com.example.todo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires java.desktop;


    opens com.example.todo.app to javafx.fxml;
    exports com.example.todo.app;

    opens com.example.todo.GUI to javafx.fxml;
    exports com.example.todo.GUI;
    exports com.example.todo.app.interfaces;
    opens com.example.todo.app.interfaces to javafx.fxml;

    exports com.example.todo.database;
    opens com.example.todo.database to javafx.fxml;
    exports com.example.todo.app.utility;
    opens com.example.todo.app.utility to javafx.fxml;
}