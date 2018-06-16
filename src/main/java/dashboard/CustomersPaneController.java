package dashboard;

import com.jfoenix.controls.JFXTextField;
import dbConnection.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Modals.Customers;
import login.LoginController;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


public class CustomersPaneController implements Initializable {

    private Parent activeCustomerInfoPane;
    @FXML
    private JFXTextField searchCustomer;

    @FXML
    private TableView<Customers> a_customersLists;

    @FXML
    private TableColumn<?, ?> a_customer_username;

    @FXML
    private TableView<Customers> allCustomerLists;

    @FXML
    private TableColumn<?, ?> customer_username;

    @FXML
    private TableColumn<?, ?> customer_firstname;

    @FXML
    private TableColumn<?, ?> customer_lastname;

    @FXML
    private TableColumn<?, ?> customer_address;

    @FXML
    private TableColumn<?, ?> customer_phone;

    @FXML
    private TableColumn<?, ?> customer_email;

    ObservableList<Customers> observableList = FXCollections.observableArrayList();

    ObservableList<Customers> observableLists = FXCollections.observableArrayList();

    private Connection connection;
    private DBHandler dbHandler;
    private Statement st;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHandler = new DBHandler();
        activeCustomerLists();
        customerLists();

        a_customer_username.setCellValueFactory(new PropertyValueFactory<>("username"));

        customer_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        customer_firstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        customer_lastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        customer_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        customer_phone.setCellValueFactory(new PropertyValueFactory<>("phonenumber"));
        customer_email.setCellValueFactory(new PropertyValueFactory<>("email"));

        a_customersLists.setItems(observableList);
        allCustomerLists.setItems(observableLists);

        a_customersLists.setOnMouseClicked(event -> {
            try {
                activeCustomerInfo();
            }catch (NullPointerException e){}
        });

        a_customersLists.getSelectionModel().select(0);
    }

    private void activeCustomerInfo(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ActiveCustomerInfo.fxml"));

            ActiveCustomerInfoController activeCustomerInfoController = loader.getController();
            activeCustomerInfoController.selectedusername = a_customersLists.getSelectionModel().getSelectedItem().getUsername();

            activeCustomerInfoPane = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(LoginController.mainStage);
            stage.setTitle("Customer Information ["+activeCustomerInfoController.selectedusername+"]");
            stage.setScene(new Scene(activeCustomerInfoPane));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void refreshloadActiveCustomers() {
        observableList.clear();
        activeCustomerLists();
        System.out.println("Reloaded");
    }

    private void customerLists(){
        String sql = "SELECT * FROM customers";
        try {
            connection = dbHandler.getConnection();
            st  = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                observableLists.add(new Customers(rs.getString("username"), rs.getString("first_name"),
                        rs.getString("last_name"), rs.getString("address"), rs.getString("phone_number"), rs.getString("email")));
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
    private void activeCustomerLists(){
        String sql = "SELECT * FROM activeCustomers";
        try {
            connection = dbHandler.getConnection();
            st  = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                observableList.add(new Customers(rs.getString("username")));
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
    void searchActiveCustomers(KeyEvent event) {
        FilteredList<Customers> filteredList = new FilteredList<>(observableList,p-> true);
        searchCustomer.textProperty().addListener((observable,oldvalue,newvalue)->{
           filteredList.setPredicate(customers -> {
               if(newvalue == null || newvalue.isEmpty()){
                   return true;
               }
               String typedText = newvalue.toLowerCase();
               if (customers.getUsername().toLowerCase().indexOf(typedText) != -1){
                   return true;
               }
               return false;
           });
        });
        SortedList<Customers> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(a_customersLists.comparatorProperty());
        a_customersLists.setItems(sortedList);
    }


}
