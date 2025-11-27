package com.trademax.console;

import com.trademax.model.User;
import com.trademax.service.DummyStockApiService;
import com.trademax.service.TradeService;
import com.trademax.service.UserService;
import com.trademax.service.WalletService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleMenu implements CommandLineRunner {
    private final UserService userSvc;
    private final WalletService walletSvc;
    private final DummyStockApiService stockSvc;
    private final TradeService tradeSvc;

    public ConsoleMenu(UserService u, WalletService w, DummyStockApiService s, TradeService t){
        this.userSvc = u; 
        this.walletSvc = w; 
        this.stockSvc = s; 
        this.tradeSvc = t;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("==== TradeMax Pro Console ====");
        while (true) {
            System.out.println();
            System.out.println("1.Register  2.Add Money  3.Withdraw  4.View Stocks");
            System.out.println("5.Buy       6.Sell       7.View Profile 8.View Portfolio");
            System.out.println("9.Exit");
            System.out.print("Choose> ");
            String choice = sc.nextLine();
            try {
                if ("1".equals(choice)) {
                    System.out.print("Name: "); 
                    String name = sc.nextLine();
                    System.out.print("Email: "); 
                    String email = sc.nextLine();
                    System.out.print("PAN: "); 
                    String pan = sc.nextLine();
                    User u = new User(null, name, email, pan, null, null);
                    User saved = userSvc.register(u);
                    System.out.println("Registered. userId: " + saved.getId());
                } 
                else if ("2".equals(choice)) {
                    System.out.print("UserId: "); 
                    String id = sc.nextLine();
                    System.out.print("Amount: "); 
                    double amt = Double.parseDouble(sc.nextLine());
                    System.out.println("New balance: " + walletSvc.addMoney(id, amt));
                } 
                else if ("3".equals(choice)) {
                    System.out.print("UserId: "); 
                    String id = sc.nextLine();
                    System.out.print("Amount: "); 
                    double amt = Double.parseDouble(sc.nextLine());
                    System.out.println("New balance: " + walletSvc.withdrawMoney(id, amt));
                } 
                else if ("4".equals(choice)) {
                    stockSvc.listStocks().forEach(s ->
                        System.out.println(s.getTicker() + " | " + s.getCompany() + " | " + s.getPrice()));
                } 
                else if ("5".equals(choice)) {
                    System.out.print("UserId: "); 
                    String id = sc.nextLine();
                    System.out.print("Ticker: "); 
                    String tkr = sc.nextLine();
                    System.out.print("Qty: "); 
                    int q = Integer.parseInt(sc.nextLine());
                    tradeSvc.buy(id, tkr, q);
                    System.out.println("Bought.");
                } 
                else if ("6".equals(choice)) {
                    System.out.print("UserId: "); 
                    String id = sc.nextLine();
                    System.out.print("Ticker: "); 
                    String tkr = sc.nextLine();
                    System.out.print("Qty: "); 
                    int q = Integer.parseInt(sc.nextLine());
                    tradeSvc.sell(id, tkr, q);
                    System.out.println("Sold.");
                } 
                else if ("7".equals(choice)) {
                    System.out.print("UserId: "); 
                    String id = sc.nextLine();
                    System.out.println(userSvc.get(id));
                } 
                else if ("8".equals(choice)) {
                    System.out.print("UserId: "); 
                    String id = sc.nextLine();
                    var resp = userSvc.get(id).getPortfolio();
                    System.out.println(resp == null || resp.isEmpty() ? "No holdings" : resp);
                } 
                else if ("9".equals(choice)) {
                    System.out.println("Exiting..."); 
                    break;
                } 
                else {
                    System.out.println("Invalid option");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        sc.close();
    }
}
