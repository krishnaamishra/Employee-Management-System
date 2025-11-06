package org.example.employee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HelloController {

    @FXML
    private Button loginbtn;

    @FXML
    private AnchorPane mainform;

    @FXML
    private PasswordField passwd;

    @FXML
    private TextField username;
@FXML
    private Stage stage;

    @FXML
    private Scene scene;
    @FXML
    private Parent root;

@FXML
private TextField empusername;

    @FXML
    private TextField emppass;

    @FXML
    private void navigateToEmployee(ActionEvent event,String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/employee/employeepanel.fxml"));
            Parent root = loader.load();

//            // Pass the username to the DashboardController
            EmployeePanel emppanel = loader.getController();
            int eid=DatabaseUtil.getempid(username);

            emppanel.setUsername(username,eid);

            // Set the new scene and show the dashboard
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Employee Panel");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            alerts("Navigation Error", "Unable to load the Employee Panel. Please try again.");
        }
    }

    @FXML
    private void loginemp(ActionEvent event){

        String username=empusername.getText();
        String pass=emppass.getText();

        boolean isvalidemp=DatabaseUtil.validateemp(username,pass);

        if(isvalidemp){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Successful");
            alert.setHeaderText(null);
            alert.setContentText("Welcome, " + username );
            alert.showAndWait();
            navigateToEmployee(event,username);
        }else {
            alerts("Login Failed", "Invalid username or password.");
        }


    }



    @FXML
    private void loginadmin(ActionEvent event) {
        String usern = username.getText();
        String passw = passwd.getText();

        if (usern.isEmpty() || passw.isEmpty()) {
            alerts("All fields are required", "Please provide both username and password.");
            return;
        }
        boolean isValidUser = DatabaseUtil.validateUser(usern, passw);
//        List<Employee> employees = DatabaseUtil.fetchEmployees();
        if (isValidUser) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Successful");
            alert.setHeaderText(null);
            alert.setContentText("Welcome, " + usern + "!");
            alert.showAndWait();

            navigateToDashboard(event, usern);
        } else {
            alerts("Login Failed", "Invalid username or password.");
        }
    }

    @FXML
    private void navigateToDashboard(ActionEvent event, String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/employee/employeedashboard.fxml"));
            Parent root = loader.load();

            // Pass the username to the DashboardController
            dashboard dashboardController = loader.getController();
            dashboardController.setName(username);

            // Set the new scene and show the dashboard
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            alerts("Navigation Error", "Unable to load the dashboard. Please try again.");
        }
    }

    private void alerts(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
@FXML
    public void AboutDevoloper(ActionEvent event)throws Exception {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/employee/aboutdevolopers.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("About Devolopers");
        stage.show();

    } catch (Exception e) {
e.printStackTrace();
    }
}
    @FXML
    public void aboutems(ActionEvent event)throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/employee/aboutems.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("About E.M.S");
        stage.show();

    }



}
