package ApiConnection;


import org.json.JSONObject;

public class JsonConversion {
    public Double getDataConversion(StringBuilder stringBuilder){
        JSONObject myResponse=new JSONObject(stringBuilder.toString());
        return myResponse.getDouble("result");
    }
    public String[]getDataSymbols(StringBuilder stringBuilder){
        JSONObject myResponse=new JSONObject(stringBuilder.toString());
        return myResponse.get("symbols").toString().split(",");
    }
    public String[]getDataHistoricalCurrency(StringBuilder stringBuilder){
        JSONObject myResponse=new JSONObject(stringBuilder.toString());
        return myResponse.get("rates").toString().split(",");}
}
