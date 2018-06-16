package dashboard;

import Modals.Products;
import dbConnection.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
    private Pane emptyMessage;


    @FXML
    private TableView<Products> OutofStockList;

    @FXML
    private TableColumn<?, ?> productID;

    @FXML
    private TableColumn<?, ?> productName;

    @FXML
    private TableColumn<?, ?> productQuantity;

    @FXML
    private ScrollPane stockPane;

    ObservableList<Products> outOfStock = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHandler = new DBHandler();
        checkProductStock();
        productID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productQuantity.setCellValueFactory(new PropertyValueFactory<>("productquantity"));

        OutofStockList.setItems(outOfStock);
    }

    private void checkProductStock() {
        String sql = "SELECT * FROM products WHERE productquantity<=20";
        try {
            connection = dbHandler.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.isBeforeFirst()) {
                stockPane.setVisible(true);
                emptyMessage.setVisible(false);
                while (rs.next()) {
                    outOfStock.add(new Products(rs.getString("productID"), rs.getString("productname"), rs.getInt("productquantity")));
                }
            }
            else {
                stockPane.setVisible(false);
                emptyMessage.setVisible(true);
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
