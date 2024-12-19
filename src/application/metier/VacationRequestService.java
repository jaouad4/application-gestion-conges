package application.metier;

import application.dao.EmployeeDAO;
import application.dao.VacationRequestDAO;
import application.dao.VacationTypeDAO;
import application.model.Employee;
import application.model.VacationRequest;
import application.model.VacationType;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class VacationRequestService {
    private final VacationRequestDAO requestDAO;
    private final EmployeeDAO employeeDAO;
    private final VacationTypeDAO vacationTypeDAO;

    public VacationRequestService() {
        this.requestDAO = new VacationRequestDAO();
        this.employeeDAO = new EmployeeDAO();
        this.vacationTypeDAO = new VacationTypeDAO();
    }

    public void addRequest(VacationRequest request) throws SQLException {
        validateRequest(request);
        requestDAO.addVacationRequest(request);
    }

    public List<VacationRequest> getAllRequests() throws SQLException {
        return requestDAO.getAllRequests();
    }

    public List<Employee> getAllEmployees() throws SQLException {
        return employeeDAO.getAllEmployees();
    }

    public List<VacationType> getAllVacationTypes() throws SQLException {
        return vacationTypeDAO.getAllVacationTypes();
    }

    public void updateRequest(VacationRequest request) throws SQLException {
        validateRequest(request);
        requestDAO.updateVacationRequest(request);
    }

    public void deleteRequest(int id) throws SQLException {
        requestDAO.deleteVacationRequest(id);
    }

    private void validateRequest(VacationRequest request) {
        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new IllegalArgumentException("Les dates ne peuvent pas être vides");
        }
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("La date de début doit être antérieure à la date de fin");
        }
        if (request.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La date de début ne peut pas être dans le passé");
        }
    }
}