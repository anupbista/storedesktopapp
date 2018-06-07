package dashboard;

import dbConnection.DBHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import login.LoginController;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ProfilePaneController implements Initializable {

    @FXML
    private ImageView userImage;

    @FXML
    private Text userName;
    private Connection connection;
    private DBHandler dbHandler;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHandler = new DBHandler();
        setProflePicture();
        userName.setText(LoginController.user);
    }

    private void setProflePicture(){
        String sql = "SELECT user_image FROM staffs WHERE username='"+LoginController.user+"'";
        try {
            connection = dbHandler.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                InputStream userProfileImage = rs.getBinaryStream("user_image");
                OutputStream image = new FileOutputStream(new File("userPhoto.jpg"));
                byte[] content = new byte[1024];
                int size=0;
                while((size = userProfileImage.read(content))!=-1){
                    image.write(content,0,size);
                }
                userProfileImage.close();
                image.close();
                Image uimage = new Image("file:userPhoto.jpg");
                userImage.setImage(uimage);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
