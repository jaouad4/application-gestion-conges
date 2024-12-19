package application.dao;

import application.model.VacationRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacationRequestDAO {
    public List<VacationRequest> getAllRequests() throws SQLException {
        List<VacationRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM vacation_requests";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                VacationRequest request = new VacationRequest(
                        rs.getInt("id"),
                        rs.getInt("employee_id"),
                        rs.getInt("vacation_type_id"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate()
                );
                requests.add(request);
            }
        }
        return requests;
    }

    public void addVacationRequest(VacationRequest request) throws SQLException {
        String sql = "INSERT INTO vacation_requests (employee_id, vacation_type_id, start_date, end_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, request.getEmployeeId());
            pstmt.setInt(2, request.getVacationTypeId());
            pstmt.setDate(3, Date.valueOf(request.getStartDate()));
            pstmt.setDate(4, Date.valueOf(request.getEndDate()));
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    request.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void updateVacationRequest(VacationRequest request) throws SQLException {
        String sql = "UPDATE vacation_requests SET employee_id=?, vacation_type_id=?, start_date=?, end_date=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, request.getEmployeeId());
            pstmt.setInt(2, request.getVacationTypeId());
            pstmt.setDate(3, Date.valueOf(request.getStartDate()));
            pstmt.setDate(4, Date.valueOf(request.getEndDate()));
            pstmt.setInt(5, request.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteVacationRequest(int id) throws SQLException {
        String sql = "DELETE FROM vacation_requests WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}