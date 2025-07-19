package ApiConnection;

import repository.CurrencyApiProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;

public class OpenExchangeApiConnection implements CurrencyApiProvider {
    private final String openUrl;
    private final String openKey;

    public OpenExchangeApiConnection(String openUrl, String openKey) {
        this.openUrl = openUrl;
        this.openKey = openKey;
    }

    @Override
    public StringBuilder getRate(String from, String to, Double amount) throws IOException, URISyntaxException {
        String url = openUrl + "/convert/" + amount + "/" + from + "/" + to +
                "?app_id=" + openKey + "&prettyprint=false";
        return fetch(url);
    }

    @Override
    public StringBuilder getCurrencyHistory(String base, LocalDate start, LocalDate end, String symbol) throws IOException, URISyntaxException {
        String url = openUrl + "/time-series.json" +
                "?app_id=" + openKey +
                "&start=" + start +
                "&end=" + end +
                "&base=" + base +
                "&symbols=" + symbol +
                "&prettyprint=false";
        return fetch(url);
    }

    @Override
    public StringBuilder getSymbolsWithSignification() throws IOException, URISyntaxException {
        String url = openUrl + "/currencies.json?app_id=" + openKey;
        return fetch(url);
    }

    private StringBuilder fetch(String urlStr) throws IOException, URISyntaxException {
        URL url = new URI(urlStr).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            content.append(inputLine);
        return content;
    }
}

