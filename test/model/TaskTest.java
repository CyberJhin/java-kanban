package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    // Проверка того, что задачи с одинаковыми ID считаются равными
    @Test
    void testTaskEqualityById() {
        Task task1 = new Task("Просто задача - 1", "Описание простой задачи - 1");
        Task task2 = new Task("Просто задача - 2", "Описание простой задачи - 1");
        task2.setId(task1.getId()); // Устанавливаем тот же ID для второй задачи

        assertEquals(task1.getId(), task2.getId(), "ID задач не совпадают.");
        assertEquals(task1, task2, "Задачи с одинаковыми ID должны быть равны.");
    }
}