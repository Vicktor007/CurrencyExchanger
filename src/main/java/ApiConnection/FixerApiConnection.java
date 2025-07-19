package ApiConnection;

import repository.CurrencyApiProvider;
import utility.CurrencyConverterFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;

public class FixerApiConnection implements CurrencyApiProvider {
    private final String url;
    private final String apiKey;

    public FixerApiConnection(String url, String apiKey) {
        this.url = url;
        this.apiKey = apiKey;
    }

    public StringBuilder getRate(String from, String to, Double amount) throws IOException {
        URL objUrl = URI.create(url + "/convert?to="+ to + "&from=" + from + "&amount=" + amount).toURL();
        HttpURLConnection httpURLConnection = (HttpURLConnection) objUrl.openConnection();
        httpURLConnection.setRequestProperty("apikey", apiKey);
        return getStringBuilder(httpURLConnection);
    }

    public StringBuilder getSymbolsWithSignification() throws MalformedURLException, IOException {
        URL objUrl = URI.create(url + "/symbols").toURL();
        HttpURLConnection httpURLConnection = (HttpURLConnection) objUrl.openConnection();
        httpURLConnection.setRequestProperty("apikey", apiKey);
        return getStringBuilder(httpURLConnection);
    }

    public StringBuilder getCurrencyHistory(String base, LocalDate startDate, LocalDate endDate, String symbol) throws IOException {
        URL objUrl= URI.create(url+"/timeseries?start_date="+startDate+"&end_date="
                +endDate+"&base="+base+"&symbols="+symbol).toURL();
        HttpURLConnection httpURLConnection=(HttpURLConnection) objUrl.openConnection();
        httpURLConnection.setRequestProperty("apikey", apiKey);
        return getStringBuilder(httpURLConnection);
    }

    private StringBuilder getStringBuilder(HttpURLConnection httpURLConnection) throws IOException {
        int statusCode = httpURLConnection.getResponseCode();
        if (statusCode == 429) {
            throw new CurrencyConverterFactory.Http429Exception("Rate limit exceeded: HTTP 429");
        }
        if (statusCode != 200) {
            throw new IllegalStateException("Connection Failed: HTTP error code : " + statusCode);
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();
        while ((inputLine = bufferedReader.readLine()) != null){
            sb.append(inputLine);}
        return sb;
    }
}
