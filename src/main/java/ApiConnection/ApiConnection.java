package ApiConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

public class ApiConnection {
    private final String url;
    private final String apiKey;

    public ApiConnection(String url, String apiKey) {
        this.url = url;
        this.apiKey = apiKey;
    }

    public StringBuilder getRate(String from, String to, Double amount) throws IOException {
        URL objUrl = new URL(url + "/convert?to="+ to + "&from=" + from + "&amount=" + amount);
        HttpURLConnection httpURLConnection = (HttpURLConnection) objUrl.openConnection();
        httpURLConnection.setRequestProperty("apikey", apiKey);
        StringBuilder sb = getStringBuilder(httpURLConnection);
        return sb;
    }

    public StringBuilder getSymbolsWithSignification() throws MalformedURLException, IOException {
        URL objUrl = new URL(url + "/symbols");
        HttpURLConnection httpURLConnection = (HttpURLConnection) objUrl.openConnection();
        httpURLConnection.setRequestProperty("apikey", apiKey);
        StringBuilder sb = getStringBuilder(httpURLConnection);
        return sb;
    }

    public StringBuilder getCurrencyHistory(String base, LocalDate startDate, LocalDate endDate, String symbol) throws IOException {
        URL objUrl=new URL(url+"/timeseries?start_date="+startDate+"&end_date="
                +endDate+"&base="+base+"&symbols="+symbol);
        HttpURLConnection httpURLConnection=(HttpURLConnection) objUrl.openConnection();
        httpURLConnection.setRequestProperty("apikey", apiKey);
        StringBuilder sb= getStringBuilder(httpURLConnection);
        return sb;
    }

    private StringBuilder getStringBuilder(HttpURLConnection httpURLConnection) throws IOException {
        if(httpURLConnection.getResponseCode() != 200)
            throw new IllegalStateException("Connection Failed: HTTP error code : " + httpURLConnection.getResponseCode());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();
        while ((inputLine = bufferedReader.readLine()) != null){
            sb.append(inputLine);}
        return sb;
    }
}
