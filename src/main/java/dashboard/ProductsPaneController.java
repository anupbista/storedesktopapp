package dashboard;

import Modals.Customers;
import Modals.Products;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import dbConnection.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import login.LoginController;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ProductsPaneController implements Initializable {

    @FXML
    private TableView<Products> allProducts;

    @FXML
    private TableColumn<?, ?> productID;

    @FXML
    private TableColumn<?, ?> productname;

    @FXML
    private TableColumn<?, ?> productdesc;

    @FXML
    private TableColumn<?, ?> productcat;

    @FXML
    private TableColumn<?, ?> productsize;

    @FXML
    private TableColumn<?, ?> productcolor;

    @FXML
    private TableColumn<?, ?> productbrand;

    @FXML
    private TableColumn<?, ?> productprice;

    @FXML
    private TableColumn<?, ?> productquantity;
    @FXML
    private JFXTextField searchProducts;

//    add products
    @FXML
    private JFXTextField addproductid;

    @FXML
    private JFXTextField addproductname;

    @FXML
    private JFXTextArea addproductdesc;

    @FXML
    private JFXTextField addproductsize;

    @FXML
    private JFXTextField addproductcat;
    @FXML
    private JFXTextField addproductcolor;

    @FXML
    private JFXTextField addproductbrand;

    @FXML
    private JFXTextField addproductprice;

    @FXML
    private JFXTextField addproductquantity;
    @FXML
    private Label error_addProduct;
    private Boolean validationError=false;

    @FXML
    private JFXTextField updateproductID;

    @FXML
    private Pane updatePane;

    @FXML
    private JFXTextField updateproductname;

    @FXML
    private JFXTextArea updateproductdesc;

    @FXML
    private JFXTextField updateproductcat;

    @FXML
    private JFXTextField updateproductsize;

    @FXML
    private JFXTextField updateproductcolor;

    @FXML
    private JFXTextField updateproductbrand;

    @FXML
    private JFXTextField updateproductprice;

    @FXML
    private JFXTextField updateproductquantity;

    @FXML
    private Label error_updateProduct;
    @FXML
    private JFXTextField deleteProductID;


    ObservableList<Products> observableListAllProducts = FXCollections.observableArrayList();

    private Connection connection;
    private DBHandler dbHandler;
    private Statement st;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHandler = new DBHandler();

        loadAllProducts();
        productID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productname.setCellValueFactory(new PropertyValueFactory<>("productname"));
        productdesc.setCellValueFactory(new PropertyValueFactory<>("productdesc"));
        productcat.setCellValueFactory(new PropertyValueFactory<>("productcategory"));
        productsize.setCellValueFactory(new PropertyValueFactory<>("productsize"));
        productcolor.setCellValueFactory(new PropertyValueFactory<>("productcolor"));
        productbrand.setCellValueFactory(new PropertyValueFactory<>("productbrand"));
        productprice.setCellValueFactory(new PropertyValueFactory<>("productprice"));
        productquantity.setCellValueFactory(new PropertyValueFactory<>("productquantity"));

        allProducts.setItems(observableListAllProducts);
    }

    public void loadAllProducts() {
        String sql = "SELECT * FROM products";
        try {
            connection = dbHandler.getConnection();
            st  = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                observableListAllProducts.add(new Products(rs.getString("productID"), rs.getString("productname"),
                        rs.getString("productdesc"), rs.getString("productcategory"), rs.getString("productsize"), rs.getString("productcolor"),
                        rs.getString("productbrand"),rs.getString("productprice"),rs.getInt("productquantity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (connection!=null){
                    connection.close();
                    st.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    void searchAllProducts(KeyEvent event) {
        FilteredList<Products> filteredList = new FilteredList<>(observableListAllProducts, p-> true);
        searchProducts.textProperty().addListener((observable,oldvalue,newvalue)->{
            filteredList.setPredicate(products -> {
                if(newvalue == null || newvalue.isEmpty()){
                    return true;
                }
                String typedText = newvalue.toLowerCase();
                if (products.getProductID().toLowerCase().indexOf(typedText) != -1){
                    return true;
                }
                if (products.getProductname().toLowerCase().indexOf(typedText) != -1){
                    return true;
                }
                if (products.getProductcategory().toLowerCase().indexOf(typedText) != -1){
                    return true;
                }
                return false;
            });
        });
        SortedList<Products> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(allProducts.comparatorProperty());
        allProducts.setItems(sortedList);
    }

    @FXML
    void addProduct(ActionEvent event) {
        if(validationError){
            LoginController.resetValidationErrorMessage(error_addProduct);
            validationError=false;
        }
        if (validateEmpty()){
            Products products = new Products(addproductid.getText(),addproductname.getText(),addproductdesc.getText(),addproductcat.getText(),addproductsize.getText(),addproductcolor.getText(),
                    addproductbrand.getText(),addproductprice.getText(),Integer.parseInt(addproductquantity.getText()));
            String sql = "INSERT INTO products values(?,?,?,?,?,?,?,?,?)";
            try {
                connection = dbHandler.getConnection();
                PreparedStatement pst= connection.prepareStatement(sql);

                try {
                    pst.setString(1,products.getProductID());
                    pst.setString(2,products.getProductname());
                    pst.setString(3,products.getProductdesc());
                    pst.setString(4,products.getProductcategory());
                    pst.setString(5,products.getProductsize());
                    pst.setString(6,products.getProductcolor());
                    pst.setString(7,products.getProductbrand());
                    pst.setString(8,products.getProductprice());
                    pst.setInt(9,products.getProductquantity());
                    pst.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added");
                    alert.showAndWait();

                    addproductid.clear();
                    addproductname.clear();
                    addproductdesc.clear();
                    addproductcat.clear();
                    addproductsize.clear();
                    addproductcolor.clear();
                    addproductbrand.clear();
                    addproductprice.clear();
                    addproductquantity.clear();
                    refreshloadAllProducts();

                }
                catch (SQLException e){
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to add. Try again");
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
    }


    @FXML
    public void refreshloadAllProducts() {
        observableListAllProducts.clear();
        loadAllProducts();
    }

    @FXML
    void updateProduct(ActionEvent event) {
        if(validationError){
            LoginController.resetValidationErrorMessage(error_updateProduct);
            validationError=false;
        }
        if (updateValidateEmpty()){
            String updateSQL = "UPDATE products SET productname=?,productdesc=?,productcategory=?,productsize=?," +
                    "productcolor=?,productbrand=?,productprice=?,productquantity=? WHERE productid=?";
            try{
                connection = dbHandler.getConnection();
                PreparedStatement psmt = connection.prepareStatement(updateSQL);
                psmt.setString(1,updateproductname.getText());
                psmt.setString(2,updateproductdesc.getText());
                psmt.setString(3,updateproductcat.getText());
                psmt.setString(4,updateproductsize.getText());
                psmt.setString(5,updateproductcolor.getText());
                psmt.setString(6,updateproductbrand.getText());
                psmt.setString(7,updateproductprice.getText());
                psmt.setInt(8,Integer.parseInt(updateproductquantity.getText()));
                psmt.setString(9,updateproductID.getText());

                psmt.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!!!");
                alert.showAndWait();
                updatePane.setVisible(false);
                updateproductID.clear();
                refreshloadAllProducts();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{

        }

    }

    @FXML
    void showUpdatePane(ActionEvent event) {
        if ((updateproductID.getText() == null || updateproductID.getText().isEmpty())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Enter product id to update !!!");
            alert.showAndWait();
        }else{
            String updateProductID = updateproductID.getText();
            String sql = "SELECT * FROM products where productID="+updateProductID+"";
            try {
                connection = dbHandler.getConnection();
                st  = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if (rs.isBeforeFirst()){
                    while (rs.next()){
                        if (rs.getString("productID").equals(updateProductID)){
                            updatePane.setVisible(true);
                            updateproductname.setText(rs.getString("productname"));
                            updateproductdesc.setText(rs.getString("productdesc"));
                            updateproductcat.setText(rs.getString("productcategory"));
                            updateproductsize.setText(rs.getString("productsize"));
                            updateproductcolor.setText(rs.getString("productcolor"));
                            updateproductbrand.setText(rs.getString("productbrand"));
                            updateproductprice.setText(rs.getString("productprice"));
                            updateproductquantity.setText(String.valueOf(rs.getInt("productquantity")));
                        }
                        else{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR Dialog");
                            alert.setHeaderText(null);
                            alert.setContentText("Enter valid product id to update !!!");
                            alert.showAndWait();
                            updatePane.setVisible(false);
                        }
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Enter valid product id to update !!!");
                    alert.showAndWait();
                    updatePane.setVisible(false);
                }

            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Enter valid product id to delete !!!");
                alert.showAndWait();
                updatePane.setVisible(false);
                updateproductID.clear();
            }
            finally {
                try {
                    if (connection!=null){
                        connection.close();
                        st.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void toggleUpdatepane(KeyEvent event) {
        updatePane.setVisible(false);
    }

    @FXML
    void deleteproduct(ActionEvent event) {
        if ((deleteProductID.getText() == null || deleteProductID.getText().isEmpty())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Enter ID to delete!!!");
            alert.showAndWait();
        }else{
            String deleteproductID = deleteProductID.getText();
            String sql = "SELECT * FROM products where productID="+deleteproductID+"";
            try {
                connection = dbHandler.getConnection();
                st  = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if (rs.isBeforeFirst()){
                    while (rs.next()){
                        if (rs.getString("productID").equals(deleteproductID)){
                            try{
                                PreparedStatement statement = connection.prepareStatement("DELETE from products WHERE productID=?");
                                statement.setString(1,deleteproductID);
                                statement.executeUpdate();
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("ERROR Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Successfully Deleted");
                                alert.showAndWait();
                                deleteProductID.clear();
                                refreshloadAllProducts();

                            }
                            catch (SQLException e){
                                e.printStackTrace();
                            }
                        }
                        else{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR Dialog");
                            alert.setHeaderText(null);
                            alert.setContentText("Enter valid product id to delete !!!");
                            alert.showAndWait();

                        }
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Enter valid product id to delete !!!");
                    alert.showAndWait();
                }

            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Enter valid product id to delete !!!");
                alert.showAndWait();
            }
            finally {
                try {
                    if (connection!=null){
                        connection.close();
                        st.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private Boolean validateEmpty(){
        String productId = addproductid.getText();
        String productName = addproductname.getText();
        String productDesc = addproductdesc.getText();
        String productCat = addproductcat.getText();
        String productPrice = addproductprice.getText();
        String productQuantity = addproductquantity.getText();
        if((productId == null || productId.isEmpty()) || (productName == null || productName.isEmpty()) || (productDesc == null || productDesc.isEmpty()) ||
                (productCat == null || productCat.isEmpty()) || (productPrice == null || productPrice.isEmpty()) ||
                (productQuantity == null || productQuantity.isEmpty()) ) {
            LoginController.setValidationErrorMessage(error_addProduct, "All Required fields must be filled");
            validationError=true;
            return false;
        }else{
            return true;
        }
    }

    private Boolean updateValidateEmpty(){
        String updateproductId = updateproductID.getText();
        String updateproductName = updateproductname.getText();
        String updateproductDesc = updateproductdesc.getText();
        String updateproductCat = updateproductcat.getText();
        String updateproductPrice = updateproductprice.getText();
        String updateproductQuantity = updateproductquantity.getText();
        if((updateproductId == null || updateproductId.isEmpty()) || (updateproductName == null || updateproductName.isEmpty()) || (updateproductDesc == null || updateproductDesc.isEmpty()) ||
                (updateproductCat == null || updateproductCat.isEmpty()) || (updateproductPrice == null || updateproductPrice.isEmpty()) ||
                (updateproductQuantity == null || updateproductQuantity.isEmpty()) ) {
            LoginController.setValidationErrorMessage(error_updateProduct, "All Required fields must be filled");
            validationError=true;
            return false;
        }else{
            return true;
        }
    }
}