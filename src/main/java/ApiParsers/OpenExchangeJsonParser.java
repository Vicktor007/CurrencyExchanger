package ApiParsers;

import models.Currency;
import models.CurrencyHistory;
import org.json.JSONObject;
import repository.CurrencyJsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenExchangeJsonParser implements CurrencyJsonParser {
    @Override
    public Double parseConversionRate(StringBuilder response) {
        JSONObject obj = new JSONObject(response.toString());
        return obj.getJSONObject("response").getDouble("amount");
    }

    @Override
    public List<CurrencyHistory> parseHistoricalRates(StringBuilder response, String base, String symbol) {
        JSONObject rates = new JSONObject(response.toString()).getJSONObject("rates");
        List<CurrencyHistory> history = new ArrayList<>();
        for (String date : rates.keySet()) {
            double rate = rates.getJSONObject(date).getDouble(symbol);
            history.add(new CurrencyHistory(base, symbol, date, rate));
        }
        return history;
    }

    @Override
    public Map<String, String> parseSymbols(StringBuilder response) {
        JSONObject symbols = new JSONObject(response.toString());
        Map<String, String> symbolMap = new HashMap<>();
        for (String key : symbols.keySet()) {
            symbolMap.put(key, symbols.getString(key));
        }
        return symbolMap;
    }

    @Override
    public List<Currency> parseCurrencyObjects(StringBuilder response) {
        Map<String, String> symbolMap = parseSymbols(response);
        return symbolMap.entrySet().stream()
                .map(entry -> new Currency(entry.getKey(), entry.getValue()))
                .toList();
    }

}

