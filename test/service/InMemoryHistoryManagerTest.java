package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {
    private TaskManager manager;
    private Task task;
    private Epic epic;

    @BeforeEach
    void setUp() {
        task = new Task("Просто задача - 1", "Описание простой задачи - 1");
        epic = new Epic("Эпическая задача - 1",
                "Описание эпической задачи - 1");
        manager = Managers.getDefault();
    }

    @Test
    void addTaskInHistoryTest() {
        final int taskId = manager.addTask(task);

        manager.getTaskById(taskId);

        final List<Task> history = manager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void addEpicInHistoryTest() {
        final int epicId = manager.addEpic(epic);

        manager.getEpicById(epicId);

        final List<Task> history = manager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void addSubtaskInHistoryTest() {
        final int epicId = manager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача - 1",
                "Описание подзадачи - 1, эпической задачи - 1", epicId);
        final int subtaskId = manager.addSubtask(subtask);

        manager.getEpicById(epicId);
        manager.getSubtaskById(subtaskId);

        final List<Task> history = manager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(2, history.size(), "История не пустая.");
    }

    @Test
    void getHistoryTest() {
        final int epicId = manager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача - 1",
                "Описание подзадачи - 1, эпической задачи - 1", epicId);
        final int subtaskId = manager.addSubtask(subtask);

        manager.getEpicById(epicId);
        manager.getSubtaskById(subtaskId);

        final List<Task> history = manager.getHistory();

        assertEquals(history.get(0), epic, "История возвращается неверно");
        assertEquals(history.get(1), subtask, "История возвращается неверно");
    }
}
