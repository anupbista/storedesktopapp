package dashboard;

import Modals.Products;
import dbConnection.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class InventoryNotiController implements Initializable {

    private Connection connection;
    private DBHandler dbHandler;
    @FXML
    private ListView<Products> notiLIstVIew;



    ObservableList<Products> notiLists = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHandler = new DBHandler();
        checkProductStock();
        notiLIstVIew.setItems(notiLists);
        notiLIstVIew.setCellFactory(notificationListView-> new NotiListController());


    }

    private void checkProductStock() {
        String sql = "SELECT * FROM products WHERE productquantity<=20";
        try {
            connection = dbHandler.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                notiLists.add(new Products(rs.getString("productID"),rs.getString("productname"),rs.getInt("productquantity")));
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


}
