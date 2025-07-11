package repository;

import models.Currency;
import models.CurrencyHistory;

import java.io.IOException;
import java.util.List;

public interface Converter {

    Double convert(String from, String to, Double value) throws IOException;
    List<Currency> getAllCurrencies() throws IOException;
    List<String> getAllSymbolsWithSignifications(List<Currency> currencies);
    List<CurrencyHistory> getHistory(String base, Integer duration,String symbol) throws IOException;
}
