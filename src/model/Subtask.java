package model;

public class Subtask extends Task{

    private final int epicId;

    public Subtask(int id, String name, String description, TaskStatus taskStatus, int epicId) {
        super(id, name, description, taskStatus);
        if (id == epicId) {
            throw new IllegalArgumentException("Subtask cannot reference itself as its own epic.");
        }
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, int epicId) {
        super(id, name, description);
        if (id == epicId) {
            throw new IllegalArgumentException("Subtask cannot reference itself as its own epic.");
        }
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        if (getId() == epicId) { // Assuming ID will be set before use
            throw new IllegalArgumentException("Subtask cannot reference itself as its own epic.");
        }
        this.epicId = epicId;
    }


    public int getEpicId() {
        return this.epicId;
    }

    @Override
    public String toString() {
        return "Subtask={" +
                "name= " + getName() +
                ", description= " + getDescription() +
                ", status= " + getStatus() +
                ", id= " + getId() +
                ", epicId= " + getEpicId() + "}";
    }

}
