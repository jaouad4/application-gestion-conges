package application.dao;

import application.model.VacationType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacationTypeDAO {
    public List<VacationType> getAllVacationTypes() throws SQLException {
        List<VacationType> types = new ArrayList<>();
        String sql = "SELECT * FROM vacation_types";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                VacationType type = new VacationType(
                        rs.getInt("id"),
                        rs.getString("type")
                );
                types.add(type);
            }
        }
        return types;
    }
}