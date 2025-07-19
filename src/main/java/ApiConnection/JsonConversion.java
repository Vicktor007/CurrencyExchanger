//package FixerApiConnection;
//
//
//import models.CurrencyHistory;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
//public class JsonConversion {
//    public Double getDataConversion(StringBuilder stringBuilder){
//        JSONObject myResponse=new JSONObject(stringBuilder.toString());
//        return myResponse.getDouble("result");
//    }
//    public String[]getDataSymbols(StringBuilder stringBuilder){
//        JSONObject myResponse=new JSONObject(stringBuilder.toString());
//        return myResponse.get("symbols").toString().split(",");
//    }
//
//    public List<CurrencyHistory> parseHistoricalRates(StringBuilder stringBuilder, String base, String symbol) {
//        JSONObject response = new JSONObject(stringBuilder.toString());
//        JSONObject rates = response.getJSONObject("rates");
//
//        List<CurrencyHistory> historyList = new ArrayList<>();
//
//        for (String dateKey : rates.keySet()) {
//            JSONObject dailyRates = rates.getJSONObject(dateKey);
//            double rate = dailyRates.getDouble(symbol);
//            historyList.add(new CurrencyHistory(base, symbol, dateKey, rate));
//        }
//
//        historyList.sort(Comparator.comparing(CurrencyHistory::getDay));
//        return historyList;
//    }
//
//}
