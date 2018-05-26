package dashboard;

import colors.Colors;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

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
    private JFXButton settingBtn;
    private JFXButton[] buttons;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttons = new JFXButton[]{dashboardBtn,profileBtn,profileBtn,settingBtn};
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardPane.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardPane.fxml"));
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