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
        // Step 1: Create and add the Epic to ensure it gets a unique ID
        final int epicId = manager.addEpic(epic);

        // Step 2: Create a Subtask that references the Epic's ID correctly
        Subtask subtask = new Subtask("Подзадача - 1",
                "Описание подзадачи - 1, эпической задачи - 1", epicId);

        // Step 3: Add the Subtask to the manager, which will handle ID generation
        int subtaskId = manager.addSubtask(subtask);
        final Subtask savedSubtask = manager.getSubtaskById(subtaskId);

        // Assertions to verify correct addition
        assertNotNull(savedSubtask, "Сабтаск не найдена.");
        assertEquals(subtask, savedSubtask, "Сабтаски не совпадают.");

        final int savedEpicId = savedSubtask.getEpicId();
        assertEquals(epicId, savedEpicId, "Епики у сабтасок не совпадают.");

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
                "Описание подзадачи - 1, эпической задачи - 1", epicId);

        manager.addSubtask(subtask);
        subtask.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask);

        assertEquals(TaskStatus.DONE, epic.getStatus(), "Неверный статус DONE");

        Subtask subtask2 = new Subtask("Подзадача - 1",
                "Описание подзадачи - 1, эпической задачи - 1", epic.getId());
        manager.addSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Неверный статус IN_PROGRESS");
    }
    @Test
    void testTaskEqualityById() {
        Task task1 = new Task("Задача 1", "Описание 1");
        task1.setId(1);
        Task task2 = new Task("Задача 2", "Описание 2");
        task2.setId(1);

        assertEquals(task1, task2, "Задачи с одинаковым ID должны быть равны");
    }

    @Test
    void testEpicEqualityById() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        epic1.setId(2);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        epic2.setId(2);

        assertEquals(epic1, epic2, "Эпики с одинаковым ID должны быть равны");
    }

    @Test
    void testSubtaskEqualityById() {
        Epic epic = new Epic("Эпик для подзадач", "Описание эпика");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        subtask1.setId(3);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic.getId());
        subtask2.setId(3);

        assertEquals(subtask1, subtask2, "Подзадачи с одинаковым ID должны быть равны");
    }

    @Test
    void testEpicCannotContainItself() {
        Epic epic = new Epic("Эпик с самопривязкой", "Описание эпика");
        int epicId = manager.addEpic(epic);

        // Попытка добавить эпик как подзадачу самого себя, что должно привести к исключению
        assertThrows(IllegalArgumentException.class, () -> {
            epic.addSubtask(new Subtask(epicId, "Подзадача", "Описание", epicId)); // намеренно ошибка
        }, "Эпик не может содержать себя как подзадачу");
    }

    @Test
    void testSubtaskCannotBeEpicOfItself() {
        // Создаем эпик
        Epic epic = new Epic("Эпик с самоссылкой", "Описание эпика");
        int epicId = manager.addEpic(epic);

        // Попытка создать подзадачу с ID своего эпика, что не должно быть разрешено
        assertThrows(IllegalArgumentException.class, () -> {
            Subtask subtask = new Subtask(epicId, "Подзадача с самоссылкой", "Описание", TaskStatus.NEW, epicId); // самоссылка
            manager.addSubtask(subtask);
        }, "Подзадача не должна иметь себя как эпик");
    }

    @Test
    void testManagerInitialization() {
        assertNotNull(manager.getAllTasks(), "Менеджер должен инициализировать список задач");
        assertNotNull(manager.getAllEpics(), "Менеджер должен инициализировать список эпиков");
        assertNotNull(manager.getAllSubtasks(), "Менеджер должен инициализировать список подзадач");
    }

    @Test
    void testManagerMethodsAfterInitialization() {
        assertTrue(manager.getAllTasks().isEmpty(), "Список задач должен быть пустым после инициализации");
        assertTrue(manager.getAllEpics().isEmpty(), "Список эпиков должен быть пустым после инициализации");
        assertTrue(manager.getAllSubtasks().isEmpty(), "Список подзадач должен быть пустым после инициализации");
    }

    @Test
    void testTaskUnchangedAfterAddition() {
        Task originalTask = new Task("Оригинальная задача", "Оригинальное описание");
        int taskId = manager.addTask(originalTask);
        Task retrievedTask = manager.getTaskById(taskId);

        assertEquals(originalTask.getName(), retrievedTask.getName(), "Имя задачи не должно изменяться");
        assertEquals(originalTask.getDescription(), retrievedTask.getDescription(), "Описание задачи не должно изменяться");
        assertEquals(originalTask.getStatus(), retrievedTask.getStatus(), "Статус задачи не должен изменяться");
    }

    @Test
    void testHistoryManagerStoresPreviousVersionOfTask() {
        int taskId = manager.addTask(task);
        Task task = manager.getTaskById(taskId);

        List<Task> history = manager.getHistory();
        assertTrue(history.contains(task), "Предыдущая версия задачи должна быть сохранена в истории");
    }

    @Test
    void testCustomAndGeneratedIdConflict() {
        Task taskWithCustomId = new Task("Задача с пользовательским ID", "Описание");
        taskWithCustomId.setId(5);
        manager.addTask(taskWithCustomId);

        Task newTask = new Task("Новая задача", "Новое описание");
        int generatedId = manager.addTask(newTask);

        assertNotEquals(5, generatedId, "Сгенерированный ID не должен конфликтовать с пользовательским ID");
    }

    @Test
    void testCustomIdFunctionality() {
        Task taskWithCustomId = new Task("Задача с пользовательским ID", "Описание");
        taskWithCustomId.setId(999); // установка пользовательского ID

        manager.addTask(taskWithCustomId);
        Task retrievedTask = manager.getTaskById(999);

        assertNotNull(retrievedTask, "Задача с пользовательским ID должна быть извлечена по ID");
        assertEquals(999, retrievedTask.getId(), "Извлеченная задача должна иметь установленный пользовательский ID");
    }

}
