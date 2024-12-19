package application.model;

import java.time.LocalDate;

public class VacationRequest {
    private int id;
    private int employeeId;
    private int vacationTypeId;
    private LocalDate startDate;
    private LocalDate endDate;

    public VacationRequest() {}

    public VacationRequest(int id, int employeeId, int vacationTypeId, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.vacationTypeId = vacationTypeId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public int getVacationTypeId() { return vacationTypeId; }
    public void setVacationTypeId(int vacationTypeId) { this.vacationTypeId = vacationTypeId; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}