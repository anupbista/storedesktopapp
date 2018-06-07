package signup;

import com.jfoenix.controls.*;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dbConnection.DBHandler;
import javafx.util.Duration;
import login.LoginController;
import Modals.Staffs;

public class SignUpController implements Initializable {

    @FXML
    private Pane signUpPane;

    @FXML
    private JFXTextField first_name;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXTextField address;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField last_name;

    @FXML
    private JFXTextField phone_number;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField re_password;

    private Connection connection;
    private DBHandler dbHandler;
    private PreparedStatement pst;

    @FXML
    private JFXRadioButton gender_male;

    @FXML
    private ImageView profile_image_show;

    @FXML
    private JFXRadioButton gender_female;

    private FileInputStream fis;
    private File selectedFile;
    private File defaultSelectedFile;
    private String gender;
    private Parent loginPane;
    private Parent dashboardPane;
    @FXML
    private JFXDatePicker dob;
    @FXML
    private Label error_signup_message;
    @FXML
    private Label error_signup_email_message;
    private URI defaultUserImage;
    private Boolean validationError=false;
    @FXML
    private Label error_signup_username_message;
    @FXML
    private Label error_signup_password_message;
    @FXML
    private Label error_re_password_message;
    @FXML
    private ComboBox staffRole;

    ObservableList<String> staffRoleList = FXCollections.observableArrayList("Admin","Cashier","Inventory Manager");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHandler = new DBHandler();

        gender_male.setOnAction(e ->{
            gender = gender_male.getText();
        });
        gender_female.setOnAction(e ->{
            gender = gender_female.getText();
        });

        re_password.focusedProperty().addListener( (observable, oldValue, newValue)->{
            if(!password.getText().equals(re_password.getText())){
                LoginController.setValidationErrorMessage(error_re_password_message, "Password donot match");
            }else{
                LoginController.resetValidationErrorMessage(error_re_password_message);
            }
        });

        staffRole.setValue("Admin");
        staffRole.setItems(staffRoleList);

        staffRole.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                staffRole.setValue(newValue);
            }
        });

    }

    @FXML
    public void openLoginPane(ActionEvent event) {
        try {
            loginPane = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        FadeTransition loginFadeDash = new FadeTransition();
        loginFadeDash.setNode(signUpPane);
        loginFadeDash.setDuration(Duration.millis(400));
        loginFadeDash.setFromValue(1.0);
        loginFadeDash.setToValue(0.0);
        loginFadeDash.play();
        loginFadeDash.setOnFinished(e -> {
            LoginController.getMainStage().setScene(new Scene(loginPane));
        });
    }

    @FXML
    public void signUp(ActionEvent event){
        if(validationError){
            LoginController.resetValidationErrorMessage(error_signup_message);
            LoginController.resetValidationErrorMessage(error_signup_email_message);
            LoginController.resetValidationErrorMessage(error_signup_username_message);
            LoginController.resetValidationErrorMessage(error_signup_password_message);
            validationError=false;
        }
        if(validateEmpty() && validateEmail() && validateUsername() && validatePassword()){

            if(selectedFile != null){
                try {
                    fis = new FileInputStream(selectedFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    defaultUserImage = getClass().getResource("/img/user.png").toURI();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                try {
                    defaultSelectedFile = new File(defaultUserImage);
                    fis=new FileInputStream(defaultSelectedFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            File userImage;
            if(selectedFile!=null){
                userImage = selectedFile;
            }else{
                userImage = defaultSelectedFile;

            }
            Staffs staffs = new Staffs(first_name.getText(), last_name.getText(), address.getText(), phone_number.getText(),email.getText(),username.getText(),password.getText(),userImage,gender,staffRole.getValue().toString(),dob.getValue());

            String sql = "INSERT INTO staffs(first_name,last_name,address,phone_number,email,username,password,user_image,gender,dob,role)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
            try {
                connection = dbHandler.getConnection();
                pst = connection.prepareStatement(sql);

            try {
                pst.setString(1,staffs.getFirstName());
                pst.setString(2,staffs.getLastName());
                pst.setString(3,staffs.getAddress());
                pst.setString(4,staffs.getPhoneNumber());
                pst.setString(5,staffs.getEmail());
                pst.setString(6,staffs.getUserName());
                pst.setString(7,staffs.getPassword());

                pst.setBinaryStream(8, fis, (int)staffs.getUserImage().length());

                pst.setString(9,staffs.getGender());
                pst.setString(10, String.valueOf(staffs.getDob()));
                pst.setString(11,staffs.getRole());
                pst.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Successfully registered");
                alert.showAndWait();

            }
            catch (SQLException e){
                e.printStackTrace();
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (connection!=null){
                        connection.close();
                        pst.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            LoginController.user=username.getText();
            String role = staffs.getRole();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDashboard.fxml"));
                switch (role){
                    case "Admin":
                        loader = new FXMLLoader(getClass().getResource("/AdminDashboard.fxml"));
                        break;
                    case "Cashier":
                        loader = new FXMLLoader(getClass().getResource("/CashierDashboard.fxml"));
                        break;
                    case "Inventory Manager":
                        loader = new FXMLLoader(getClass().getResource("/InventoryDashboard.fxml"));
                        break;
                }
                dashboardPane = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FadeTransition loginFadeDash = new FadeTransition();
            loginFadeDash.setNode(signUpPane);
            loginFadeDash.setDuration(Duration.millis(400));
            loginFadeDash.setFromValue(1.0);
            loginFadeDash.setToValue(0.0);
            loginFadeDash.play();
            loginFadeDash.setOnFinished(e -> {
                LoginController.getMainStage().setScene(new Scene(dashboardPane));
            });

        }
    }

    private Boolean validateEmpty(){
        String firstName = first_name.getText();
        String lastName = last_name.getText();
        String userEmail = email.getText();
        String userPhone = phone_number.getText();
        LocalDate userDob = dob.getValue();
        String userAddress = address.getText();
        String userName = username.getText();
        String userPass = password.getText();
        String userRePass = re_password.getText();
        if((firstName == null || firstName.isEmpty()) || (lastName == null || lastName.isEmpty()) || (userEmail == null || userEmail.isEmpty()) ||
                (userPhone == null || userPhone.isEmpty()) || (userDob == null) || (userAddress == null || userAddress.isEmpty()) ||
                (userName == null || userName.isEmpty()) || (userPass == null || userPass.isEmpty()) || (userRePass == null || userRePass.isEmpty()) ||
                (gender == null || gender.isEmpty()) ) {
            LoginController.setValidationErrorMessage(error_signup_message, "All fields must be filled");
            validationError=true;
            return false;
        }else{
            return true;
        }
    }

    private Boolean validateUsername(){
        Pattern usernamePattern = Pattern.compile("[a-zA-Z0-9]*");
        Matcher usernameMatcher = usernamePattern.matcher(username.getText());
        if(usernameMatcher.find() && usernameMatcher.group().equals(username.getText())){
            String sql = "SELECT username FROM staffs";
            try {
                connection = dbHandler.getConnection();
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()){
                    String userName = rs.getString("username");
                    if (userName.equals(username.getText().toLowerCase())){
                        LoginController.setValidationErrorMessage(error_signup_username_message, "Username already taken");
                        validationError=true;
                        return false;
                    }
                }
                return true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            LoginController.setValidationErrorMessage(error_signup_username_message, "Username can only have letters and numbers");
            validationError=true;
        }
        return false;
    }

    private Boolean validateEmail(){
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
        Matcher emailMatcher = emailPattern.matcher(email.getText());
        if(emailMatcher.find() && emailMatcher.group().equals(email.getText())){
            String sql = "SELECT email FROM staffs";
            try {
                connection = dbHandler.getConnection();
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()){
                    String userEmail = rs.getString("email");
                    if (userEmail.equals(email.getText().toLowerCase())){
                        LoginController.setValidationErrorMessage(error_signup_email_message, "Email already taken");
                        validationError=true;
                        return false;
                    }
                }
                return true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            LoginController.setValidationErrorMessage(error_signup_email_message, "Enter valid Email");
            validationError=true;
        }
        return false;
    }

    private Boolean validatePassword(){
        if (password.getText().length()<=6){
            LoginController.setValidationErrorMessage(error_signup_password_message, "Password must be greater than 6");
            validationError=true;
            return false;
        }
        return true;
    }

//    Image Uploader
    @FXML
    private void profileImageUploader(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.png", "*.jpg", "*.gif")); // limit fileChooser options to image files
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {

            String imageFile = null;
            try {
                imageFile = selectedFile.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            Image image = new Image(imageFile);
            profile_image_show.setImage(image);
        } else {
            System.out.println("Cancelled");
        }
    }


}
