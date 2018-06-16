package dashboard;

import Modals.Products;
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

    private Connection connection;
    private DBHandler dbHandler;

    @FXML
    private Text userName;

    @FXML
    private ImageView userImage;

    @FXML
    private Text fullName;

    @FXML
    private Text address;

    @FXML
    private Text phoneNumber;

    @FXML
    private Text email;

    @FXML
    private Text gender;

    @FXML
    private Text dob;

    @FXML
    private Text role;

    @FXML
    private Text password;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHandler = new DBHandler();
        setProflePicture();
        userName.setText(LoginController.user);
        getUserInfo();
    }

    private void getUserInfo() {
        String sql = "SELECT * FROM staffs WHERE username='"+LoginController.user+"'";
        try {
            connection = dbHandler.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                fullName.setText(rs.getString("first_name")+" "+rs.getString("last_name"));
                address.setText(rs.getString("address"));
                phoneNumber.setText(rs.getString("phone_number"));
                email.setText(rs.getString("email"));
                gender.setText(rs.getString("gender"));
                dob.setText(rs.getString("dob"));
                role.setText(rs.getString("role"));
                password.setText(rs.getString("password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
