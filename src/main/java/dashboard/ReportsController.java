package dashboard;

import Modals.Products;
import dbConnection.DBHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ReportsController implements Initializable{

    @FXML
    private LineChart<String, Integer> salesLineChart;

    @FXML
    private CategoryAxis salesLineChartX;

    @FXML
    private NumberAxis salesLineChartY;
    private Connection connection;
    private DBHandler dbHandler;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHandler = new DBHandler();
        getSalesData();
    }

    private void getSalesData() {
        XYChart.Series<String, Integer> salesSeries = new XYChart.Series<>();
//        String sql = "select * from sales order by date desc limit 10";
        String sql = "SELECT date, sum(productQuantity) FROM sales group by date desc limit 10";
        try {
            connection = dbHandler.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    System.out.println(rs.getString(1)+"    "+rs.getString(2));
                    salesSeries.getData().add(new XYChart.Data<>(rs.getString(1),rs.getInt(2)));
                }
                salesLineChart.getData().addAll(salesSeries);
            }
            else {
//                if empty
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
