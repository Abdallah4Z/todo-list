package com.example.todo.app;

public class Task {
    private final String id;
    private String title;
    private boolean statue;

    public Task() {
        this.id = "0";
    }

    public Task(String title, boolean statue, String id) {
        this.title = title;
        this.statue = statue;
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isComplete() {
        return this.statue;
    }

    public void setStatue(boolean statue) {
        this.statue = statue;
    }

    public String getId() {
        return this.id;
    }
}
