package dashboard;

import Modals.Order;
import Modals.Products;
import com.jfoenix.controls.JFXButton;
import dbConnection.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import login.LoginController;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

public class OrderProcessingInfoController implements Initializable {


    @FXML
    private TableView<Order> orderProducts;

    @FXML
    private TableColumn<?, ?> orderProductID;

    @FXML
    private TableColumn<?, ?> orderProductName;

    @FXML
    private TableColumn<?, ?> orderProductSize;

    @FXML
    private TableColumn<?, ?> orderProductColor;

    @FXML
    private TableColumn<?, ?> orderProductQuality;

    @FXML
    private TableColumn<?, ?> orderProductPrice;

    @FXML
    private Text orderUsername;

    @FXML
    private Text orderName;

    @FXML
    private Text orderAddress;

    @FXML
    private Text orderPhone;
    @FXML
    private Text orderDate;

    @FXML
    private Text orderTIme;
    @FXML
    private JFXButton completedbtn;

    private Connection connection;
    public static String orderID;
    private DBHandler dbHandler;

    @FXML
    private Text totalprice;
    private int totalPrice;
    String username;


    ObservableList<Order> observableListOrderProducts = FXCollections.observableArrayList();

    @FXML
    private Text checkoutBy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHandler = new DBHandler();
        itemsOnOrder();
        calculatePrice();

        orderProductID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        orderProductName.setCellValueFactory(new PropertyValueFactory<>("productname"));
        orderProductSize.setCellValueFactory(new PropertyValueFactory<>("orderSize"));
        orderProductColor.setCellValueFactory(new PropertyValueFactory<>("orderColor"));
        orderProductQuality.setCellValueFactory(new PropertyValueFactory<>("productquantity"));
        orderProductPrice.setCellValueFactory(new PropertyValueFactory<>("orderPrice"));


        orderProducts.setItems(observableListOrderProducts);

        setExtraInfo();
    }

    private void getUserInfo() {
        String sql = "SELECT * FROM customers WHERE username='" + username + "'";
        try {
            connection = dbHandler.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                orderName.setText(rs.getString("first_name")+" "+rs.getString("last_name"));
                orderAddress.setText(rs.getString("address"));
                orderPhone.setText(rs.getString("phone_number"));
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

    private void setExtraInfo() {
        checkoutBy.setText(LoginController.user);
    }


    private void itemsOnOrder() {
        String sql = "SELECT * FROM orders WHERE orderID='" + orderID + "'";
        try {
            connection = dbHandler.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            String productPrice;
            while (rs.next()) {
                username = rs.getString("username");
                orderUsername.setText(rs.getString("username"));
                orderDate.setText(String.valueOf(rs.getDate("orderDate")));
                orderTIme.setText(rs.getString("orderTime"));
                String sql2 = "SELECT * FROM products WHERE productID='" + rs.getString("productID") + "'";
                Statement st2 = connection.createStatement();
                ResultSet rs2 = st2.executeQuery(sql2);
                while (rs2.next()) {
                    productPrice = rs2.getString("productprice");
                    observableListOrderProducts.add(new Order(rs.getString("orderID"), rs2.getString("productname"), rs2.getString("productsize"), rs2.getString("productcolor"), rs2.getString("productquantity"), rs2.getString("productprice")));
                    totalPrice += Integer.parseInt(productPrice);
                }
            }
            getUserInfo();
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

    private void calculatePrice() {
        totalprice.setText("Rs. " + String.valueOf(totalPrice));
    }

    @FXML
    void completed(ActionEvent event) {
        String sql = "UPDATE newOrder SET status=? WHERE newOrderID=?";
        String sql2 = "UPDATE orders SET status=? WHERE orderID=?";
        try {
            connection = dbHandler.getConnection();
            PreparedStatement st  = connection.prepareStatement(sql);
            PreparedStatement st2  = connection.prepareStatement(sql2);
            st.setString(1,"completed");
            st.setString(2, orderID);
            st.executeUpdate();

            st2.setString(1,"completed");
            st2.setString(2, orderID);
            st2.executeUpdate();

            removeProductFromnewOrder();
            removeProductFromOrders();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (connection!=null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        completedbtn.setDisable(true);
        System.out.println("Delivery Completed");
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    private void removeProductFromOrders() {
        String sql = "DELETE FROM orders WHERE orderID='"+orderID+"'";
        try {
            connection = dbHandler.getConnection();
            Statement st  = connection.createStatement();
            int rows = st.executeUpdate(sql);
            if (rows < 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Delivery Completion Failed!!! Retry");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Delivery Completed!!!");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (connection!=null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeProductFromnewOrder() {
        String sql = "DELETE FROM newOrder WHERE newOrderID='"+orderID+"'";
        try {
            connection = dbHandler.getConnection();
            Statement st  = connection.createStatement();
            int rows = st.executeUpdate(sql);
            if (rows < 1){
                System.out.println("Failed deleting from new order table");
            }
            else {
                System.out.println("Successfully delete from new order table");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (connection!=null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}