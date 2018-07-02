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
    private LineChart<?, ?> salesLineChart;

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

        XYChart.Series salesSeries = new XYChart.Series();
        for (int i = 0; i < 10; i++) {
            salesSeries.getData().add(new XYChart.Data(""+i+"",i+1));
        }

        salesLineChart.getData().addAll(salesSeries);

    }

    private void getSalesData() {
        String sql = "select convert(char(3), sales.date, 0)";
        try {
            connection = dbHandler.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    System.out.println(rs.getString("date"));
                }
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
