package model;

import Enum.TaskStatus;


public class Task {
    private String name;
    private String description;
    private int id;
    private TaskStatus status;

    public Task(int id, String name, String description, TaskStatus taskStatus) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = taskStatus;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus taskStatus) {
        this.status = taskStatus;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }

        Task otherTask = (Task) object;
        if (this.id == ((Task) object).id) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.id * 31;
    }

    @Override
    public String toString() {
        return "Task={" +
                "name= " + getName() +
                ", description= " + getDescription() +
                ", status= " + getStatus() +
                ", id= " + getId() +
                "}";
    }



}
