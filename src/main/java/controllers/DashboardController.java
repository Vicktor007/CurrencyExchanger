package controllers;

import ApiConnection.*;
import ApiParsers.FixerJsonParser;
import ApiParsers.OpenExchangeJsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.Currency;
import models.CurrencyHistory;
import repository.Converter;
import repository.ConverterImp;
import repository.CurrencyApiProvider;
import repository.CurrencyJsonParser;
import services.CurrencyHistoryService;
import services.CurrencyService;
import utility.ConfigLoader;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController implements Initializable {

    @FXML
    private AnchorPane anchorPaneConv;

    @FXML
    private AnchorPane anchorPaneHistCurr;

    @FXML
    private AnchorPane anchorPaneSymbCurr;

    @FXML
    private TableColumn<Currency, String> col1Symbols;

    @FXML
    private TableColumn<Currency, String> col2Signification;

    @FXML
    private ComboBox comboBoxChooSymb;

    @FXML
    private ComboBox comboBoxFrom;

    @FXML
    private ComboBox comboBoxTo;

    @FXML
    private Text convLabel1;

    @FXML
    private Text convLabel2;

    @FXML
    private Text convLabel3;

    @FXML
    private Button convertBtn;

    @FXML
    private LineChart<?, ?> lineChart;

    @FXML
    private Button sbConvBtn;

    @FXML
    private Button sbHistCurr;

    @FXML
    private Button sbSymbCurr;

    @FXML
    private Button searchBtn;

    @FXML
    private TableView<Currency> tableView;

    @FXML
    private Text tbTitle1;

    @FXML
    private Text tbTitle2;

    @FXML
    private TextField txFieldAmount;

    @FXML
    private TextField txFieldSearch;



    CurrencyApiProvider provider;
    CurrencyJsonParser parser;
    List<Currency> currencyList;
    CurrencyService currencyService;
    private final Integer DAYS=8;
    private final String BASE="EUR";


//    private Converter resolveProvider(String fixerUrl, String fixerKey, String openUrl, String openKey) {
//        try {
//            CurrencyApiProvider primary = new ApiConnection(fixerUrl, fixerKey);
//            CurrencyJsonParser fixerParser = new FixerJsonParser();
//
//            // Test with a lightweight ping request
//            primary.getSymbolsWithSignification();
//            System.out.println("✅ Primary API available (Fixer)");
//            return new ConverterImp(primary, fixerParser);
//
//        } catch (Exception ex) {
//            System.out.println("⚠️ Primary API failed: " + ex.getMessage() + "\nSwitching to fallback...");
//
//            CurrencyApiProvider fallback = new OpenExchangeApiConnection(openUrl, openKey);
//            CurrencyJsonParser openParser = new OpenExchangeJsonParser();
//            return new ConverterImp(fallback, openParser);
//        }
//    }

    private Converter resolveProvider(String fixerUrl, String fixerKey, String openUrl, String openKey) {
        CurrencyApiProvider primary = null;
        CurrencyJsonParser fixerParser = new FixerJsonParser();

        try {
            primary = new ApiConnection(fixerUrl, fixerKey);
            primary.getSymbolsWithSignification(); // Ping test
            System.out.println("✅ Primary API available (Fixer)");
            return new ConverterImp(primary, fixerParser);
        } catch (Exception ex) {
            System.out.println("⚠️ Primary API failed: " + ex.getMessage());
        }

        // Fallback logic
        System.out.println("🔄 Switching to fallback API...");
        try {
            CurrencyApiProvider fallback = new OpenExchangeApiConnection(openUrl, openKey);
            CurrencyJsonParser openParser = new OpenExchangeJsonParser();
            return new ConverterImp(fallback, openParser);
        } catch (Exception fallbackEx) {
            System.out.println("⛔ Fallback API also failed: " + fallbackEx.getMessage());
            throw new RuntimeException("Both APIs failed to initialize.");
        }
    }



    @Override
    public void initialize(URL uri, ResourceBundle resourceBundle) {
        try {
            String fixerUrl = ConfigLoader.get("fixer.api.url");
            String fixerKey = ConfigLoader.get("fixer.api.key");
            String openUrl = ConfigLoader.get("open.api.url");
            String openKey = ConfigLoader.get("open.api.key");


            Converter converter = resolveProvider(fixerUrl, fixerKey, openUrl, openKey);
            currencyService = new CurrencyService(converter);
            CurrencyHistoryService currencyHistoryService = new CurrencyHistoryService(converter);


//            Converter converter = new ConverterImp(provider, parser);
//            currencyService = new CurrencyService(converter);
//            CurrencyHistoryService currencyHistoryService = new CurrencyHistoryService(converter);

            currencyList = currencyService.getAllCurrencies();
            List<String> symbolsList = currencyService.getAllSymbolsAndSignifications(currencyList);
            comboBoxFrom.getItems().addAll(symbolsList);
            comboBoxTo.getItems().addAll(symbolsList);
            comboBoxChooSymb.getItems().addAll(symbolsList);
            fillCurrencyTableView(currencyList);

//            CurrencyHistoryService currencyHistoryService = new CurrencyHistoryService(converter);
            comboBoxChooSymb.setOnAction(event -> {
                try {

                    String rawSymbol = comboBoxChooSymb.getSelectionModel().getSelectedItem().toString();
                    String symbol = rawSymbol.substring(rawSymbol.lastIndexOf(" ") + 1);
                    List<CurrencyHistory> list = currencyHistoryService.currencyHistoryData(BASE, DAYS, symbol);
                    drawChart(list,symbol);
                } catch (IOException ex) {
                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillCurrencyTableView(List<Currency> currencyList) {
        col1Symbols.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        col2Signification.setCellValueFactory(new PropertyValueFactory<>("signification"));
        ObservableList<Currency> currencyObservableList = FXCollections.observableArrayList(currencyList);
        tableView.setItems(currencyObservableList);

    }

    @FXML
    void eventComboBoxGraph(ActionEvent event) {

    }


    @FXML
    void eventConvBtn(ActionEvent event) {
        try {
            String fromEntry = comboBoxFrom.getSelectionModel().getSelectedItem().toString();
            String toEntry = comboBoxTo.getSelectionModel().getSelectedItem().toString();

            // Extract last word (symbol) from the selected string
            String from = fromEntry.substring(fromEntry.lastIndexOf(" ") + 1);
            String to = toEntry.substring(toEntry.lastIndexOf(" ") + 1);

            Double amount = Double.parseDouble(txFieldAmount.getText());
            Double result = currencyService.convert(from, to, amount);

            convLabel1.setText(amount + " " + fromEntry);
            convLabel2.setText(result + " " + toEntry);
            convLabel3.setText("1 " + toEntry + " = " + amount / result + " " + fromEntry);

            convLabel1.setVisible(true);
            convLabel2.setVisible(true);
            convLabel3.setVisible(true);
        } catch (Exception e) {
            alertMessage();
            System.err.println(e.getMessage());
        }
    }



//    void eventConvBtn(ActionEvent event) {
//        try{
//            String from=comboBoxFrom.getSelectionModel().getSelectedItem().toString();
//            String to=comboBoxTo.getSelectionModel().getSelectedItem().toString();
//            Double amount=Double.parseDouble(txFieldAmount.getText());
//            Double result=currencyService.convert(from, to, amount);
//            convLabel1.setText(amount+" "+from);
//            convLabel2.setText(result+" "+to);
//            convLabel3.setText("1 "+to +" = "+amount/result+" "+from);
//            convLabel1.setVisible(true);
//            convLabel2.setVisible(true);
//            convLabel3.setVisible(true);
//        }catch(Exception e){
//            alertMessage();
//            System.err.println(e.getMessage());
//        }
//    }

    @FXML
    void eventSearchBtn(ActionEvent event) {
        String symbol=txFieldSearch.getText();
        if(currencyList==null)
            throw new IllegalStateException("the list currency should not be null");
        List<Currency> list=currencyService.findCurrency(currencyList, symbol);
        fillCurrencyTableView(list);

    }

    @FXML
    void eventSideBar(ActionEvent event) {
        if(event.getSource() == sbConvBtn){
            anchorPaneConv.setVisible(true);
            anchorPaneSymbCurr.setVisible(false);
            anchorPaneHistCurr.setVisible(false);
           tbTitle1.setText("Home/Converter");
           tbTitle2.setText("Converter");
        } else if(event.getSource() == sbSymbCurr){
            anchorPaneConv.setVisible(false);
            anchorPaneSymbCurr.setVisible(true);
            anchorPaneHistCurr.setVisible(false);
            tbTitle1.setText("Home/Currency Symbols");
            tbTitle2.setText("Currency Symbols");
        } else if(event.getSource() == sbHistCurr){
            anchorPaneConv.setVisible(false);
            anchorPaneSymbCurr.setVisible(false);
            anchorPaneHistCurr.setVisible(true);
            tbTitle1.setText("Home/Currency History");
            tbTitle2.setText("Currency History");
        }
    }
    private void alertMessage() {
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.setTitle("invalid data input");
        alert.setContentText("please fill all the fields");
        alert.showAndWait();
    }

    private void drawChart(List<CurrencyHistory> list, String symbol) {
        XYChart.Series series=new XYChart.Series();
        series.setName("Currency Rate "+symbol+" To "+BASE);
        for (CurrencyHistory currencyHistory : list) {
            series.getData().add(new XYChart.Data(currencyHistory.getDay()
                    ,currencyHistory.getValue()));
        }
        if(lineChart.getData().size()>2){
            lineChart.getData().set(2, series);
        }else{
            lineChart.getData().add(series);
        }
    }

}
