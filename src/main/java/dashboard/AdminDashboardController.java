package dashboard;

import colors.Colors;
import com.jfoenix.controls.JFXButton;
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
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    @FXML
    private StackPane dashboard_main_content;
    private StackPane profilePane;
    private StackPane productPane;
    private StackPane settingsPane;
    private StackPane dashboardPane;
    @FXML
    private JFXButton dashboardBtn;

    @FXML
    private JFXButton profileBtn;
    @FXML
    private JFXButton productBtn;

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
        buttons = new JFXButton[]{dashboardBtn,profileBtn,productBtn,settingBtn};
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CustomersPane.fxml"));
            dashboardPane = loader.load();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        setDashContent(dashboardPane);
        setDashSideButtonStatus(buttons[0]);
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
    void loadProductPane(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductsPane.fxml"));
            productPane = loader.load();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        setDashSideButtonStatus((JFXButton) event.getTarget());
        setDashContent(productPane);
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

    public void setDashSideButtonStatus(JFXButton targetButton){
        targetButton.setStyle("-fx-background-color:"+Colors.DARK_PRIMARY_COLOR+";");
        for (JFXButton button: buttons){
            if(button != targetButton){
                button.setStyle("-fx-background-color:"+Colors.PRIMARY_COLOR+";");
            }
        }
    }
}