package services;

import models.CurrencyHistory;
import repository.Converter;

import java.io.IOException;
import java.util.List;

public class CurrencyHistoryService {
    private final Converter converter;
    public CurrencyHistoryService(Converter converter) {
        this.converter = converter;
    }
    public List<CurrencyHistory> currencyHistoryData(String base
            , Integer durationDay, String symbol) throws IOException {
        return converter.getHistory(base, durationDay, symbol);
    }

}
