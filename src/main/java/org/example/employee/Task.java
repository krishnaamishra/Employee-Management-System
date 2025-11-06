package org.example.employee;

public class Task {
    private int taskId;
    private String taskName;
    private String description;
    private String status;
    private String startDate;
    private int employeeId;

    // Constructor
    public Task(int taskId, String taskName, String description, String status, String startDate, int employeeId) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
        this.status = status;
        this.startDate = startDate;
        this.employeeId = employeeId;
    }

    // Getters
    public int getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getStartDate() {
        return startDate;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    // Optional: Setters (if needed for editing)
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

}
