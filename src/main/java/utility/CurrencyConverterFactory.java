package utility;

import ApiConnection.FixerApiConnection;
import ApiConnection.OpenExchangeApiConnection;
import ApiParsers.FixerJsonParser;
import ApiParsers.OpenExchangeJsonParser;
import repository.Converter;
import repository.ConverterImp;
import repository.CurrencyApiProvider;
import repository.CurrencyJsonParser;

import java.io.IOException;
import java.net.URISyntaxException;

public class CurrencyConverterFactory {
    public static Converter resolveProvider(String fixerUrl, String fixerKey, String openUrl, String openKey) throws InterruptedException {
        CurrencyApiProvider primary = null;
        CurrencyJsonParser fixerParser = new FixerJsonParser();


            primary = new FixerApiConnection(fixerUrl, fixerKey);


        int retries = 3;
        int delay = 1000; // 1 second
        for (int i = 0; i < retries; i++) {
            try {
                primary.getSymbolsWithSignification();
                return new ConverterImp(primary, fixerParser);
            } catch (IOException | URISyntaxException e) {
                System.out.println("⏳ Rate limit hit, retrying in " + delay + "ms...");
                Thread.sleep(delay);
                delay *= 2; // exponential backoff
            }
        }
        System.out.println("⚠️ Fixer API unreachable after retries.");


        // Fallback logic
        System.out.println("🔄 Switching to fallback API...");
        try {
            CurrencyApiProvider fallback = new OpenExchangeApiConnection(openUrl, openKey);
            CurrencyJsonParser openParser = new OpenExchangeJsonParser();
            return new ConverterImp(fallback, openParser);
        } catch (Exception fallbackEx) {
            System.out.println("⛔ Fallback API also failed: " + fallbackEx.getMessage());
            throw new RuntimeException("Both APIs failed to initialize.");
        }
    }


    public static class Http429Exception extends IOException {
        public Http429Exception(String message) {
            super(message);
        }
    }

}
