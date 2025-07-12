package repository;

import models.Currency;
import models.CurrencyHistory;

import java.util.List;
import java.util.Map;

public interface CurrencyJsonParser {
    Double parseConversionRate(StringBuilder response);
    List<CurrencyHistory> parseHistoricalRates(StringBuilder response, String base, String symbol);
    Map<String, String> parseSymbols(StringBuilder response);
    List<Currency> parseCurrencyObjects(StringBuilder response);
}
