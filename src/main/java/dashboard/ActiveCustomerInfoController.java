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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import login.LoginController;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.*;
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

    ObservableList<Products> observableListCartProducts = FXCollections.observableArrayList();

    @FXML
    private Text checkoutBy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHandler = new DBHandler();
        itemsOnCart();
        calculatePrice();

        cartProductID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        cartProductName.setCellValueFactory(new PropertyValueFactory<>("productname"));
        cartProductCat.setCellValueFactory(new PropertyValueFactory<>("productcategory"));
        cartProductSize.setCellValueFactory(new PropertyValueFactory<>("productsize"));
        cartProductColor.setCellValueFactory(new PropertyValueFactory<>("productcolor"));
        cartProductBrand.setCellValueFactory(new PropertyValueFactory<>("productbrand"));
        cartProductQuality.setCellValueFactory(new PropertyValueFactory<>("productquantity"));
        cartProductPrice.setCellValueFactory(new PropertyValueFactory<>("productprice"));

        cartProducts.setItems(observableListCartProducts);

        setExtraInfo();
    }

    private void setExtraInfo() {
        checkoutBy.setText(LoginController.user);
    }

    private void itemsOnCart() {
        String sql = "SELECT * FROM productOnCart WHERE username='"+selectedusername+"'";
        try {
            connection = dbHandler.getConnection();
            Statement st  = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int productquantity;
            while (rs.next()){
                productID = rs.getString("productID");
                productquantity  = rs.getInt("productquantity");
                String sql2 = "SELECT * FROM products WHERE productID='"+productID+"'";
                Statement st2 = connection.createStatement();
                ResultSet rs2 = st2.executeQuery(sql2);
                while (rs2.next()){
                    observableListCartProducts.add(new Products(rs2.getString("productID"),rs2.getString("productname"),rs2.getString("productcategory"),rs2.getString("productsize"),
                            rs2.getString("productcolor"),rs2.getString("productbrand"),rs2.getString("productprice"),rs2.getInt("productquantity")));
                    totalPrice+= Integer.parseInt(rs2.getString("productprice"))*productquantity;
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

//    private void itemsOnCart(){
//        try {
//            connection = dbHandler.getConnection();
//            Statement st = connection.createStatement();
//            String sql = "SELECT * FROM productOnCart WHERE username='"+selectedusername+"'";
//            ResultSet rs = st.executeQuery(sql);
//            int productquantity;
//            int i = 0;
//            while (rs.next()){
//                productID = rs.getString("productID");
//                productquantity  = rs.getInt("productquantity");
//                String sql2 = "SELECT * FROM products WHERE productID='"+productID+"'";
//                Statement st2 = connection.createStatement();
//                ResultSet rs2 = st2.executeQuery(sql2);
//                while (rs2.next()){
//                    Text productid = new Text(rs2.getString("productID"));
//                    Text productname = new Text(rs2.getString("productname"));
//                    Text productprice = new Text("Rs: "+rs2.getString("productprice"));
//                    Text productq = new Text(String.valueOf(productquantity));
//                    totalPrice+= Integer.parseInt(rs2.getString("productprice"))*productquantity;
//                    TilePane tile = new TilePane();
//                    tile.setPadding(new Insets(10,10,10,10));
//                    tile.setStyle("-fx-background-color:#eeeeee;");
//                    tile.setLayoutY(i*80);
//                    tile.setPrefHeight(60);
//                    tile.setPrefWidth(600);
//                    tile.setHgap(30);
//                    tile.setVgap(10);
//                    tile.getChildren().add(productid);
//                    tile.getChildren().add(productname);
//                    tile.getChildren().add(productprice);
//                    tile.getChildren().add(productq);
//                    activeCustomerInfo.getChildren().add(tile);
//                    i++;
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    @FXML
    void checkout(ActionEvent event) {
//        try {
//            connection = dbHandler.getConnection();
//            String sql = "UPDATE products SET productquantity=? WHERE productid='"+productID+"'";
//            PreparedStatement pst = connection.prepareStatement(sql);
//            pst.setInt();
//            int rows = st.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}