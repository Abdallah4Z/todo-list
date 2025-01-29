package com.example.todo.app;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TodoItem {

    private String id;
    private String title;
    private String subTitle;
    private String memo;
    private List<String> tags;
    private List<Task> tasks;
    private Date date;

    public TodoItem() {

        this.id = "0";
    }

    public TodoItem(String title, String subTitle, String memo, List<String> tags, List<Task> tasks, Date date, String id) {
        this.title = title;
        this.subTitle = subTitle;
        this.memo = memo;
        this.tags = tags;
        this.tasks = tasks;
        this.date = date;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Task getTask(String id) {
        for (Task task :
                this.tasks) {
            if (task.getId().equals(id)) return task;
        }
        return null;
    }

    public void addTask(String title, boolean statue) {
        Task task = new Task(title, statue, generateTaskID());
        this.tasks.add(task);
    }

    private String generateTaskID() {
        Set<Integer> usedIds = new HashSet<>();
        for (Task task : tasks)
            usedIds.add(Integer.valueOf(task.getId()));

        int newId = 1;
        while (usedIds.contains(newId))
            newId++;

        return String.valueOf(newId);
    }

    public void removeTask(String id) {
        this.tasks.remove(getTask(id));
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public void removeTag(String tag) {
        this.tags.remove(tag);
    }

    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }

}
