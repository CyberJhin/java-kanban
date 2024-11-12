package service;

import java.util.List;

import model.*;



 public interface TaskManager {

    void updateTask(Task task);

     void updateEpic(Epic epic);

     void updateSubtask(Subtask subtask);

     int addTask(Task task);

     int addEpic(Epic epic);

     int addSubtask(Subtask subtask);

     Task getTaskById(int id);

     Epic getEpicById(int id);

     Subtask getSubtaskById(int id);

     List<Task> getAllTasks();

     List<Subtask> getAllSubtasks();

     List<Epic> getAllEpics();

     void deleteTaskById(int id);

     void deleteEpicById(int id);

     void deleteSubtaskById(int id);

     void deleteEpics();

     void deleteTasks();

     void deleteSubtasks();

     List<Epic> getEpics();

     List<Task> getTasks();

     List<Subtask> getSubtasks();

     List<Task> getHistory();
}
