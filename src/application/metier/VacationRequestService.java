package application.metier;

import application.dao.VacationRequestDAO;
import application.model.VacationRequest;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class VacationRequestService {
    private final VacationRequestDAO requestDAO;

    public VacationRequestService() {
        this.requestDAO = new VacationRequestDAO();
    }

    public void addRequest(VacationRequest request) throws SQLException {
        validateRequest(request);
        requestDAO.add(request);
    }

    public List<VacationRequest> getAllRequests() throws SQLException {
        return requestDAO.getAll();
    }

    public void updateRequest(VacationRequest request) throws SQLException {
        validateRequest(request);
        requestDAO.update(request);
    }

    public void deleteRequest(int id) throws SQLException {
        requestDAO.delete(id);
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