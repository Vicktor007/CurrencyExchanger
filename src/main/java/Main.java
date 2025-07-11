import ApiConnection.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Currency;
import repository.Converter;
import repository.ConverterImp;
import services.CurrencyHistoryService;
import services.CurrencyService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Main extends Application  {
    public static void main(String[] args) throws IOException {
        launch(args);

    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Dashboard.fxml"));
        String css = getClass().getResource("/css/styling.css").toExternalForm();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.setTitle("Currency Exchanger");
        stage.show();
    }
}
