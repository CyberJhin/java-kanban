package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> taskList;
    private final HashMap<Integer, Epic> epicList;
    private final HashMap<Integer, Subtask> subtaskList;
    private final InMemoryHistoryManager historyManager;  // История задач
    private int taskId = 0;

    public InMemoryTaskManager() {
        this.taskList = new HashMap<>();
        this.epicList = new HashMap<>();
        this.subtaskList = new HashMap<>();
        this.historyManager = new InMemoryHistoryManager();  // Инициализация менеджера истории
    }

    @Override
    public void updateTask(Task task) {
        if (taskList.containsKey(task.getId())) {
            taskList.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!epicList.containsKey(epic.getId())) {
            return;
        }
        Epic oldEpic = epicList.get(epic.getId());
        epicList.put(epic.getId(), epic);
        epic.setSubtasks(oldEpic.getSubtasks());
        epicCheckStatus(epic);
    }

    @Override
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

    @Override
    public int addTask(Task task) {
        task.setId(idGenerator());
        taskList.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int addEpic(Epic epic) {
        epic.setId(idGenerator());
        epicList.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
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

    @Override
    public Task getTaskById(int id) {
        Task task = taskList.get(id);
        if (task != null) {
            historyManager.addInHistory(task);  // Добавляем задачу в историю
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicList.get(id);
        if (epic != null) {
            historyManager.addInHistory(epic);  // Добавляем эпик в историю
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtaskList.get(id);
        if (subtask != null) {
            historyManager.addInHistory(subtask);  // Добавляем подзадачу в историю
        }
        return subtask;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(taskList.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskList.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epicList.values());
    }

    @Override
    public void deleteTaskById(int id) {
        taskList.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        if (epicList.containsKey(id)) {
            Epic epic = epicList.remove(id);
            for (Subtask subtask : epic.getSubtasks()) {
                subtaskList.remove(subtask.getId());
            }
        }
    }

    @Override
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

    @Override
    public void deleteEpics() {
        epicList.clear();
        subtaskList.clear();
    }

    @Override
    public void deleteTasks() {
        taskList.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtaskList.clear();
        for (Epic epic : epicList.values()) {
            epic.clearEpicsSubtasks();
            epic.setStatus(TaskStatus.NEW);
        }
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epicList.values());
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(taskList.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtaskList.values());
    }

    private int idGenerator() {
        return ++taskId;
    }

    // Метод для получения истории
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(historyManager.getHistory());
    }
}
