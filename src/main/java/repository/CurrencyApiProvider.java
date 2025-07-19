package repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;

public interface CurrencyApiProvider {
    StringBuilder getRate(String from, String to, Double amount) throws IOException, URISyntaxException;
    StringBuilder getCurrencyHistory(String base, LocalDate start, LocalDate end, String symbol) throws IOException, URISyntaxException;
    StringBuilder getSymbolsWithSignification() throws IOException, URISyntaxException;
}
