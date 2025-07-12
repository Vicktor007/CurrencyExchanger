package repository;

import java.io.IOException;
import java.time.LocalDate;

public interface CurrencyApiProvider {
    StringBuilder getRate(String from, String to, Double amount) throws IOException;
    StringBuilder getCurrencyHistory(String base, LocalDate start, LocalDate end, String symbol) throws IOException;
    StringBuilder getSymbolsWithSignification() throws IOException;
}
