package dashboard;

import Modals.Customers;
import Modals.Products;
import colors.Colors;
import com.jfoenix.controls.JFXButton;
import dbConnection.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import login.LoginController;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ActiveCustomerInfoController implements Initializable {

    @FXML
    private AnchorPane activeCustomerInfo;
    public static String selectedusername;
    private Connection connection;
    private DBHandler dbHandler;

    @FXML
    private Text totalprice;
    private int totalPrice;
    String productID;

    @FXML
    private TableView<Products> cartProducts;

    @FXML
    private TableColumn<?, ?> cartProductID;

    @FXML
    private TableColumn<?, ?> cartProductName;

    @FXML
    private TableColumn<?, ?> cartProductCat;

    @FXML
    private TableColumn<?, ?> cartProductSize;

    @FXML
    private TableColumn<?, ?> cartProductColor;

    @FXML
    private TableColumn<?, ?> cartProductBrand;

    @FXML
    private TableColumn<?, ?> cartProductQuality;

    @FXML
    private TableColumn<?, ?> cartProductPrice;

    @FXML
    private JFXButton checkoutbtn;

    ObservableList<Products> observableListCartProducts = FXCollections.observableArrayList();

    @FXML
    private Text checkoutBy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHandler = new DBHandler();
        itemsOnCart();
        calculatePrice();

        cartProductID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        cartProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        cartProductCat.setCellValueFactory(new PropertyValueFactory<>("productCat"));
        cartProductSize.setCellValueFactory(new PropertyValueFactory<>("productSize"));
        cartProductColor.setCellValueFactory(new PropertyValueFactory<>("productColor"));
        cartProductBrand.setCellValueFactory(new PropertyValueFactory<>("productBrand"));
        cartProductQuality.setCellValueFactory(new PropertyValueFactory<>("productquantity"));
        cartProductPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));

        cartProducts.setItems(observableListCartProducts);

        setExtraInfo();
        checkoutStatus();
    }

    private void checkoutStatus() {
        String sql = "SELECT userCheckout FROM activeCustomers WHERE username='"+selectedusername+"'";
        try {
            connection = dbHandler.getConnection();
            Statement st  = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
               int status = rs.getInt("userCheckout");
               if (status == 1){
                   checkoutbtn.setDisable(false);
               }else{
                   checkoutbtn.setDisable(true);
               }
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

    private void setExtraInfo() {
        checkoutBy.setText(LoginController.user);
    }

    private void itemsOnCart() {
        String sql = "SELECT * FROM productOnCart WHERE userName='"+selectedusername+"'";
        try {
            connection = dbHandler.getConnection();
            Statement st  = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int productquantity;
            String productPrice;
            while (rs.next()){
                productID = rs.getString("productID");
                productquantity  = rs.getInt("productQuantity");
                productPrice  = rs.getString("productPrice");
                String sql2 = "SELECT * FROM products WHERE productID='"+productID+"'";
                Statement st2 = connection.createStatement();
                ResultSet rs2 = st2.executeQuery(sql2);
                while (rs2.next()){
                    observableListCartProducts.add(new Products(rs2.getString("productID"),rs2.getString("productname"),rs2.getString("productcategory"),rs2.getString("productsize"),
                            rs2.getString("productcolor"),rs2.getString("productbrand"),productPrice,productquantity));
                    totalPrice+= Integer.parseInt(productPrice);
                }
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

    private void calculatePrice() {
        totalprice.setText("Rs. "+String.valueOf(totalPrice));
    }

    @FXML
    void checkout(ActionEvent event) {
        removeProductFromCart();
        checkoutbtn.setDisable(true);
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    private void updateCheckout() {
        String sql = "UPDATE activeCustomers SET staffCheckout=?, userCheckout=? WHERE username=?";
        try {
            connection = dbHandler.getConnection();
            PreparedStatement st  = connection.prepareStatement(sql);
            st.setInt(1,0);
            st.setInt(2,0);
            st.setString(3,selectedusername);
            st.executeUpdate();
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

    private void updateStaffCheckout() {
        String sql = "UPDATE activeCustomers SET staffCheckout=? WHERE username=?";
        try {
            connection = dbHandler.getConnection();
            PreparedStatement st  = connection.prepareStatement(sql);
            st.setInt(1,1);
            st.setString(2,selectedusername);
            st.executeUpdate();
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

    public void addToSales(){
        String sql = "SELECT * FROM productOnCart WHERE userName='"+selectedusername+"'";
        try {
            connection = dbHandler.getConnection();
            Statement st  = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            PreparedStatement psmt = connection.prepareStatement("INSERT into sales(saleID,username,productID,date,totalprice,productQuantity,checkoutBy,time) VALUES(?,?,?,?,?,?,?,?)");
            while (rs.next()){
                psmt.setString(1,rs.getString("userName")+rs.getString("productID")+(LocalDateTime.now().toString()));
                psmt.setString(2,rs.getString("userName"));
                psmt.setString(3,rs.getString("productID"));
                psmt.setString(4, (new LocalDate().toString()));
                psmt.setString(5,Integer.toString(Integer.parseInt(rs.getString("productQuantity"))*Integer.parseInt(rs.getString("productPrice"))));
                psmt.setString(6,rs.getString("productQuantity"));
                psmt.setString(7,LoginController.user);
                psmt.setString(8,(new LocalTime().toString()));

                psmt.executeUpdate();
            }
            System.out.println("Added to sales");
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

    private void removeProductFromCart() {
        clearBill();
        addToBill();
        addToSales();
        String sql = "DELETE FROM productOnCart WHERE userName='"+selectedusername+"'";
        try {
            connection = dbHandler.getConnection();
            Statement st  = connection.createStatement();
            int rows = st.executeUpdate(sql);
            if (rows < 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Payment Failed!!! Retry");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Payment Successful!!!");
                alert.showAndWait();
                updateStaffCheckout();

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

    private void clearBill() {
        String sql = "DELETE FROM checkoutBill WHERE userName='"+selectedusername+"'";
        try {
            connection = dbHandler.getConnection();
            Statement st  = connection.createStatement();
            int rows = st.executeUpdate(sql);
            if (rows < 1){
                System.out.println("Bill Not Cleared");
            }
            else {
                System.out.println("Bill Cleared");
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

    private void addToBill() {
        String sql = "SELECT * FROM productOnCart WHERE userName='"+selectedusername+"'";
        try {
            connection = dbHandler.getConnection();
            Statement st  = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            PreparedStatement psmt = connection.prepareStatement("INSERT into checkoutBill(userName,productID,productName,productQuantity,productPrice,checkoutBy) VALUES(?,?,?,?,?,?)");
            while (rs.next()){
                psmt.setString(1,rs.getString("userName"));
                psmt.setString(2,rs.getString("productID"));
                psmt.setString(3,rs.getString("productName"));
                psmt.setInt(4,Integer.parseInt(rs.getString("productQuantity")));
                psmt.setString(5,rs.getString("productPrice"));
                psmt.setString(6,LoginController.user);

                psmt.executeUpdate();
            }
            System.out.println("Added to BIll");
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