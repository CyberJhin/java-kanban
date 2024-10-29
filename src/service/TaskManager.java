package service;

import java.util.HashMap;
import java.util.ArrayList;
import model.*;



public class TaskManager {

    private final HashMap<Integer, Task> taskList;
    private final HashMap<Integer, Epic> epicList;
    private final HashMap<Integer, Subtask> subtaskList;

    private int taskId = 0;

    public TaskManager() {
        this.taskList = new HashMap<>();
        this.epicList = new HashMap<>();
        this.subtaskList = new HashMap<>();
    }

    public void updateTask(Task task) {
        if (taskList.containsKey(task.getId())) {
            taskList.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (!epicList.containsKey(epic.getId())) {
            return;
        }
        Epic oldEpic = epicList.get(epic.getId());
        epicList.put(epic.getId(), epic);
        epic.setSubtasks(oldEpic.getSubtasks());
        epicCheckStatus(epic);
    }

    public void updateSubtask(Subtask subtask) {
        if (!subtaskList.containsKey(subtask.getId())) {
            return;
        }
        subtaskList.put(subtask.getId(), subtask);
        Epic epic = epicList.get(subtask.getEpicId());
        if (!epic.getSubtasks().contains(subtask)) {
            epic.getSubtasks().add(subtask);
        } else {
            epic.getSubtasks().remove(subtask);
            epic.getSubtasks().add(subtask);
        }
        epicCheckStatus(epic);
    }

    public int addTask(Task task) {
        task.setId(idGenerator());
        taskList.put(task.getId(), task);
        return task.getId();
    }

    public int addEpic(Epic epic) {
        epic.setId(idGenerator());
        epicList.put(epic.getId(), epic);
        return epic.getId();
    }

    public int addSubtask(Subtask subtask) {
        subtask.setId(idGenerator());
        subtaskList.put(subtask.getId(), subtask);
        Epic epic = epicList.get(subtask.getEpicId());
        epic.addSubtask(subtask);
        epicCheckStatus(epic);
        return subtask.getId();
    }

    private void epicCheckStatus(Epic epic) {
        int newStatusCount = 0;
        int doneStatusCount = 0;

        for (Subtask subtask : epic.getSubtasks()) {
            if (subtask.getStatus() == TaskStatus.IN_PROGRESS) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
                return;
            }
            if (subtask.getStatus() == TaskStatus.NEW) {
                newStatusCount++;
            } else if (subtask.getStatus() == TaskStatus.DONE) {
                doneStatusCount++;
            }
        }

        if (newStatusCount == epic.getSubtasks().size()) {
            epic.setStatus(TaskStatus.NEW);
        } else if (doneStatusCount == epic.getSubtasks().size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public Task getTaskById(int id) {
        return taskList.get(id);
    }

    public Epic getEpicById(int id) {
        return epicList.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtaskList.get(id);
    }

    public void deleteTaskById(int id) {
        taskList.remove(id);
    }

    public void deleteEpicById(int id) {
        if (epicList.containsKey(id)) {
            Epic epic = epicList.remove(id);
            for (Subtask subtask : epic.getSubtasks()) {
                subtaskList.remove(subtask.getId());
            }
        }
    }

    public void deleteSubtaskById(int id) {
        Subtask subtask = subtaskList.remove(id);
        if (subtask != null) {
            Epic epic = epicList.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtasks().remove(subtask);
                epicCheckStatus(epic);
            }
        }
    }

    public void deleteEpics() {
        epicList.clear();
        subtaskList.clear();
    }

    public void deleteTasks() {
        taskList.clear();
    }

    public void deleteSubtasks() {
        subtaskList.clear();
        for (Epic epic : epicList.values()) {
            epic.clearEpicsSubtasks();
            epic.setStatus(TaskStatus.NEW);
        }
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epicList.values());
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(taskList.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtaskList.values());
    }

    private int idGenerator() {
        return ++taskId;
    }
}
