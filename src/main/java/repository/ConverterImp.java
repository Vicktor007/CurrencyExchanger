//package repository;
//
//import FixerApiConnection.*;
//import models.Currency;
//import models.CurrencyHistory;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//    public class ConverterImp implements Converter {
//        private final CurrencyApiProvider apiProvider;
//        private final CurrencyJsonParser jsonParser;
//
//        public ConverterImp(CurrencyApiProvider apiProvider, CurrencyJsonParser jsonParser) {
//            this.apiProvider = apiProvider;
//            this.jsonParser = jsonParser;
//        }
//
//
//
//    @Override
//    public Double convert(String from, String to, Double value) throws IOException {
//        StringBuilder sb = apiConnection.getRate(from,to, value);
//        return jsonConversion.getDataConversion(sb);
//    }
//
//    @Override
//    public List<Currency> getAllCurrencies() throws IOException {
//        List<Currency> currencies = new ArrayList<>();
//        StringBuilder sb = apiConnection.getSymbolsWithSignification();
//        String SymbolSignification[] = jsonConversion.getDataSymbols(sb);
//        for (String line : SymbolSignification) {
//            String temp[] = line.split(":");
//            String symbol = temp[0].replace("{", "").replace("\"", "");
//            String signification=temp[1].replace("}", "").replace("\"", "");
//            Currency currency=new Currency(symbol, signification);
//            currencies.add(currency);
//        }
//        return currencies;
//    }
//
//    @Override
//    public List<String> getAllSymbolsWithSignifications(List<Currency> currencies) {
//        return currencies.stream()
//                .map(c -> c.getSignification() + " " + c.getSymbol())
//                .sorted()
//                .collect(Collectors.toList());
//    }
//
////    @Override
////    public List<String> getAllSymbols(List<Currency> currencies) {
////        return currencies.stream().map(Currency::getSymbol).sorted().collect(Collectors.toList());
////    }
//
//    @Override
//    public List<CurrencyHistory> getHistory(String base, Integer duration, String symbol) throws IOException {
//        LocalDate today = LocalDate.now();
//        LocalDate dayAgo = today.minusDays(duration);
//
//        StringBuilder sb = apiConnection.getCurrencyHistory(base, dayAgo, today, symbol);
//
////        String currencyHistoryValue[] = jsonConversion.getDataHistoricalCurrency(sb);
////        List<CurrencyHistory> history = new ArrayList<>();
////        for (String line : currencyHistoryValue) {
////            String dayHist = line.subSequence(1, 12).toString().replace("\"", "");
////            Double value = Double.parseDouble(line.subSequence(20, 27).toString().replace(":", ""));
////            CurrencyHistory currencyHistory = new CurrencyHistory(
////                    base, symbol, dayHist, value
////            );
////            history.add(currencyHistory);
////        } history.sort((o1, o2) -> {
////            return o1.getDay().compareTo(o2.getDay());
////        });
//        return jsonConversion.parseHistoricalRates(sb, base, symbol);
//    }
//}

package repository;

import models.Currency;
import models.CurrencyHistory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class ConverterImp implements Converter {
    private final CurrencyApiProvider apiProvider;
    private final CurrencyJsonParser jsonParser;

    public ConverterImp(CurrencyApiProvider apiProvider, CurrencyJsonParser jsonParser) {
        this.apiProvider = apiProvider;
        this.jsonParser = jsonParser;
    }

    @Override
    public Double convert(String from, String to, Double value) throws IOException, URISyntaxException {
        StringBuilder response = apiProvider.getRate(from, to, value);
        return jsonParser.parseConversionRate(response);
    }

    @Override
    public List<Currency> getAllCurrencies() throws IOException, URISyntaxException {
        StringBuilder response = apiProvider.getSymbolsWithSignification();
        return jsonParser.parseCurrencyObjects(response); // optional parsing function
    }

    @Override
    public List<String> getAllSymbolsWithSignifications(List<Currency> currencies) {
        return currencies.stream()
                .sorted(Comparator.comparing(Currency::getSignification)) // 🔽 Sort by signification
                .map(currency -> currency.getSignification() + " " + currency.getSymbol())
                .toList();
    }

    @Override
    public List<CurrencyHistory> getHistory(String base, Integer duration, String symbol) throws IOException, URISyntaxException {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(duration);
        StringBuilder response = apiProvider.getCurrencyHistory(base, startDate, today, symbol);
        return jsonParser.parseHistoricalRates(response, base, symbol);
    }
}

