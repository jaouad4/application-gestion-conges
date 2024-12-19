package application.model;

import java.time.LocalDate;

public class VacationRequestDTO {
    private int id;
    private int employeeId;
    private String employeeName;
    private String vacationType;
    private LocalDate startDate;
    private LocalDate endDate;

    public VacationRequestDTO(VacationRequest request, String employeeName, String vacationType) {
        this.id = request.getId();
        this.employeeId = request.getEmployeeId();
        this.employeeName = employeeName;
        this.vacationType = vacationType;
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
    }

    // Getters
    public int getId() { return id; }
    public int getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public String getVacationType() { return vacationType; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
}