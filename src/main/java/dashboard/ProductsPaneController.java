package dashboard;

import Modals.Products;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import dbConnection.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import login.LoginController;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ProductsPaneController implements Initializable {

    @FXML
    private TableView<Products> allProducts;

    @FXML
    private TableColumn<?, ?> productID;

    @FXML
    private TableColumn<?, ?> productName;

    @FXML
    private TableColumn<?, ?> productDesc;

    @FXML
    private TableColumn<?, ?> productCat;

    @FXML
    private TableColumn<?, ?> productSize;

    @FXML
    private TableColumn<?, ?> productColor;

    @FXML
    private TableColumn<?, ?> productBrand;

    @FXML
    private TableColumn<?, ?> productPrice;

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
    private Boolean validationError = false;

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
    private FileInputStream fis;
    private File selectedFile;
    private File defaultSelectedFile;
    private URI defaultProductImage;
    private String productImagePath;
    @FXML
    private ImageView productImage;

    ObservableList<Products> observableListAllProducts = FXCollections.observableArrayList();

    private Connection connection;
    private DBHandler dbHandler;
    private Statement st;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHandler = new DBHandler();

        loadAllProducts();
        productID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productDesc.setCellValueFactory(new PropertyValueFactory<>("productDesc"));
        productCat.setCellValueFactory(new PropertyValueFactory<>("productCat"));
        productSize.setCellValueFactory(new PropertyValueFactory<>("productSize"));
        productColor.setCellValueFactory(new PropertyValueFactory<>("productColor"));
        productBrand.setCellValueFactory(new PropertyValueFactory<>("productBrand"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        productquantity.setCellValueFactory(new PropertyValueFactory<>("productquantity"));

        allProducts.setItems(observableListAllProducts);

        allProducts.setOnMouseClicked(event -> {
            try {
                generateQRCode();
            }catch (NullPointerException e){}
        });

    }


    private void generateQRCode() {

        String productID = allProducts.getSelectionModel().getSelectedItem().getProductID();
        String productName = allProducts.getSelectionModel().getSelectedItem().getProductName();
        String productCat = allProducts.getSelectionModel().getSelectedItem().getProductCat();
        String productSize = allProducts.getSelectionModel().getSelectedItem().getProductSize();
        String productColor = allProducts.getSelectionModel().getSelectedItem().getProductColor();
        String productBrand = allProducts.getSelectionModel().getSelectedItem().getProductBrand();
        String productPrice = allProducts.getSelectionModel().getSelectedItem().getProductPrice();
        String productDesc = allProducts.getSelectionModel().getSelectedItem().getProductDesc();

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        Gson gson = new Gson();
        Products qrcode = new Products(productID, productName, productDesc, productCat, productSize, productColor, productBrand, productPrice);

        // 1. Java object to JSON, and save into a file
        gson.toJson(qrcode);

        // 2. Java object to JSON, and assign to a String
        String jsonInString = gson.toJson(qrcode);

        int width = 800;
        int height = 800;

        BufferedImage bufferedImage = null;
        try {
            BitMatrix byteMatrix = qrCodeWriter.encode(jsonInString, BarcodeFormat.QR_CODE, width, height);
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedImage.createGraphics();

            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            System.out.println("Generated QR Code");
        } catch (WriterException ex) {
            System.out.println("Failed to generate QR Code " + ex);
        }

        ImageView qrView = new ImageView();
        qrView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));

        StackPane root = new StackPane();
        root.getChildren().add(qrView);
        Scene scene = new Scene(root);
        Stage QRCodeStage = new Stage();
        QRCodeStage.initModality(Modality.WINDOW_MODAL);
        QRCodeStage.initOwner(LoginController.mainStage);
        QRCodeStage.setScene(scene);
        QRCodeStage.show();
    }

    public void loadAllProducts() {
        String sql = "SELECT * FROM products";
        try {
            connection = dbHandler.getConnection();
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                observableListAllProducts.add(new Products(rs.getString("productID"), rs.getString("productname"),
                        rs.getString("productdesc"), rs.getString("productcategory"), rs.getString("productsize"), rs.getString("productcolor"),
                        rs.getString("productbrand"), rs.getString("productprice"), rs.getInt("productquantity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
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
        FilteredList<Products> filteredList = new FilteredList<>(observableListAllProducts, p -> true);
        searchProducts.textProperty().addListener((observable, oldvalue, newvalue) -> {
            filteredList.setPredicate(products -> {
                if (newvalue == null || newvalue.isEmpty()) {
                    return true;
                }
                String typedText = newvalue.toLowerCase();
                if (products.getProductID().toLowerCase().indexOf(typedText) != -1) {
                    return true;
                }
                if (products.getProductName().toLowerCase().indexOf(typedText) != -1) {
                    return true;
                }
                return products.getProductCat().toLowerCase().indexOf(typedText) != -1;
            });
        });
        SortedList<Products> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(allProducts.comparatorProperty());
        allProducts.setItems(sortedList);
    }

    @FXML
    void addProduct(ActionEvent event) {
        if (validationError) {
            LoginController.resetValidationErrorMessage(error_addProduct);
            validationError = false;
        }
        if (validateEmpty()) {

            if (selectedFile != null) {
                try {
                    fis = new FileInputStream(selectedFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    defaultProductImage = getClass().getResource("/img/product.png").toURI();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                try {
                    defaultSelectedFile = new File(defaultProductImage);
                    fis = new FileInputStream(defaultSelectedFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            File userImage;
            if (selectedFile != null) {
                userImage = selectedFile;
            } else {
                userImage = defaultSelectedFile;

            }

            Products products = new Products(addproductid.getText(), addproductname.getText(), addproductdesc.getText(), addproductcat.getText(), addproductsize.getText(), addproductcolor.getText(),
                    addproductbrand.getText(), addproductprice.getText(), Integer.parseInt(addproductquantity.getText()), userImage);
            String sql = "INSERT INTO products values(?,?,?,?,?,?,?,?,?,?)";
            try {
                connection = dbHandler.getConnection();
                PreparedStatement pst = connection.prepareStatement(sql);

                try {
                    pst.setString(1, products.getProductID());
                    pst.setString(2, products.getProductName());
                    pst.setString(3, products.getProductDesc());
                    pst.setString(4, products.getProductCat());
                    pst.setString(5, products.getProductSize());
                    pst.setString(6, products.getProductColor());
                    pst.setString(7, products.getProductBrand());
                    pst.setString(8, products.getProductPrice());
                    pst.setInt(9, products.getProductquantity());
                    pst.setBinaryStream(10, fis, (int) products.getProductImage().length());
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

                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to add. Try again");
                    alert.showAndWait();
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

    //    Image Uploader
    @FXML
    private void addProductImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.png", "*.jpg", "*.jpeg")); // limit fileChooser options to image files
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {

            String imageFile = null;
            try {
                imageFile = selectedFile.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            Image image = new Image(imageFile);
            productImage.setImage(image);
        } else {
            System.out.println("Cancelled");
        }
    }

    @FXML
    public void refreshloadAllProducts() {
        observableListAllProducts.clear();
        loadAllProducts();
    }

    @FXML
    void updateProduct(ActionEvent event) {
        if (validationError) {
            LoginController.resetValidationErrorMessage(error_updateProduct);
            validationError = false;
        }
        if (updateValidateEmpty()) {
            String updateSQL = "UPDATE products SET productname=?,productdesc=?,productcategory=?,productsize=?," +
                    "productcolor=?,productbrand=?,productprice=?,productquantity=? WHERE productid=?";
            try {
                connection = dbHandler.getConnection();
                PreparedStatement psmt = connection.prepareStatement(updateSQL);
                psmt.setString(1, updateproductname.getText());
                psmt.setString(2, updateproductdesc.getText());
                psmt.setString(3, updateproductcat.getText());
                psmt.setString(4, updateproductsize.getText());
                psmt.setString(5, updateproductcolor.getText());
                psmt.setString(6, updateproductbrand.getText());
                psmt.setString(7, updateproductprice.getText());
                psmt.setInt(8, Integer.parseInt(updateproductquantity.getText()));
                psmt.setString(9, updateproductID.getText());

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
        } else {

        }

    }

    @FXML
    void showUpdatePane(ActionEvent event) {
        if ((updateproductID.getText() == null || updateproductID.getText().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Enter product id to update !!!");
            alert.showAndWait();
        } else {
            String updateProductID = updateproductID.getText();
            String sql = "SELECT * FROM products where productID='" + updateProductID + "'";
            try {
                connection = dbHandler.getConnection();
                st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        if (rs.getString("productID").equals(updateProductID)) {
                            updatePane.setVisible(true);
                            updateproductname.setText(rs.getString("productname"));
                            updateproductdesc.setText(rs.getString("productdesc"));
                            updateproductcat.setText(rs.getString("productcategory"));
                            updateproductsize.setText(rs.getString("productsize"));
                            updateproductcolor.setText(rs.getString("productcolor"));
                            updateproductbrand.setText(rs.getString("productbrand"));
                            updateproductprice.setText(rs.getString("productprice"));
                            updateproductquantity.setText(String.valueOf(rs.getInt("productquantity")));
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR Dialog");
                            alert.setHeaderText(null);
                            alert.setContentText("Enter valid product id to update !!!");
                            alert.showAndWait();
                            updatePane.setVisible(false);
                        }
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Enter valid product id to update !!!");
                    alert.showAndWait();

                    updatePane.setVisible(false);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Enter valid product id to update !!!");
                alert.showAndWait();
                updatePane.setVisible(false);
                updateproductID.clear();
            } finally {
                try {
                    if (connection != null) {
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
        if ((deleteProductID.getText() == null || deleteProductID.getText().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Enter ID to delete!!!");
            alert.showAndWait();
        } else {
            String deleteproductID = deleteProductID.getText();
            String sql = "SELECT * FROM products where productID=" + deleteproductID + "";
            try {
                connection = dbHandler.getConnection();
                st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        if (rs.getString("productID").equals(deleteproductID)) {
                            try {
                                PreparedStatement statement = connection.prepareStatement("DELETE from products WHERE productID=?");
                                statement.setString(1, deleteproductID);
                                statement.executeUpdate();
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("ERROR Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Successfully Deleted");
                                alert.showAndWait();
                                deleteProductID.clear();
                                refreshloadAllProducts();

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR Dialog");
                            alert.setHeaderText(null);
                            alert.setContentText("Enter valid product id to delete !!!");
                            alert.showAndWait();

                        }
                    }
                } else {
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
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                        st.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private Boolean validateEmpty() {
        String productId = addproductid.getText();
        String productName = addproductname.getText();
        String productDesc = addproductdesc.getText();
        String productCat = addproductcat.getText();
        String productPrice = addproductprice.getText();
        String productQuantity = addproductquantity.getText();
        if ((productId == null || productId.isEmpty()) || (productName == null || productName.isEmpty()) || (productDesc == null || productDesc.isEmpty()) ||
                (productCat == null || productCat.isEmpty()) || (productPrice == null || productPrice.isEmpty()) ||
                (productQuantity == null || productQuantity.isEmpty())) {
            LoginController.setValidationErrorMessage(error_addProduct, "All Required fields must be filled");
            validationError = true;
            return false;
        } else {
            return true;
        }
    }

    private Boolean updateValidateEmpty() {
        String updateproductId = updateproductID.getText();
        String updateproductName = updateproductname.getText();
        String updateproductDesc = updateproductdesc.getText();
        String updateproductCat = updateproductcat.getText();
        String updateproductPrice = updateproductprice.getText();
        String updateproductQuantity = updateproductquantity.getText();
        if ((updateproductId == null || updateproductId.isEmpty()) || (updateproductName == null || updateproductName.isEmpty()) || (updateproductDesc == null || updateproductDesc.isEmpty()) ||
                (updateproductCat == null || updateproductCat.isEmpty()) || (updateproductPrice == null || updateproductPrice.isEmpty()) ||
                (updateproductQuantity == null || updateproductQuantity.isEmpty())) {
            LoginController.setValidationErrorMessage(error_updateProduct, "All Required fields must be filled");
            validationError = true;
            return false;
        } else {
            return true;
        }
    }
}