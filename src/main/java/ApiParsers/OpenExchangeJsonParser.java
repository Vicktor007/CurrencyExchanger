package ApiParsers;

import models.Currency;
import models.CurrencyHistory;
import org.json.JSONObject;
import repository.CurrencyJsonParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ApiParsers.FixerJsonParser.getCurrencyHistories;

public class OpenExchangeJsonParser implements CurrencyJsonParser {
    @Override
    public Double parseConversionRate(StringBuilder response) {
        JSONObject obj = new JSONObject(response.toString());
        return obj.getJSONObject("response").getDouble("amount");
    }

    @Override
    public List<CurrencyHistory> parseHistoricalRates(StringBuilder response, String base, String symbol) {
        return getCurrencyHistories(response, base, symbol);
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

