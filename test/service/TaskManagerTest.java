package service;

import model.Epic;
import model.TaskStatus;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
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
    void addNewTaskTest() {
        final int taskId = manager.addTask(task);
        final Task savedTask = manager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = manager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void updateTaskTest() {
        final int taskId = manager.addTask(task);
        final Task savedTask = manager.getTaskById(taskId);

        savedTask.setStatus(TaskStatus.DONE);
        manager.updateTask(savedTask);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertEquals(task.getStatus(), savedTask.getStatus(), "Статусы задач не совпадают");
    }

    @Test
    void deleteTaskTest() {
        manager.addTask(task);
        manager.deleteTaskById(task.getId());

        assertTrue(manager.getAllTasks().isEmpty(), "Задача не удалилась");
        assertEquals(0, manager.getAllTasks().size(), "Задача не удалилась");
    }

    @Test
    void deleteAllTasksTest() {
        manager.addTask(task);
        manager.addTask(task);
        manager.deleteTasks();

        assertTrue(manager.getAllTasks().isEmpty(), "Задачи не удалились");
        assertEquals(0, manager.getAllTasks().size(), "Задачи не удалились");
    }

    @Test
    void addNewEpicTest() {
        final int epicId = manager.addEpic(epic);
        final Epic savedEpic = manager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найдена.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = manager.getAllEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество Эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void addNewSubtaskTest() {
        final int epicId = manager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача - 1",
                "Описание подзадачи - 1, эпической задачи - 1", epicId);
        int subtaskId = manager.addSubtask(subtask);
        final Subtask savedSubtask = manager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Сабтаск не найдена.");
        assertEquals(subtask, savedSubtask, "Сабтаски не совпадают.");

        final int savedEpicId = savedSubtask.getEpicId();

        assertEquals(subtask.getEpicId(), savedEpicId, "Епики у сабтасок не совпадают.");

        final List<Subtask> subtasks = manager.getAllSubtasks();

        assertNotNull(subtasks, "Сабтаски не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество Сабтасков.");
        assertEquals(subtask, subtasks.get(0), "Сабтаски не совпадают.");
    }

    @Test
    void updateEpicTest() {
        int epicId = manager.addEpic(epic);
        final Epic savedEpic = manager.getEpicById(epicId);
        Epic epic2 = new Epic(epic.getId(), "Эпическая задача - 2", "Ставим вместо эпической задачи - 1");
        manager.updateEpic(epic2);

        assertNotNull(savedEpic, "Эпик не найдена.");
        assertEquals(epic2, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = manager.getAllEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic2, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void updateSubtaskAndEpicTest() {
        int epicId = manager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача - 1",
                "Описание подзадачи - 1, эпической задачи - 1", epicId);
        int subtaskId = manager.addSubtask(subtask);
        final Subtask savedSubtask = manager.getSubtaskById(subtaskId);

        savedSubtask.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask);

        assertNotNull(savedSubtask, "Сабтаск не найдена.");
        assertEquals(subtask, savedSubtask, "Сабтаски не совпадают.");

        final List<Subtask> subtasks = manager.getAllSubtasks();

        assertNotNull(subtasks, "Сабтаски на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество сабтаск.");
        assertEquals(subtask, subtasks.get(0), "Сабтаски не совпадают.");
        assertEquals(manager.getEpicById(epicId).getStatus(), savedSubtask.getStatus(), "Статусы сабтасков не совпадают");
    }

    @Test
    void deleteEpicTest() {
        manager.addEpic(epic);
        manager.deleteEpicById(epic.getId());

        assertTrue(manager.getAllEpics().isEmpty(), "Эпик не удалился");
        assertEquals(0, manager.getAllEpics().size(), "Эпик не удалился");
    }

    @Test
    void deleteAllEpicsTest() {
        manager.addEpic(epic);
        manager.addEpic(epic);
        manager.deleteEpics();

        assertTrue(manager.getAllEpics().isEmpty(), "Эпики не удалились");
        assertEquals(0, manager.getAllEpics().size(), "Эпики не удалились");
    }

    @Test
    void deleteSubtaskTest() {
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача - 1",
                "Описание подзадачи - 1, эпической задачи - 1", epic.getId());
        int subtaskId = manager.addSubtask(subtask);
        manager.deleteSubtaskById(subtaskId);

        assertTrue(manager.getAllSubtasks().isEmpty(), "Сабтаск не удалился");
        assertEquals(0, manager.getAllSubtasks().size(), "Сабтаск не удалился");
    }

    @Test
    void deleteAllSubtaskTest() {
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача - 1",
                "Описание подзадачи - 1, эпической задачи - 1", epic.getId());
        manager.addSubtask(subtask);
        manager.addSubtask(subtask);
        manager.deleteSubtasks();

        assertTrue(manager.getAllSubtasks().isEmpty(), "Сабтаски не удалилися");
        assertEquals(0, manager.getAllSubtasks().size(), "Сабтаски не удалилися");
    }

    @Test
    void updateStatusEpicTest() {
        final int epicId = manager.addEpic(epic);

        assertEquals(TaskStatus.NEW, epic.getStatus(), "Неверный статус NEW");

        Subtask subtask = new Subtask("Подзадача - 1",
                "Описание подзадачи - 1, эпической задачи - 1", epic.getId());
        manager.addSubtask(subtask);
        subtask.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask);

        assertEquals(TaskStatus.DONE, epic.getStatus(), "Неверный статус DONE");

        Subtask subtask2 = new Subtask("Подзадача - 1",
                "Описание подзадачи - 1, эпической задачи - 1", epic.getId());
        manager.addSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Неверный статус IN_PROGRESS");
    }
}
