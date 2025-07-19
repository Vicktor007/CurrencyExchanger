package services;

import models.Currency;
import repository.Converter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

public class CurrencyService {
    private final Converter converter;

    public CurrencyService(Converter converter) {
        this.converter = converter;
    }
    public Double convert(String from,String to,Double amount) throws IOException, URISyntaxException {
        return  converter.convert(from, to, amount);
    }
    public List<Currency> getAllCurrencies() throws IOException, URISyntaxException {
        return converter.getAllCurrencies();
    }

    public List<Currency> findCurrency(List<Currency> list, String keyword) {
        String search = keyword.toUpperCase();
        return list.stream()
                .filter(currency ->
                        currency.getSymbol().toUpperCase().contains(search) ||
                                currency.getSignification().toUpperCase().contains(search))
                .collect(Collectors.toList());
    }

    //    public List<Currency>findCurrency(List<Currency> list,String symbol){
//        String symbol2=toUpperCase(symbol);
//        return list.stream().filter(currency->currency.getSymbol()
//                        .contains(symbol2))
//                .collect(Collectors.toList());
//    }
    public List<String>getAllSymbolsAndSignifications(List<Currency> list){
        return converter.getAllSymbolsWithSignifications(list);
    }

    private String toUpperCase(String str){
        String st="";
        for (int i = 0; i < str.length(); i++) {
            st+=Character.toUpperCase(str.charAt(i))+"";
        }

        return st; }
}
