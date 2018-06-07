package dashboard;

import Modals.Products;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class NotiListController extends ListCell<Products> {
    @FXML
    private Text productID;

    @FXML
    private Text productName;

    @FXML
    private Text productQuantity;
    @FXML
    private StackPane notiVIew;
    private FXMLLoader loader;
    @Override
    protected void updateItem(Products item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null){
            setText(null);
            setGraphic(null);
        }

        else{
            if (loader == null){
                    loader = new FXMLLoader(getClass().getResource("/ListNoti.fxml"));
                    loader.setController(this);
                try {
                    loader.load();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            productID.setText(item.getProductID());
            productName.setText(item.getProductName());
            productQuantity.setText(String.valueOf(item.getProductquantity()));

            setText(null);
            setGraphic(notiVIew);
        }
    }


}