package org.example.employee;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/employee";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin@123";

    // Method to establish a connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    //    ..method to validate emp
    public static boolean validateemp(String username, String password) {
        String sql = "SELECT * FROM empdata WHERE empusername = ? AND emppass = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    // Method to validate admin credentials
    public static boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM emp WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getPresentEmp() {
        int presentEmpCount = 0;
        String sql = "SELECT COUNT(*) FROM attendance WHERE date = CURRENT_DATE AND status = 'Present'";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            if (rs.next()) {
                presentEmpCount = rs.getInt(1);  // get the count of employees marked as 'Present'
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return presentEmpCount;
    }

    public static void update_sal(int eid, int sal) {
        String sql = "UPDATE empdata SET salary = ? WHERE eid = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // Set salary (first ? in the query)
            pstmt.setInt(1, sal);

            // Set eid (second ? in the query)
            pstmt.setInt(2, eid);

            // Execute the update
            int row=pstmt.executeUpdate();
//            System.out.println("Salary updated successfully for employee ID: " + eid);
            if(row>0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Salary Updated ");
                alert.showAndWait();
                UpdatesalaryRecord(eid,(float)sal);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Please Enter Valid Employee ID" +
                        "");
                alert.showAndWait();
            }

        } catch (Exception e) {
//            return false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Please Enter Valid employee id");
            alert.showAndWait();
        }
//        return true;

    }



    public static int gettotal(){
        int totalemp=0;
        String sql = "SELECT COUNT(*) AS total FROM empdata";

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()){

            if (rs.next()) {
                totalemp = rs.getInt("total");
            }


        }catch (Exception e){

        }

        return totalemp;
    }

    public static List<Employee> fetchEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM empdata";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int eid = rs.getInt("eid");
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                String gender = rs.getString("gender");
                String contact = rs.getString("contact");
                String mail=rs.getString("mail");
                String designation = rs.getString("designation");
                String image = rs.getString("image");
                String doj = rs.getString("doj");
                int salary=rs.getInt("salary");

                // Create an Employee object and add it to the list
                Employee employee = new Employee(eid, fname, lname, gender, contact, mail, designation, image, doj,salary);
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.err.println("Error while fetching employees: " + e.getMessage());
            e.printStackTrace();
        }

        return employees;
    }


    public static void InsertAddData(String fname,String lname,String contact,String mail,String dat,String gender,String desig,int esal,String paths){


        String userNemp=mail;
        String pass=fname+contact.substring(0,4);

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement("INSERT INTO empdata (fname,lname,gender,contact,mail,designation,doj,salary,image,empusername,emppass) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)");
             FileInputStream fis = new FileInputStream(new File(paths))) {

            // Validate file path
            File file = new File(paths);
            if (!file.exists()) {
                throw new FileNotFoundException("Image file not found at: " + paths);
            }

            // Set parameters
            pstmt.setString(1, fname);               // First Name
            pstmt.setString(2, lname);               // Last Name
            pstmt.setString(3, gender);              // Gender
            pstmt.setString(4, contact);             // Contact
            pstmt.setString(5, mail);                // Email
            pstmt.setString(6, desig);               // Designation
            pstmt.setDate(7, Date.valueOf(dat));                 // Date (ensure format is correct if DATE type)
            pstmt.setInt(8, esal); // Salary
            pstmt.setBinaryStream(9, fis, (int) file.length()); // Image
            pstmt.setString(10,userNemp);
            pstmt.setString(11,pass);


            // Execute the query
            int row=pstmt.executeUpdate();
            if(row>0) {
                // Show success alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Data Insert Successful");
                alert.setContentText("Employee data has been inserted successfully!");
                alert.showAndWait();

                int eid = getempid(mail);
                insertsalaryRecord(eid, (float) esal);
            }
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Image Not Found");
            alert.setContentText("Please Upload Image");
            alert.showAndWait();



        }catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Duplicate Email");
                alert.setHeaderText("Email Already Exists");
                alert.setContentText("The email '" + mail + "' is already used by another employee.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText("Something went wrong while inserting data");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("All fields Are Required to insert data");
            alert.setContentText("Please Provide all Details");
            alert.showAndWait();
        }



    }

    // Method to fetch employee details by eid
    public static Employee getEmployeeById(int eid) {
        String sql = "SELECT * FROM empdata WHERE eid = ?";
        Employee employee = null;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the eid parameter
            pstmt.setInt(1, eid);

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Map the result to an Employee object
                    employee = new Employee(
                            rs.getInt("eid"),
                            rs.getString("fname"),
                            rs.getString("lname"),
                            rs.getString("gender"),
                            rs.getString("contact"),
                            rs.getString("mail"),
                            rs.getString("designation"),
                            rs.getString("image"),
                            rs.getString("doj"),
                            rs.getInt("salary")
                    );
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,"Failed","Record Not found","Employee Id is not Valid "+eid);
        }

        return employee;
    }

    public static List<Task> getTask() {
        List<Task> task = new ArrayList<>();
        String sql = "SELECT * FROM task";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int eid=rs.getInt("eid");
                int taskid=rs.getInt("task_id");
                String desc=rs.getString("description");
                String post=rs.getString("post");
                String status=rs.getString("status");
                String taskname=rs.getString("task_name");
//                System.out.println(status+post+taskid);

                // Create an Employee object and add it to the list
                Task tasks = new Task(taskid,taskname,desc,status,post,eid);
                task.add(tasks);
            }
        } catch (SQLException e) {
            System.err.println("Error while fetching Tasks: " + e.getMessage());
            e.printStackTrace();
        }

        return task;
    }

    public static void updateEmployeeImage(int eid, String imagePath) {
        String sql = "UPDATE empdata SET image = ? WHERE eid = ?";
        File file = new File(imagePath);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             FileInputStream fis = new FileInputStream(file)) {

            pstmt.setBinaryStream(1, fis, (int) file.length());
            pstmt.setInt(2, eid);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Profile Picture Updated", "Your profile picture has been successfully updated!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Update Failed", "No employee found for given ID.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating image", e.getMessage());
        }
    }
    public static Image getImageFromDatabase(int eid) {
        String sql = "SELECT image FROM empdata WHERE eid = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, eid);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                InputStream is = rs.getBinaryStream("image");
                if (is != null) {
                    return new Image(is);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Fetch tasks by employee ID
    public static List<Task> getTaskByIDemp(int eid) {


        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT task_id, task_name, description, post, status FROM task WHERE eid = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameter in the query
            pstmt.setInt(1, eid);

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int taskid = rs.getInt("task_id");
                    String tasktitle = rs.getString("task_name");
                    String taskdesc = rs.getString("description");
                    String taskdate = rs.getString("post"); // Change to `Date` if using `java.sql.Date`
                    String status = rs.getString("status");


                    // Create a Task object and add it to the list
                    Task task = new Task(taskid, tasktitle, taskdesc, taskdate, status, eid);
                    tasks.add(task);
                }
            }catch (Exception e){
                System.out.println("RS");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Error while fetching tasks: " + e.getMessage());
            e.printStackTrace();
        }

        return tasks;
    }

    @FXML
    public static int getempid(String username) {
        int empId = 0;
        String sql = "SELECT eid FROM empdata WHERE empusername = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    empId = rs.getInt("eid");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching Employee ID: " + e.getMessage());
        }

        return empId;
    }







    public static void Update_RecordByID(String fname,String lname,String gender,String contact,String mail,String desig,String doj,int eid){

        String updateQuery = "UPDATE empdata SET fname = ?  , lname = ? ,gender = ? , contact = ? , mail= ? ,designation = ? , doj = ?  WHERE eid = ?";

        try(Connection con=getConnection();
            PreparedStatement pstmt = con.prepareStatement(updateQuery)
        ){

            pstmt.setString(1,fname);
            pstmt.setString(2,lname);
            pstmt.setString(3,gender);
            pstmt.setString(4,contact);
            pstmt.setString(5,mail);
            pstmt.setString(6,desig);
            pstmt.setDate(7,Date.valueOf(doj));
            pstmt.setInt(8,eid);

            int rows= pstmt.executeUpdate();

            if(rows>0){
                showAlert(Alert.AlertType.INFORMATION,"Success","Record Updated Successfully"," Record Update Employee Id "+eid);
            }else{
                showAlert(Alert.AlertType.ERROR,"Failed","Failed to Update ","Employee Id Not found "+eid);
            }

        }catch (Exception e){
            showAlert(Alert.AlertType.ERROR,"Failed","Failed to Update ","Employee Id Not found "+eid);
        }



    }


    public static  void DeleteByidRecord(int eid){

        String deletesql="DELETE FROM empdata where eid = ?";

        try(Connection con=getConnection();
            PreparedStatement ps=con.prepareStatement(deletesql)) {

            ps.setInt(1,eid);

            int rows=ps.executeUpdate();

            if(rows>0){
                showAlert(Alert.AlertType.INFORMATION,"Success","Record Deleted Successfully"," Record Deleted Employee Id "+eid);
            }else{
                showAlert(Alert.AlertType.ERROR,"Failed","Unable To Delete Record"," Record not fount with this Employee Id "+eid);
            }


        }catch(Exception e){
            showAlert(Alert.AlertType.ERROR,"Failed","Unable To Delete Record"," Record not fount with this Employee Id "+eid);
        }


    }

    public static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }


    public static void Assigntask(int eid,String title,String desc){
        String assignsql="insert into task(task_name,description,post,eid) VALUES(?,?,CURRENT_DATE,?)";

        try(Connection con=getConnection();
            PreparedStatement ps=con.prepareStatement(assignsql)){

            ps.setString(1,title);
            ps.setString(2,desc);
            ps.setInt(3,eid);

            int rows=ps.executeUpdate();

            if (rows > 0) {
                showAlert(Alert.AlertType.INFORMATION,"Success","Task Assigned ","Employee id  "+eid);
            } else {

            }


        }catch(Exception e){
            showAlert(Alert.AlertType.ERROR,"Failed","Task Assignment failed ","Employee id is not Valid "+eid);
        }



    }

    public static void Deletetask(int taskid){
        String deletesql="DELETE from task where task_id=?";

        try(Connection con=getConnection();
            PreparedStatement ps=con.prepareStatement(deletesql)){
            ps.setInt(1,taskid);

            int rows=ps.executeUpdate();
            if (rows > 0) {
                showAlert(Alert.AlertType.INFORMATION,"Success","Task Deleted ","Task id "+taskid);
            } else {

            }



        }catch(Exception e){
            showAlert(Alert.AlertType.ERROR,"Failed","Task Deletion failed ","Task id is not Valid "+taskid);
        }


    }



    public static List<Leave> getLeave() {
        List<Leave> leaves = new ArrayList<>();
        String sql = "SELECT * FROM leave_requests";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int eid=rs.getInt("eid");
                int id=rs.getInt("id");
                String reason=rs.getString("leave_reason");
                String ltype=rs.getString("leave_type");
                String from=rs.getString("start_date");
                String to=rs.getString("end_date");
                String status=rs.getString("status");
//                System.out.println(status+post+taskid);

                // Create an Employee object and add it to the list
                Leave leavess = new Leave(eid,id,status,reason,from,to,ltype);
                leaves.add(leavess);
            }
        } catch (SQLException e) {
            System.err.println("Error while fetching Leaves: " + e.getMessage());
            e.printStackTrace();
        }

        return leaves;
    }


    public static  void update_leavestatus(int lid,String res){

        String query="update leave_requests SET status = ? where id = ?";

        try(Connection con=getConnection();
            PreparedStatement ps=con.prepareStatement(query)
        ){
            ps.setString(1,res);
            ps.setInt(2,lid);

            int rows=ps.executeUpdate();
            if(rows>0){
                DatabaseUtil.showAlert(Alert.AlertType.INFORMATION,"Success","Leave Request Status Updated","Leave Id "+lid+" Status "+res);
            }else{
                DatabaseUtil.showAlert(Alert.AlertType.INFORMATION,"Failed","Provide Valid Details","All fields are required ");
            }

        }catch(Exception e){
            DatabaseUtil.showAlert(Alert.AlertType.ERROR,"Failed","Provide Valid Details","All fields are required ");
        }



    }


    public static  void UpdateTAskbyemp(int tid,String status){

        String query="Update task SET status = ? WHERE task_id = ?";

        try(Connection c=getConnection();
            PreparedStatement ps=c.prepareStatement(query)){
            ps.setString(1,status);
            ps.setInt(2,tid);

            int row=ps.executeUpdate();

            if(row>0){
                showAlert(Alert.AlertType.INFORMATION,"Success","Task Status Updated","Task Id "+tid+" Status "+status);
            }else{
                showAlert(Alert.AlertType.ERROR,"Failed","Task Status Updated Failed","Please Enter All Details ");
            }

        }catch(Exception e){
            showAlert(Alert.AlertType.ERROR,"Failed","Task Status Updated Failed","Please Enter Valid Task id "+tid);
        }


    }


    public static void updateattendance(int eid) {
        String query = "INSERT INTO attendance (eid, date, status) VALUES (?, CURRENT_DATE, ?)";

        try (Connection c = getConnection();
             PreparedStatement p = c.prepareStatement(query)) {

            // Set query parameters
            p.setInt(1, eid);
            p.setString(2, "Present");

            // Try to insert the record
            int row = p.executeUpdate();

            if (row > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Attendance Marked", "Status for today is Present");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            // Handle duplicate entry error (unique constraint violation)
            showAlert(Alert.AlertType.ERROR, "Failed", "Attendance Already Marked", "You cannot mark attendance multiple times in one day.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Database Error", e.getMessage());
        }
    }
    public static boolean isEmployeePresentToday(int eid) {
        String sql = "SELECT COUNT(*) FROM attendance WHERE eid = ? AND DATE(attendance_date) = CURDATE()";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, eid);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // already marked present
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static List<Leave> getLeaveById(int empid) {
        List<Leave> leaves = new ArrayList<>();
        String sql = "SELECT * FROM leave_requests where eid = ? ";

        try (Connection conn = getConnection();

             PreparedStatement ps=conn.prepareStatement(sql)) {

            ps.setInt(1,empid);

            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
                int eid=rs.getInt("eid");
                int id=rs.getInt("id");
                String reason=rs.getString("leave_reason");
                String ltype=rs.getString("leave_type");
                String from=rs.getString("start_date");
                String to=rs.getString("end_date");
                String status=rs.getString("status");
//                System.out.println(status+post+taskid);
                // Create an Employee object and add it to the list
                Leave leavess = new Leave(eid,id,status,reason,from,to,ltype);
                leaves.add(leavess);
            }


        } catch (SQLException e) {
            System.err.println("Error while fetching Leaves: " + e.getMessage());
            e.printStackTrace();
        }

        return leaves;
    }

    @FXML
    public static void addleave(int eid,String ltype,String from,String to,String reason){
        String sqlquery="insert into leave_requests(leave_type,start_date,end_date,leave_reason,eid) VALUES (?,?,?,?,?)";

        try(Connection c=getConnection();
            PreparedStatement p=c.prepareStatement(sqlquery)){

            p.setString(1,ltype);
            p.setDate(2,Date.valueOf(from));
            p.setDate(3,Date.valueOf(to));
            p.setString(4,reason);
            p.setInt(5,eid);

            int row=p.executeUpdate();
            if(row>0){
                showAlert(Alert.AlertType.INFORMATION,"Success","Leave Request Sent to Admin","Employee Id "+eid);

            }else{
                showAlert(Alert.AlertType.ERROR,"Failed","Leave Request failed ","Employee Id "+eid);
            }

        }catch(Exception e){
            showAlert(Alert.AlertType.ERROR,"Failed","Missing Details","Provide all Details");
        }



    }



    //salary Record
    public static void insertsalaryRecord(int eid,float grosspay){
        float health=(grosspay*12)/100;
        float tax=0;
        if(grosspay>=100000){
            tax=(grosspay*10)/100;
        }

        float travel=(grosspay*5)/100;

        float netpay=(grosspay-health-tax)+travel;

        String sql="insert into salary_record Values(?,?,?,?,?,?)";

        try(Connection c=getConnection();
            PreparedStatement ps=c.prepareStatement(sql)){
            ps.setDouble(1,grosspay);
            ps.setDouble(2,netpay);
            ps.setDouble(3,health);
            ps.setDouble(4,travel);
            ps.setDouble(5,tax);
            ps.setInt(6,eid);

            int row=ps.executeUpdate();
            if(row>0){
                System.out.println("Updated");
            }else{
                System.out.println("Not Updated");
            }

        }catch(Exception e){
            e.printStackTrace();
        }



    }

    public static void UpdatesalaryRecord(int eid,float grosspay){
        float health=(grosspay*12)/100;
        float tax=0;
        if(grosspay>=100000){
            tax=(grosspay*10)/100;
        }

        float travel=(grosspay*5)/100;

        float netpay=(grosspay-health-tax)+travel;

        String sql="UPDATE salary_record SET gross_pay =?,net_pay=?,health_insurance=?,travel_allowance=?,tax=? where eid=?";

        try(Connection c=getConnection();
            PreparedStatement ps=c.prepareStatement(sql)){
            ps.setDouble(1,grosspay);
            ps.setDouble(2,netpay);
            ps.setDouble(3,health);
            ps.setDouble(4,travel);
            ps.setDouble(5,tax);
            ps.setInt(6,eid);

            int row=ps.executeUpdate();
            if(row>0){
                System.out.println("Updated");
            }else{
                System.out.println("Not Updated");
            }

        }catch(Exception e){
            e.printStackTrace();
        }



    }




}
