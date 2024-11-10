import model.*;
import service.*;


public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager manager = new InMemoryTaskManager();

        // Тесты на добавление и получение задач
        testAddAndGetTasks(manager);

        // Тесты на обновление задач
        testUpdateTasks(manager);

        // Тесты на удаление задач
        testDeleteTasks(manager);

        // Тесты на обновление статуса эпика
        testEpicStatus(manager);

        // Тесты на очистку задач
        testClearAllTasks(manager);
    }

    public static void testAddAndGetTasks(TaskManager manager) {
        System.out.println("Тестирование добавления и получения задач:");

        // Добавление и проверка Task
        Task task = new Task("Test Task", "Description");
        int taskId = manager.addTask(task);
        Task retrievedTask = manager.getTaskById(taskId);

        if (retrievedTask != null && retrievedTask.getId() == taskId) {
            System.out.println("Task добавлен и получен успешно: " + retrievedTask);
        } else {
            System.out.println("Ошибка при добавлении и получении Task.");
        }

        // Добавление и проверка Epic
        Epic epic = new Epic("Test Epic", "Epic Description");
        int epicId = manager.addEpic(epic);
        Epic retrievedEpic = manager.getEpicById(epicId);

        if (retrievedEpic != null && retrievedEpic.getId() == epicId) {
            System.out.println("Epic добавлен и получен успешно: " + retrievedEpic);
        } else {
            System.out.println("Ошибка при добавлении и получении Epic.");
        }

        // Добавление и проверка Subtask
        Subtask subtask = new Subtask("Test Subtask", "Subtask Description", epicId);
        int subtaskId = manager.addSubtask(subtask);
        Subtask retrievedSubtask = manager.getSubtaskById(subtaskId);

        if (retrievedSubtask != null && retrievedSubtask.getId() == subtaskId && retrievedSubtask.getEpicId() == epicId) {
            System.out.println("Subtask добавлен и получен успешно: " + retrievedSubtask);
        } else {
            System.out.println("Ошибка при добавлении и получении Subtask.");
        }
    }

    public static void testUpdateTasks(TaskManager manager) {
        System.out.println("\nТестирование обновления задач:");

        // Обновление Task
        Task task = new Task("Initial Task", "Description");
        int taskId = manager.addTask(task);
        task.setStatus(TaskStatus.DONE);
        task.setName("Updated Task");
        manager.updateTask(task);

        Task updatedTask = manager.getTaskById(taskId);
        if (updatedTask.getStatus() == TaskStatus.DONE && updatedTask.getName().equals("Updated Task")) {
            System.out.println("Task обновлен успешно: " + updatedTask);
        } else {
            System.out.println("Ошибка при обновлении Task.");
        }

        // Обновление Epic
        Epic epic = new Epic("Initial Epic", "Description");
        int epicId = manager.addEpic(epic);
        epic.setName("Updated Epic");
        manager.updateEpic(epic);

        Epic updatedEpic = manager.getEpicById(epicId);
        if (updatedEpic.getName().equals("Updated Epic")) {
            System.out.println("Epic обновлен успешно: " + updatedEpic);
        } else {
            System.out.println("Ошибка при обновлении Epic.");
        }

        // Обновление Subtask
        Subtask subtask = new Subtask("Initial Subtask", "Description", epicId);
        int subtaskId = manager.addSubtask(subtask);
        subtask.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask);

        Subtask updatedSubtask = manager.getSubtaskById(subtaskId);
        if (updatedSubtask.getStatus() == TaskStatus.IN_PROGRESS) {
            System.out.println("Subtask обновлен успешно: " + updatedSubtask);
        } else {
            System.out.println("Ошибка при обновлении Subtask.");
        }
    }

    public static void testDeleteTasks(TaskManager manager) {
        System.out.println("\nТестирование удаления задач:");

        // Удаление Task
        Task task = new Task("Task to delete", "Description");
        int taskId = manager.addTask(task);
        manager.deleteTaskById(taskId);

        if (manager.getTaskById(taskId) == null) {
            System.out.println("Task удален успешно!");
        } else {
            System.out.println("Ошибка при удалении Task.");
        }

        Epic epic = new Epic("Epic to delete", "Description");
        int epicId = manager.addEpic(epic);

        // Удаление Subtask
        Subtask subtask = new Subtask("Subtask to delete", "Description", epicId);
        int subtaskId = manager.addSubtask(subtask);
        manager.deleteSubtaskById(subtaskId);

        if (manager.getSubtaskById(subtaskId) == null) {
            System.out.println("Subtask удален успешно!");
        } else {
            System.out.println("Ошибка при удалении Subtask.");
        }

        // Удаление Epic

        manager.deleteEpicById(epicId);

        if (manager.getEpicById(epicId) == null) {
            System.out.println("Epic удален успешно!");
        } else {
            System.out.println("Ошибка при удалении Epic.");
        }

    }

    public static void testEpicStatus(TaskManager manager) {
        System.out.println("\nТестирование статуса Epic:");

        Epic epic = new Epic("Epic for Status Check", "Description");
        int epicId = manager.addEpic(epic);

        // Создаем подзадачи и устанавливаем статус через setStatus
        Subtask subtask1 = new Subtask("Subtask 1", "Description", epicId);
        subtask1.setStatus(TaskStatus.NEW);
        Subtask subtask2 = new Subtask("Subtask 2", "Description", epicId);
        subtask2.setStatus(TaskStatus.DONE);

        // Добавляем подзадачи в TaskManager
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        // Проверяем статус эпика
        Epic retrievedEpic = manager.getEpicById(epicId);
        if (retrievedEpic.getStatus() == TaskStatus.IN_PROGRESS) {
            System.out.println("Статус Epic обновляется корректно при наличии подзадач со статусами NEW и DONE: " + retrievedEpic);
        } else {
            System.out.println("Ошибка в логике обновления статуса Epic.");
        }
    }


    public static void testClearAllTasks(TaskManager manager) {
        System.out.println("\nТестирование очистки всех задач:");

        // Добавляем Epic
        int epicId = manager.addEpic(new Epic("Epic to clear", "Description"));

        // Используем реальный epicId для подзадачи
        manager.addSubtask(new Subtask("Subtask to clear", "Description", epicId));

        // Удаляем все задачи, подзадачи и эпики
        manager.deleteTasks();
        manager.deleteSubtasks();
        manager.deleteEpics();

        // Проверка, что все списки очищены
        if (manager.getTasks().isEmpty() && manager.getEpics().isEmpty() && manager.getSubtasks().isEmpty()) {
            System.out.println("Все задачи успешно очищены!");
        } else {
            System.out.println("Ошибка при очистке задач.");
        }
    }
}
