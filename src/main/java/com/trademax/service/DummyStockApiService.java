package com.trademax.service;

import com.trademax.exception.StockNotFoundException;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DummyStockApiService {

    private final Map<String, StockInfo> stocks = new HashMap<>();
    private final Random random = new Random();

    @jakarta.annotation.PostConstruct
    public void init() {
        stocks.put("TCS", new StockInfo("TCS", "Tata Consultancy Services", 3925.75));
        stocks.put("INFY", new StockInfo("INFY", "Infosys", 1450.10));
        stocks.put("RELI", new StockInfo("RELI", "Reliance Industries", 2550.50));
        stocks.put("HDFC", new StockInfo("HDFC", "HDFC Bank", 1600.25));
        stocks.put("WIPRO", new StockInfo("WIPRO", "Wipro", 380.40));
    }

    public StockInfo getStock(String ticker) {
        StockInfo s = stocks.get(ticker.toUpperCase());
        if (s == null) throw new StockNotFoundException("Ticker not found: " + ticker);
        double changeFactor = 1 + (random.nextDouble() - 0.5) * 0.04;
        double newPrice = Math.round(s.getPrice() * changeFactor * 100.0) / 100.0;
        return new StockInfo(s.getTicker(), s.getCompany(), newPrice);
    }

    public List<StockInfo> listStocks() {
        List<StockInfo> out = new ArrayList<>();
        for (StockInfo s : stocks.values()) {
            out.add(getStock(s.getTicker()));
        }
        return out;
    }

    public static class StockInfo {
        private String ticker;
        private String company;
        private double price;

        public StockInfo() {}

        public StockInfo(String ticker, String company, double price) {
            this.ticker = ticker; this.company = company; this.price = price;
        }

        public String getTicker(){return ticker;}
        public String getCompany(){return company;}
        public double getPrice(){return price;}

        public void setTicker(String t){this.ticker=t;}
        public void setCompany(String c){this.company=c;}
        public void setPrice(double p){this.price=p;}
    }
}
