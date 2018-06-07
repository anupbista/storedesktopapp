package login;

import dbConnection.DBHandler;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import utils.Utils;

public class LoginController implements Initializable {

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    private DBHandler dbHandler;
    private Connection connection;
    private PreparedStatement pst;
    @FXML
    private JFXCheckBox rememberMe;

    public static Stage mainStage;
    private Parent dashboardPane;
    private Parent signUpPane;
    @FXML
    private AnchorPane loginPane;
    private String isRemember;
    @FXML
    private Label error_username;
    @FXML
    private Label error_password;
    private Boolean validationError=false;

    public static String user;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        isRemember = Utils.readPropValue("isRemember");
        if(Boolean.valueOf(isRemember)){
//            get username and password
            String userName = Utils.readPropValue("userName");
            String userPass = Utils.readPropValue("userPass");
            rememberMe.setSelected(true);
            username.setText(userName);
            password.setText(userPass);
            username.setFocusTraversable(false);
            password.setFocusTraversable(false);
        }
        rememberMe.selectedProperty().addListener( (observable, oldValue, newValue)->{
            Utils.updatePropValue("isRemember", Boolean.toString(newValue));
            isRemember = Utils.readPropValue("isRemember");
        } );

        dbHandler = new DBHandler();
    }

//    SignUp Action
    @FXML
    private void openSignUpPane(ActionEvent event) {
        try {
            signUpPane = FXMLLoader.load(getClass().getResource("/SignUp.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        FadeTransition loginFadeDash = new FadeTransition();
        loginFadeDash.setNode(loginPane);
        loginFadeDash.setDuration(Duration.millis(400));
        loginFadeDash.setFromValue(1.0);
        loginFadeDash.setToValue(0.0);
        loginFadeDash.play();
        loginFadeDash.setOnFinished(e -> {
            mainStage.setScene(new Scene(signUpPane));
        });
    }

    public static void setValidationErrorMessage(Label l, String msg){
        l.setVisible(true);
        l.setText(msg);
    }
    public static void resetValidationErrorMessage(Label l){
        l.setVisible(false);
    }

//    Login Action
    @FXML
    private void login(ActionEvent event){

        String userName = username.getText();
        String userPass= password.getText();
        if(validationError){
            resetValidationErrorMessage(error_username);
            resetValidationErrorMessage(error_password);
            validationError=false;
        }
        if ((userName == null || userName.isEmpty()) && (userPass == null || userPass.isEmpty())){
            validationError = true;
            setValidationErrorMessage(error_username,"Username must be filled");
            username.focusedProperty().addListener( (observable, oldValue, newValue)->{
                resetValidationErrorMessage(error_username);
            } );
            setValidationErrorMessage(error_password,"Password must be filled");
            password.focusedProperty().addListener( (observable, oldValue, newValue)->{
                resetValidationErrorMessage(error_password);
            } );
        }
        else if(userName == null || userName.isEmpty()){
            validationError = true;
            setValidationErrorMessage(error_username,"Username must be filled");
            username.focusedProperty().addListener( (observable, oldValue, newValue)->{
                resetValidationErrorMessage(error_username);
            } );
        }
        else if(userPass == null || userPass.isEmpty()){
            validationError = true;
            setValidationErrorMessage(error_password,"Password must be filled");
            password.focusedProperty().addListener( (observable, oldValue, newValue)->{
                resetValidationErrorMessage(error_password);
            } );
        }
        else{
            connection = dbHandler.getConnection();
            String sql = "SELECT username, password,role FROM staffs where username=? and password=?";
            try {
                pst = connection.prepareStatement(sql);
                pst.setString(1,username.getText());
                pst.setString(2,password.getText());

                ResultSet rs = pst.executeQuery();
                int count = 0;
                String role="Admin";
                while (rs.next()){
                    count++;
                    role = rs.getString("role");
                }
                if (count ==1){
                    System.out.println("Login Successfully");
                    user= username.getText();
                    if(Boolean.valueOf(isRemember)) {
                        Utils.updatePropValue("userName", username.getText());
                        Utils.updatePropValue("userPass", password.getText());
                    }

                    executeLoginAction(loginPane,role);
                }
                else{
                    System.out.println("Login Failed");
                    validationError = true;
                    setValidationErrorMessage(error_username,"Username is not correct");
                    username.focusedProperty().addListener( (observable, oldValue, newValue)->{
                        resetValidationErrorMessage(error_username);
                    } );
                    setValidationErrorMessage(error_password,"Password is not correct");
                    password.focusedProperty().addListener( (observable, oldValue, newValue)->{
                        resetValidationErrorMessage(error_password);
                    } );
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
        }
    }

    private void executeLoginAction(Node loginRoot, String role){
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
        loginFadeDash.setNode(loginRoot);
        loginFadeDash.setDuration(Duration.millis(400));
        loginFadeDash.setFromValue(1.0);
        loginFadeDash.setToValue(0.0);
        loginFadeDash.play();
        loginFadeDash.setOnFinished(event -> mainStage.setScene(new Scene(dashboardPane)));
    }

    public void closeCurrentStage(ActionEvent event) {
        ((Node)event.getSource()).getScene().getWindow().hide();
    }

    public static void registerPrimaryStage(Stage primaryStage){
        mainStage = primaryStage;
    }

    public static Stage getMainStage() {
        return mainStage;
    }
}
