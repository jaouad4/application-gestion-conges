package application.presentation;

import application.metier.VacationRequestService;
import application.model.Employee;
import application.model.VacationRequest;
import application.model.VacationRequestDTO;
import application.model.VacationType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VacationRequestController {
    @FXML private TableView<VacationRequestDTO> requestTable;
    @FXML private TableColumn<VacationRequestDTO, Integer> idColumn;
    @FXML private TableColumn<VacationRequestDTO, String> employeeColumn;
    @FXML private TableColumn<VacationRequestDTO, String> typeColumn;
    @FXML private TableColumn<VacationRequestDTO, LocalDate> startDateColumn;
    @FXML private TableColumn<VacationRequestDTO, LocalDate> endDateColumn;
    @FXML private ComboBox<String> employeeCombo;
    @FXML private ComboBox<String> typeCombo;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;

    private ObservableList<VacationRequestDTO> requestList;
    private VacationRequestService service;
    private List<Employee> employees;
    private List<VacationType> vacationTypes;

    @FXML
    public void initialize() {
        service = new VacationRequestService();

        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("vacationType"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        loadData();
        initializeComboBoxes();

        requestTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> fillFields(newValue));
    }

    private void loadData() {
        try {
            List<VacationRequest> requests = service.getAllRequests();
            List<Employee> employees = service.getAllEmployees();
            List<VacationType> types = service.getAllVacationTypes();

            List<VacationRequestDTO> dtos = requests.stream()
                    .map(request -> {
                        String empName = employees.stream()
                                .filter(e -> e.getId() == request.getEmployeeId())
                                .map(Employee::getName)
                                .findFirst()
                                .orElse("Inconnu");

                        String typeName = types.stream()
                                .filter(t -> t.getId() == request.getVacationTypeId())
                                .map(VacationType::getType)
                                .findFirst()
                                .orElse("Inconnu");

                        return new VacationRequestDTO(request, empName, typeName);
                    })
                    .collect(Collectors.toList());

            requestList = FXCollections.observableArrayList(dtos);
            requestTable.setItems(requestList);
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les données : " + e.getMessage());
        }
    }

    private void initializeComboBoxes() {
        try {
            employees = service.getAllEmployees();
            vacationTypes = service.getAllVacationTypes();

            employeeCombo.setItems(FXCollections.observableArrayList(
                    employees.stream()
                            .map(e -> e.getId() + " - " + e.getName())
                            .collect(Collectors.toList())
            ));

            typeCombo.setItems(FXCollections.observableArrayList(
                    vacationTypes.stream()
                            .map(VacationType::getType)
                            .collect(Collectors.toList())
            ));
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les données : " + e.getMessage());
        }
    }

    private void fillFields(VacationRequestDTO dto) {
        if (dto != null) {
            String employeeStr = employees.stream()
                    .filter(e -> e.getId() == dto.getEmployeeId())
                    .map(e -> e.getId() + " - " + e.getName())
                    .findFirst()
                    .orElse("");
            employeeCombo.setValue(employeeStr);

            typeCombo.setValue(dto.getVacationType());
            startDatePicker.setValue(dto.getStartDate());
            endDatePicker.setValue(dto.getEndDate());
        }
    }

    @FXML
    private void handleNewRequest() {
        try {
            validateFields();
            VacationRequest request = createVacationRequest();
            service.addRequest(request);
            loadData();
            clearFields();
        } catch (IllegalArgumentException | SQLException e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    @FXML
    private void handleUpdateRequest() {
        VacationRequestDTO selectedRequest = requestTable.getSelectionModel().getSelectedItem();
        if (selectedRequest == null) {
            showAlert("Erreur", "Veuillez sélectionner une demande à modifier.");
            return;
        }

        try {
            validateFields();
            VacationRequest request = createVacationRequest();
            request.setId(selectedRequest.getId());
            service.updateRequest(request);
            loadData();
            clearFields();
        } catch (IllegalArgumentException | SQLException e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteRequest() {
        VacationRequestDTO selectedRequest = requestTable.getSelectionModel().getSelectedItem();
        if (selectedRequest == null) {
            showAlert("Erreur", "Veuillez sélectionner une demande à supprimer.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la demande de congé");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette demande ?");

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
    }

    private VacationRequest createVacationRequest() {
        VacationRequest request = new VacationRequest();

        // Extraire l'ID de l'employé du format "id - name"
        String employeeSelection = employeeCombo.getValue();
        int employeeId = Integer.parseInt(employeeSelection.split(" - ")[0]);
        request.setEmployeeId(employeeId);

        // Trouver l'ID du type de congé sélectionné
        String typeSelection = typeCombo.getValue();
        int typeId = vacationTypes.stream()
                .filter(t -> t.getType().equals(typeSelection))
                .map(VacationType::getId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Type de congé invalide"));
        request.setVacationTypeId(typeId);

        request.setStartDate(startDatePicker.getValue());
        request.setEndDate(endDatePicker.getValue());

        validateDates(request.getStartDate(), request.getEndDate());

        return request;
    }

    private void validateFields() {
        if (employeeCombo.getValue() == null || employeeCombo.getValue().isEmpty()) {
            throw new IllegalArgumentException("Veuillez sélectionner un employé");
        }
        if (typeCombo.getValue() == null || typeCombo.getValue().isEmpty()) {
            throw new IllegalArgumentException("Veuillez sélectionner un type de congé");
        }
        if (startDatePicker.getValue() == null) {
            throw new IllegalArgumentException("Veuillez sélectionner une date de début");
        }
        if (endDatePicker.getValue() == null) {
            throw new IllegalArgumentException("Veuillez sélectionner une date de fin");
        }
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La date de début doit être antérieure à la date de fin");
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La date de début ne peut pas être dans le passé");
        }
    }

    private void clearFields() {
        employeeCombo.setValue(null);
        typeCombo.setValue(null);
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        requestTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}