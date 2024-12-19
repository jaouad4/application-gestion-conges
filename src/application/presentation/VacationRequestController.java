package application.presentation;

import application.metier.VacationRequestService;
import application.model.VacationRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class VacationRequestController {
    @FXML private TableView<VacationRequest> requestTable;
    @FXML private TableColumn<VacationRequest, Integer> idColumn;
    @FXML private TableColumn<VacationRequest, Integer> employeeColumn;
    @FXML private TableColumn<VacationRequest, Integer> typeColumn;
    @FXML private TableColumn<VacationRequest, LocalDate> startDateColumn;
    @FXML private TableColumn<VacationRequest, LocalDate> endDateColumn;

    @FXML private ComboBox<String> employeeCombo;
    @FXML private ComboBox<String> typeCombo;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;

    private VacationRequestService service;
    private ObservableList<VacationRequest> requestList;

    @FXML
    public void initialize() {
        service = new VacationRequestService();

        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("vacationTypeId"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        loadData();
        initializeComboBoxes();

        requestTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> fillFields(newValue));
    }

    private void loadData() {
        try {
            requestList = FXCollections.observableArrayList(service.getAllRequests());
            requestTable.setItems(requestList);
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les données : " + e.getMessage());
        }
    }

    private void initializeComboBoxes() {
        employeeCombo.setItems(FXCollections.observableArrayList("1", "2", "3")); // Use IDs
        typeCombo.setItems(FXCollections.observableArrayList("1", "2", "3")); // Use IDs
    }

    private void fillFields(VacationRequest request) {
        if (request != null) {
            employeeCombo.getSelectionModel().select(String.valueOf(request.getEmployeeId()));
            typeCombo.getSelectionModel().select(String.valueOf(request.getVacationTypeId()));
            startDatePicker.setValue(request.getStartDate());
            endDatePicker.setValue(request.getEndDate());
        }
    }

    @FXML
    private void handleNewRequest() {
        try {
            VacationRequest request = createVacationRequest();
            service.addRequest(request);
            loadData();
            clearFields();
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ajouter la demande : " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateRequest() {
        VacationRequest selectedRequest = requestTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            selectedRequest = createVacationRequest();
            try {
                service.updateRequest(selectedRequest);
                loadData();
                clearFields();
            } catch (Exception e) {
                showAlert("Erreur", "Impossible de modifier la demande : " + e.getMessage());
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner une demande à modifier.");
        }
    }

    @FXML
    private void handleDeleteRequest() {
        VacationRequest selectedRequest = requestTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir supprimer cette demande?");
            alert.setHeaderText("Confirmation");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    service.deleteRequest(selectedRequest.getId());
                    loadData();
                    clearFields();
                } catch (SQLException e) {
                    showAlert("Erreur", "Impossible de supprimer la demande : " + e.getMessage());
                }
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner une demande à supprimer.");
        }
    }

    private VacationRequest createVacationRequest() {
        VacationRequest request = new VacationRequest();
        request.setEmployeeId(Integer.parseInt(employeeCombo.getSelectionModel().getSelectedItem())); 
        request.setVacationTypeId(Integer.parseInt(typeCombo.getSelectionModel().getSelectedItem()));
        request.setStartDate(startDatePicker.getValue());
        request.setEndDate(endDatePicker.getValue());
        return request;
    }

    private void clearFields() {
        employeeCombo.getSelectionModel().clearSelection();
        typeCombo.getSelectionModel().clearSelection();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        requestTable.getSelectionModel().clearSelection(); // Clear selection
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}