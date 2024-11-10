package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int SIZE_HISTORY = 10;
    private final List<Task> history = new ArrayList<>(SIZE_HISTORY);

    private void ensureSizeLimit() {
        if (history.size() >= SIZE_HISTORY) {
            history.remove(0); // удаляем первый элемент, если достигнут лимит
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }

    @Override
    public void addInHistory(Task task) {
        if (task != null) {
            ensureSizeLimit(); // ограничиваем размер истории
            history.add(task);  // добавляем задачу в конец списка
        }
    }
}
