package org.example.employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class EmployeePanel {

    @FXML private TableView<Task> emptasks;
    @FXML private TableColumn<Task, Integer> taskid;
    @FXML private TableColumn<Task, String> tasktitle;
    @FXML private TableColumn<Task, String> taskstatus;
    @FXML private TableColumn<Task, String> taskfrom;
    @FXML private TableColumn<Task, String> taskdesc;

    @FXML private Label usernamefield;
    @FXML private Label empid;
    @FXML private ImageView imgdisplay;
    @FXML private AnchorPane emptaskupdate;
    @FXML private Button markattendace;


    @FXML private TableView<Leave> leaverequestempform;
    @FXML private TableColumn<Leave, Integer> leave_form_lid;
    @FXML private TableColumn<Leave, String> leave_form_ltype;
    @FXML private TableColumn<Leave, String> leave_form_lfrom;
    @FXML private TableColumn<Leave, String> leave_form_lto;
    @FXML private TableColumn<Leave, String> leave_form_lstatus;
    @FXML private TableColumn<Leave, String> leave_form_lRemarks;

    @FXML private TextField taskstatustexfield;
    @FXML private ComboBox<String> statuscombo;

    private int eid = 0;

    public void setUsername(String username, int eid) {
        usernamefield.setText(username);
        empid.setText(String.valueOf(eid));
        this.eid = eid;
        refresh();
    }

    public void refresh() {
        initialize();
    }

    @FXML
    public void initialize() {
        // Load image from database
        Image image = DatabaseUtil.getImageFromDatabase(eid);
        if (image != null) {
            this.imgdisplay.setImage(image);
        }

        checkAttendanceStatus();


        // Bind task columns
        taskid.setCellValueFactory(new PropertyValueFactory<>("taskId"));
        tasktitle.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        taskdesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        taskstatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        taskfrom.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        // Leave request columns
        leave_form_lid.setCellValueFactory(new PropertyValueFactory<>("lid"));
        leave_form_lstatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        leave_form_lto.setCellValueFactory(new PropertyValueFactory<>("to"));
        leave_form_lfrom.setCellValueFactory(new PropertyValueFactory<>("from"));
        leave_form_lRemarks.setCellValueFactory(new PropertyValueFactory<>("leaveReason"));
        leave_form_ltype.setCellValueFactory(new PropertyValueFactory<>("ltype"));

        List<Leave> Leavelist = DatabaseUtil.getLeaveById(eid);
        ObservableList<Leave> Leavedata = FXCollections.observableArrayList(Leavelist);
        leaverequestempform.setItems(Leavedata);

        // Fetch tasks
        List<Task> taskList = DatabaseUtil.getTaskByIDemp(eid);
        ObservableList<Task> taskempdata = FXCollections.observableArrayList(taskList);
        emptasks.setItems(taskempdata);

        clear();
        statuscombo.getItems().setAll("In progress", "Completed");
        searchltype.getItems().setAll("Casual", "Compoff", "Earned Leave");
    }

    public void updatetaskstatus() {
        int taskid = 0;
        try {
            taskid = Integer.parseInt(taskstatustexfield.getText());
        } catch (Exception e) {
            DatabaseUtil.showAlert(Alert.AlertType.ERROR, "Failed", "Task Status Update Failed", "Please enter a valid Task ID.");
        }

        String taskstatus = statuscombo.getValue();
        DatabaseUtil.UpdateTAskbyemp(taskid, taskstatus);
        clear();
        refresh();
    }

    @FXML
    public void markattendacebtn() {
        // Step 1: Update attendance in the database
        DatabaseUtil.updateattendance(eid);

        // Step 2: Change the button text and appearance to show success
        markattendace.setText("Present ✅");

        markattendace.setStyle(
                "-fx-background-color: #28a745;" +  // green background
                        "-fx-text-fill: white;" +            // white text
                        "-fx-font-weight: bold;" +           // bold text
                        "-fx-background-radius: 10;" +       // rounded corners
                        "-fx-border-radius: 10;"             // rounded border
        );

        // Step 3: Disable it so user can't mark twice
        markattendace.setDisable(true);
    }

    private void checkAttendanceStatus() {
        boolean isPresentToday = DatabaseUtil.isEmployeePresentToday(eid);

        if (isPresentToday) {
            markattendace.setText("Present ✅");
            markattendace.setStyle(
                    "-fx-background-color: #28a745;" +  // green
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 10;" +
                            "-fx-border-radius: 10;"
            );
            markattendace.setDisable(true); // prevent double marking
        } else {
            markattendace.setText("Mark Attendance");
            markattendace.setStyle(
                    "-fx-background-color: #f4f4f4;" +  // default light gray
                            "-fx-text-fill: black;"
            );
            markattendace.setDisable(false);
        }
    }



    public void clear() {
        taskstatustexfield.setText("");
        statuscombo.getItems().clear();
    }

    @FXML
    public void logout(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/employee/hello-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Home");
        stage.show();
    }

    @FXML private Button taskbtn;
    @FXML private Button homebtn;
    @FXML private Button leaveRequestbtn;
    @FXML private AnchorPane leaverequestform;
    @FXML private AnchorPane homeform;
    @FXML private AnchorPane salaryRecordform;
    @FXML private Button salrecord;

    @FXML
    public void Switchforms(ActionEvent event) {
        if (event.getSource() == taskbtn) {
            salaryRecordform.setVisible(false);
            leaverequestform.setVisible(false);
            homeform.setVisible(false);
            emptaskupdate.setVisible(true);
        } else if (event.getSource() == leaveRequestbtn) {
            salaryRecordform.setVisible(false);
            homeform.setVisible(false);
            emptaskupdate.setVisible(false);
            leaverequestform.setVisible(true);
        } else if (event.getSource() == homebtn) {
            salaryRecordform.setVisible(false);
            emptaskupdate.setVisible(false);
            leaverequestform.setVisible(false);
            homeform.setVisible(true);
        } else if (event.getSource() == salrecord) {
            emptaskupdate.setVisible(false);
            leaverequestform.setVisible(false);
            homeform.setVisible(false);
            salaryRecordform.setVisible(true);
        }
    }

    @FXML private TextField searchreason;
    @FXML private ComboBox<String> searchltype;
    @FXML private DatePicker searchfrom;
    @FXML private DatePicker searchto;

    @FXML
    public void AddLeaveRequest() {
        String reason = searchreason.getText();
        String ltype = searchltype.getValue();
        String from = String.valueOf(searchfrom.getValue());
        String to = String.valueOf(searchto.getValue());
        DatabaseUtil.addleave(eid, ltype, from, to, reason);
        clearleave();
        refresh();
    }

    public void clearleave() {
        searchreason.setText("");
        searchltype.getItems().clear();
        searchto.setValue(null);
        searchfrom.setValue(null);
    }

    // Salary Report
    @FXML private Label salgross;
    @FXML private Label salhealth;
    @FXML private Label saltax;
    @FXML private Label saltravel;
    @FXML private Label salnet;

    @FXML
    public void salaryRecords() {
        String sql = "SELECT * FROM salary_record WHERE eid=?";
        float gross = 0, net = 0, health = 0, tax = 0, travel = 0;

        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, eid);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                gross = rs.getFloat("gross_pay");
                net = rs.getFloat("net_pay");
                health = rs.getFloat("health_insurance");
                travel = rs.getFloat("travel_allowance");
                tax = rs.getFloat("tax");
            }

            salgross.setText("Gross Pay : " + gross);
            salnet.setText("Net Pay : " + net);
            salhealth.setText("Health Insurance : " + health);
            saltravel.setText("Travel Allowance : " + travel);
            saltax.setText("Tax : " + tax);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clearsalrecord() {
        salgross.setText("");
        salnet.setText("");
        salhealth.setText("");
        saltravel.setText("");
        saltax.setText("");
    }

    // ========================= IMAGE UPDATE FEATURE =========================
    @FXML
    private void handleChangeImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select New Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            DatabaseUtil.updateEmployeeImage(eid, selectedFile.getAbsolutePath());
            Image newImg = new Image(selectedFile.toURI().toString());
            imgdisplay.setImage(newImg);
        }
    }
}
