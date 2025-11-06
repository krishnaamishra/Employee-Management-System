package org.example.employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
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
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class dashboard {

    @FXML
    private Label uname;

    @FXML
    private AnchorPane add_emp_form;


    @FXML
    private AnchorPane home_form;

    @FXML
    private Button homebtn;
    @FXML
    private Button addempbtn;
    @FXML
    private Button salarybtn;

    @FXML
    private Button update_empbtn;

    @FXML
    private AnchorPane sal_form;

    @FXML
    private Label totalemplabel;

    @FXML
    private TableView<Employee> add_emptable;

    @FXML
    private TableView<Employee> salary_forms;

    @FXML
    private TableColumn<Employee, Integer> salary_col_eid;

    @FXML
    private TableColumn<Employee, String> salary_col_fname;

    @FXML
    private TableColumn<Employee, String> salary_col_lname;

    @FXML
    private TableColumn<Employee, String> salary_col_designation;

    @FXML
    private TableColumn<Employee, Integer> salary_col_salary;


    @FXML
    private TableColumn<Employee, Integer> add_eid;

    @FXML
    private TableColumn<Employee, String> empadd_fname;

    @FXML
    private TableColumn<Employee, String> add_lname;

    @FXML
    private TableColumn<Employee, String> add_gender;

    @FXML
    private TableColumn<Employee, String> add_contact;
    @FXML
    private TableColumn<Employee, String> add_mail;

    @FXML
    private TableColumn<Employee, String> add_designation;

    @FXML
    private TableColumn<Employee, String> add_doj;


    //add and update data

    @FXML
    private ComboBox<String> add_empgender;

    @FXML
    private ComboBox<String> add_empdesignation;


    @FXML
    private TextField add_fname;


    @FXML
    private TextField add_emplname;

    @FXML
    private TextField add_empcontact;

    @FXML
    private TextField add_empmail;

    @FXML
    private TextField add_empsalary;

    @FXML
    private AnchorPane Updaterecords;

    @FXML
    private DatePicker add_empdoj;

    @FXML
    private ImageView add_empimage;


    @FXML
    private Label presentemp;

    @FXML
    private Label absentemps;
    String paths;

    @FXML
    public void getdata() {

        try {

            int esal = 0;
            String dat = "";


            String fname = add_fname.getText();
            String lname = add_emplname.getText();
            String contact = add_empcontact.getText();
            String mail = add_empmail.getText();

            if (!add_doj.getText().isEmpty()) {
                dat = String.valueOf(add_empdoj.getValue());
            }
            String gender = add_empgender.getValue();
            String desig = add_empdesignation.getValue();

            if (!add_empsalary.getText().isEmpty()) {
                esal = Integer.parseInt(add_empsalary.getText());
            }


            if (fname.isEmpty() || lname.isEmpty() || contact.isEmpty() || desig.isEmpty() || add_empsalary.getText().isEmpty() || add_doj.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("All field Required");
                alert.setTitle("All fields Are required Please Check While Filling there is an issue.");
                alert.showAndWait();
            } else {
                if(isValidEmail(mail) ) {

                    DatabaseUtil.InsertAddData(fname, lname, contact, mail, dat, gender, desig, esal, paths);
                }else{
                    DatabaseUtil.showAlert(Alert.AlertType.ERROR,"Error in mail id","Mail Id is not Valid",mail+"");


                }

            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Enter Details in Correct format");
            alert.showAndWait();

        }
        clear();
        refresh();

    }


//    / Method to validate email
    public static boolean isValidEmail(String email) {
        // Define the email regex pattern
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Compile the regex
        Pattern pattern = Pattern.compile(emailRegex);

        // If email is null, return false
        if (email == null) {
            return false;
        }

        // Match the email with the regex
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public void importimage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");

        // Set file extension filters
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Load and display the image
            Image image = new Image(selectedFile.toURI().toString());
//            System.out.println(selectedFile.toURI().toString());
            paths = selectedFile.getAbsolutePath();

            add_empimage.setImage(image);


//            add_empimage.setFitWidth(300); // Adjust the width
//            add_empimage.setPreserveRatio(true); // Maintain aspect ratio
        }


    }

    @FXML
    private TableView<Task> task_form;

    @FXML
    private TableColumn<Task, Integer> task_id;

    @FXML
    private TableColumn<Task, String> task_title;
    @FXML
    private TableColumn<Task, String> task_description;
    @FXML
    private TableColumn<Task, String> task_status;
    @FXML
    private TableColumn<Task, String> task_date;
    @FXML
    private TableColumn<Task, Integer> task_empid;

    @FXML
    private TableView<Leave> leave_board;

    @FXML
    private TableColumn<Leave, Integer> leave_eid;
    @FXML
    private TableColumn<Leave, Integer> leave_lid;
    @FXML
    private TableColumn<Leave, String> leave_ltype;
    @FXML
    private TableColumn<Leave, String> leave_to;
    @FXML
    private TableColumn<Leave, String> leave_from;
    @FXML
    private TableColumn<Leave, String> leave_status;
    @FXML
    private TableColumn<Leave, String> leave_reason;
    @FXML
    private TableColumn<Leave, Void> leave_action;
    @FXML
    public void initialize() {
        int totalemp = DatabaseUtil.gettotal();
        totalemplabel.setText("" + totalemp);
        int presentemployee = DatabaseUtil.getPresentEmp();
        int absentemp = totalemp - presentemployee;
        presentemp.setText("" + presentemployee);
        absentemps.setText("" + absentemp);


        // Bind columns to Employee properties
        add_eid.setCellValueFactory(new PropertyValueFactory<>("eid"));
        empadd_fname.setCellValueFactory(new PropertyValueFactory<>("fname"));
        add_lname.setCellValueFactory(new PropertyValueFactory<>("lname"));
        add_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        add_contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        add_designation.setCellValueFactory(new PropertyValueFactory<>("designation"));
        add_doj.setCellValueFactory(new PropertyValueFactory<>("doj"));
        add_mail.setCellValueFactory(new PropertyValueFactory<>("mail"));

        salary_col_eid.setCellValueFactory(new PropertyValueFactory<>("eid"));
        salary_col_fname.setCellValueFactory(new PropertyValueFactory<>("fname"));
        salary_col_lname.setCellValueFactory(new PropertyValueFactory<>("lname"));
        salary_col_designation.setCellValueFactory(new PropertyValueFactory<>("designation"));
        salary_col_salary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        task_id.setCellValueFactory(new PropertyValueFactory<>("taskId"));
        task_title.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        task_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        task_date.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        task_empid.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        task_status.setCellValueFactory(new PropertyValueFactory<>("status"));


        // Fetch data and populate the TableView
        List<Employee> employees = DatabaseUtil.fetchEmployees();
        ObservableList<Employee> employeeData = FXCollections.observableArrayList(employees);
        add_emptable.setItems(employeeData);
        salary_forms.setItems(employeeData);

        List<Task> task = DatabaseUtil.getTask();
        ObservableList<Task> taskdata = FXCollections.observableArrayList(task);
        task_form.setItems(taskdata);

        add_empgender.getItems().addAll("Male", "Female", "Others");
        add_empdesignation.getItems().addAll("Software Developer", "Product Analyst", "Tester", "Quality Analyst", "Junior Architect");

        leave_actioncombo.getItems().addAll("Approved","Rejected");
        // leave data
        leave_eid.setCellValueFactory(new PropertyValueFactory<>("eid"));
        leave_lid.setCellValueFactory(new PropertyValueFactory<>("lid"));
        leave_ltype.setCellValueFactory(new PropertyValueFactory<>("ltype"));
        leave_from.setCellValueFactory(new PropertyValueFactory<>("from"));
        leave_to.setCellValueFactory(new PropertyValueFactory<>("to"));
        leave_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        leave_reason.setCellValueFactory(new PropertyValueFactory<>("leaveReason"));


        List<Leave> leave = DatabaseUtil.getLeave();
        ObservableList<Leave> leavedata = FXCollections.observableArrayList(leave);
        leave_board.setItems(leavedata);

    }

    @FXML
    public void refresh() {
        DatabaseUtil.fetchEmployees();
        initialize();

    }


    @FXML
    public void setName(String s) {
        uname.setText(s);
    }


    @FXML
    public void logout(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/employee/hello-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Employee Management System");
        stage.show();


    }

    @FXML
    private AnchorPane Update_taskforms;
    @FXML
    private Button updateTasks;
@FXML
private Button leave_formbtn;

@FXML
private AnchorPane salaryRecordform;

@FXML
private AnchorPane empuserpass;

    @FXML
    private AnchorPane Request_form;
    @FXML
    private Button salaryrecordbtn;

    @FXML
    private Button empuserpassbtn;
    @FXML
    public void SwitchForm(ActionEvent event) {
        if (event.getSource() == homebtn) {
            empuserpass.setVisible(false);
            salaryRecordform.setVisible(false);
            Request_form.setVisible(false);
            Updaterecords.setVisible(false);
            add_emp_form.setVisible(false);
            sal_form.setVisible(false);
            Update_taskforms.setVisible(false);
            home_form.setVisible(true);
        } else if (event.getSource() == addempbtn) {
            empuserpass.setVisible(false);
            salaryRecordform.setVisible(false);
            Updaterecords.setVisible(false);
            Request_form.setVisible(false);
            home_form.setVisible(false);
            sal_form.setVisible(false);
            Update_taskforms.setVisible(false);
            add_emp_form.setVisible(true);
        } else if (event.getSource() == salarybtn) {
            empuserpass.setVisible(false);
            salaryRecordform.setVisible(false);
            Updaterecords.setVisible(false);
            add_emp_form.setVisible(false);
            home_form.setVisible(false);
            Request_form.setVisible(false);
            Update_taskforms.setVisible(false);
            sal_form.setVisible(true);
        } else if (event.getSource() == update_empbtn) {
            empuserpass.setVisible(false);
            salaryRecordform.setVisible(false);
            Request_form.setVisible(false);
            add_emp_form.setVisible(false);
            home_form.setVisible(false);
            sal_form.setVisible(false);
            Update_taskforms.setVisible(false);
            Updaterecords.setVisible(true);
        } else if (event.getSource() == updateTasks) {
            empuserpass.setVisible(false);
            salaryRecordform.setVisible(false);
            Updaterecords.setVisible(false);
            add_emp_form.setVisible(false);
            home_form.setVisible(false);
            sal_form.setVisible(false);
            Request_form.setVisible(false);
            Update_taskforms.setVisible(true);
        }else if(event.getSource()==leave_formbtn){
            empuserpass.setVisible(false);
            salaryRecordform.setVisible(false);
            Updaterecords.setVisible(false);
            add_emp_form.setVisible(false);
            home_form.setVisible(false);
            sal_form.setVisible(false);
            Update_taskforms.setVisible(false);
            Request_form.setVisible(true);
        } else if (event.getSource() ==salaryrecordbtn) {
            empuserpass.setVisible(false);
            Updaterecords.setVisible(false);
            add_emp_form.setVisible(false);
            home_form.setVisible(false);
            sal_form.setVisible(false);
            Update_taskforms.setVisible(false);
            Request_form.setVisible(false);
            salaryRecordform.setVisible(true);
        } else if (event.getSource()==empuserpassbtn) {
            Updaterecords.setVisible(false);
            add_emp_form.setVisible(false);
            home_form.setVisible(false);
            sal_form.setVisible(false);
            Update_taskforms.setVisible(false);
            Request_form.setVisible(false);
            salaryRecordform.setVisible(false);
            empuserpass.setVisible(true);
        }


    }


    public void clear() {

        add_fname.setText("");
        add_emplname.setText("");
        add_empcontact.setText("");
        add_empmail.setText("");
        add_empgender.getItems().clear();
        add_empdesignation.getItems().clear();
        add_empdoj.setValue(null);
        add_empimage.setImage(null);
        add_empsalary.setText("");

    }

    @FXML
    private TextField salary_eid;

    @FXML
    private TextField salary_salary;

    @FXML
    private Button salary_updatebtn;

    @FXML
    public void Update_salbtn() {

        try {
            int eid = Integer.parseInt(salary_eid.getText());
            int sal = Integer.parseInt(salary_salary.getText());

            DatabaseUtil.update_sal(eid, sal);


            salary_eid.setText("");
            salary_salary.setText("");

        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Please Enter Valid Details");
            alert.showAndWait();
        }


        refresh();
    }

    @FXML
    private Button salary_clearbtn;

    @FXML
    public void sal_clearbtn() {
        salary_eid.setText("");
        salary_salary.setText("");
    }


    @FXML
    private TextField update_searcheid;
    @FXML
    private Button update_eidsearch;

    @FXML
    private TextField update_fname;

    @FXML
    private DatePicker update_doj;
    @FXML
    private TextField update_lname;
    @FXML
    private TextField update_mail;
    @FXML
    private TextField update_cont;

    @FXML
    private ComboBox<String> update_gender;

    @FXML
    private ComboBox<String> update_desig;


    @FXML
    private Button updateclear;

    @FXML
    private Button update_recordbtn;

    @FXML
    public void Update_Functionalities(ActionEvent event) {
        int eid = 0;
        update_gender.getItems().clear();


        update_gender.getItems().addAll("Male", "Female", "Others");
        update_desig.getItems().clear();
        update_desig.getItems().addAll("Software Developer", "Product Analyst", "Tester", "Quality Analyst", "Junior Architect");


        if (event.getSource() == update_eidsearch) {
            try {

                eid = Integer.parseInt(update_searcheid.getText());
                Employee emp = DatabaseUtil.getEmployeeById(eid);
                update_fname.setText(emp.getFname());
                update_desig.setValue(emp.getDesignation());
                update_gender.setValue(emp.getGender());
                update_doj.setValue(LocalDate.parse(emp.getDoj()));
                update_lname.setText(emp.getLname());
                update_mail.setText(emp.getMail());
                update_cont.setText(emp.getContact());


            } catch (Exception e) {
                DatabaseUtil.showAlert(Alert.AlertType.ERROR, "Failed", "Record Not found", "Employee Id is not Valid " + eid);
            }
        }


    }


    @FXML
    public void UpdateRecordsBTN() {
        String fname = update_fname.getText();
        String lname = update_lname.getText();
        String doj = String.valueOf(update_doj.getValue());
        String desig = update_desig.getValue();
        String gender = update_gender.getValue();
        String mail = update_mail.getText();
        String cont = update_cont.getText();
        int eid = Integer.parseInt(update_searcheid.getText());
        if(isValidEmail(mail) ) {
            DatabaseUtil.Update_RecordByID(fname, lname, gender, cont, mail, desig, doj, eid);
        }else{
            DatabaseUtil.showAlert(Alert.AlertType.ERROR,"Error in mail id","Mail Id is not Valid",mail+"");
        }

        updateclear();

    }


    @FXML
    public void deleterecord() {
        int eid = Integer.parseInt(update_searcheid.getText());

        DatabaseUtil.DeleteByidRecord(eid);
        updateclear();
    }

    @FXML
    public void updateclear() {
        update_fname.setText("");
        update_cont.setText("");
        update_mail.setText("");
        update_gender.getItems().clear();

        update_desig.getItems().clear();


        update_lname.setText("");
        update_doj.setValue(null);
        update_searcheid.setText("");

    }

    @FXML
    private TextField task_eid;

    @FXML
    private TextField task_searchtitle;

    @FXML
    private TextField task_searchdescription;

    public void assigntasks() {
        int eid = 0;
        try {
            eid = Integer.parseInt(task_eid.getText());
        } catch (Exception e) {
            DatabaseUtil.showAlert(Alert.AlertType.ERROR, "Failed", "Please Enter Valid ID", "You Entered Employee Id " + eid);
        }
        String tasktitle = task_searchtitle.getText();
        String desc = task_searchdescription.getText();

if(!tasktitle.isEmpty() || !desc.isEmpty()) {
    DatabaseUtil.Assigntask(eid, tasktitle, desc);

}else {
    DatabaseUtil.showAlert(Alert.AlertType.ERROR, "Failed", "All Details Are Required to assign Task", " Please fill task title and Description");
}

        refresh();
        tasksclear();

    }


    @FXML
    private TextField deletetasksearch;

    public void DeleteTask() {
        int tid = 0;
        try {
            tid = Integer.parseInt(deletetasksearch.getText());
        } catch (Exception e) {
            DatabaseUtil.showAlert(Alert.AlertType.ERROR, "Failed", "Please Enter Valid ID", "You Entered Employee Id " + tid);
        }

        DatabaseUtil.Deletetask(tid);
        refresh();
        tasksclear();

    }

    public void tasksclear() {
        deletetasksearch.setText("");
        task_eid.setText("");
        task_searchtitle.setText("");
        task_searchdescription.setText("");

    }

    @FXML
    private TextField leavegetlid;
    @FXML
    private ComboBox<String> leave_actioncombo;
    @FXML
    private Button Update_leavebtn;


    @FXML
    public void update_leave(){



        int lid=0;
        try{
            lid=Integer.parseInt(leavegetlid.getText());
        }catch(Exception e){
        DatabaseUtil.showAlert(Alert.AlertType.ERROR,"Failed","Provide Valid Leave Id","This is not a Valid ID "+lid);
        }

        String val=leave_actioncombo.getValue();

        if(val!=null) {

            DatabaseUtil.update_leavestatus(lid, val);
            clearleave();
        }else{
            DatabaseUtil.showAlert(Alert.AlertType.ERROR,"Failed","Provide Valid Details","All fields are required ");

        }


    }

    public void clearleave(){
        leavegetlid.setText("");
        leave_actioncombo.getItems().clear();
    }

    @FXML
    private TextField searchsalid;

    @FXML
    private Label salgross;
    @FXML
    private Label salhealth;
    @FXML
    private Label saltax;
    @FXML
    private Label saltravel;
@FXML
    private Label salnet;

    @FXML
    public void salaryRecords(){
int eid=0;
        try {
            eid = Integer.parseInt(searchsalid.getText());
        }catch(Exception e){

        }

        String sql="Select * from salary_record where eid=?";

        float gross=0;
        float net=0;
        float health=0;
        float tax=0;
        float travel=0;

        try(Connection c=DatabaseUtil.getConnection();
            PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,eid);
            ResultSet rs=p.executeQuery();
            while(rs.next()){
                gross=rs.getFloat("gross_pay");
                net=rs.getFloat("net_pay");
                health=rs.getFloat("health_insurance");
                travel=rs.getFloat("travel_allowance");
                tax=rs.getFloat("tax");
            }

            salgross.setText("Gross Pay : "+ gross);
            salnet.setText("Net Pay : "+net);
            salhealth.setText("Health Insurance : "+health);
            saltravel.setText("Travel Allowance : "+travel);
            saltax.setText("Tax : "+tax);
        }catch (Exception e){
e.printStackTrace();
        }

    }

    @FXML
public void clearsalrecord(){
    searchsalid.setText("");
    salgross.setText("");
    salnet.setText("");
    salhealth.setText("");
    saltravel.setText("");
    saltax.setText("");


}

@FXML
private Label setempusername;

    @FXML
    private Label setemppass;

    @FXML
    private TextField searchforempuserpass;

public void getuserandpass(){
int eid=0;
    try{
        eid=Integer.parseInt(searchforempuserpass.getText());
    }catch (Exception e){
        DatabaseUtil.showAlert(Alert.AlertType.ERROR,"Failed","Not a Valid ID","Please provide Id in valid Format");
    }

    String sql="select empusername,emppass from empdata where eid = ?";
    try(Connection c=DatabaseUtil.getConnection();
    PreparedStatement ps=c.prepareStatement(sql)){

        ps.setInt(1,eid);
        ResultSet rs = ps.executeQuery();

        // Process the result
        if (rs.next()) {
            String emppass = rs.getString("emppass");
            String userName = rs.getString("empusername");
            setemppass.setText("Employee Password: " + emppass);
            setempusername.setText("Username: " + userName);
        } else {
            setempusername.setText("No username and Password Found ! ");
        }

    }catch (Exception e){
        DatabaseUtil.showAlert(Alert.AlertType.ERROR,"Failed","Not a Valid ID","Please provide Id in valid Format");
    }


}

public void clearcredentials(){
    setemppass.setText("");
    setempusername.setText("");
    searchforempuserpass.setText("");
}


}
