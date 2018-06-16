package dashboard;

import Modals.Customers;
import colors.Colors;
import com.jfoenix.controls.JFXButton;
import dbConnection.DBHandler;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import login.LoginController;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class CashierDashboardController implements Initializable {

    @FXML
    private StackPane dashboard_main_content;
    private StackPane profilePane;
    private StackPane settingsPane;
    private StackPane dashboardPane;
    @FXML
    private JFXButton dashboardBtn;

    @FXML
    private JFXButton profileBtn;

    @FXML
    private JFXButton settingBtn;
    private JFXButton[] buttons;
    @FXML
    private JFXButton logoutBtn;
    @FXML
    private AnchorPane dashboard;
    private Parent loginPane;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttons = new JFXButton[]{dashboardBtn,profileBtn,settingBtn};
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CustomersPane.fxml"));
            dashboardPane = loader.load();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        setDashContent(dashboardPane);
        setDashSideButtonStatus(buttons[0]);
//        checkValidProducts();
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            loginPane = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        FadeTransition loginFadeDash = new FadeTransition();
        loginFadeDash.setNode(dashboard);
        loginFadeDash.setDuration(Duration.millis(400));
        loginFadeDash.setFromValue(1.0);
        loginFadeDash.setToValue(0.0);
        loginFadeDash.play();
        loginFadeDash.setOnFinished(e -> {
            LoginController.getMainStage().setScene(new Scene(loginPane));
        });
    }

    @FXML
    public void loadDashboardPane(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CustomersPane.fxml"));
            dashboardPane = loader.load();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        setDashSideButtonStatus((JFXButton) event.getTarget());
        setDashContent(dashboardPane);
    }

    @FXML
    public void loadProfilePane(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProfilePane.fxml"));
            profilePane = loader.load();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        setDashSideButtonStatus((JFXButton) event.getTarget());
        setDashContent(profilePane);
    }

    @FXML
    void loadSettingsPane(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SettingsPane.fxml"));
            settingsPane = loader.load();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        setDashSideButtonStatus((JFXButton) event.getTarget());
        setDashContent(settingsPane);
    }

    public void setDashContent(Node node){
        dashboard_main_content.getChildren().clear();
        dashboard_main_content.getChildren().add(node);
    }
//remove product from cart after some time automatically
    public void checkValidProducts(){
        System.out.println("Checking valid products");
        Connection connection;
        DBHandler dbHandler = new DBHandler();
        connection = dbHandler.getConnection();
        String sql = "SELECT * FROM productOnCart";
        new Timer().schedule(
                new TimerTask() {

                    @Override
                    public void run() {
                        try {
                            Statement st = connection.createStatement();
                            ResultSet rs = st.executeQuery(sql);
                            while (rs.next()) {
                                int time = Integer.parseInt(rs.getString("time"));
                                int validTime = time+2;

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h");
                                System.out.println(Integer.parseInt(simpleDateFormat.format(new Date())));
                                System.out.println(validTime);
                               if (Integer.parseInt(simpleDateFormat.format(new Date())) > validTime){
                                   System.out.println("Product Removed for unvalid");

                                   String sql = "DELETE FROM productOnCart WHERE productOnCartID='"+rs.getString("productOnCartID")+"'";
                                   try {
                                       PreparedStatement psmt = connection.prepareStatement(sql);
                                       psmt.executeUpdate();

                                   } catch (SQLException e) {
                                       e.printStackTrace();
                                   }
                               }
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 5000);
    }

    public void setDashSideButtonStatus(JFXButton targetButton){
        targetButton.setStyle("-fx-background-color:"+Colors.DARK_PRIMARY_COLOR+";");
        for (JFXButton button: buttons){
            if(button != targetButton){
                button.setStyle("-fx-background-color:"+Colors.PRIMARY_COLOR+";");
            }
        }
    }
}