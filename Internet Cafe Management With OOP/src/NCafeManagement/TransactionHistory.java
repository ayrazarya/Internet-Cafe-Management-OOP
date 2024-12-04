package NCafeManagement;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class TransactionHistory {
    private final List<PaymentInterface> payments;
    
    public TransactionHistory(){
        payments = new ArrayList<>();
    }
    
    class CashPayment implements PaymentInterface {
        double amount;
        Date paymentDate;
        
        @Override
        public boolean processPayment(double amount){
            // Simulate processing of cash payment
            System.out.println("Cash payment processed for amount: Rp." + amount);
            return true; // Return true to simulate successful payment
        }

        @Override
        public double getAmount() {
            return amount;
        }

        @Override
        public void setAmount(double amount) {
            this.amount = amount;
        }

        @Override
        public Date getDate() {
            return paymentDate;
        }

        @Override
        public void setDate(Date date) {
            this.paymentDate = date;
        }
    }

    class DummyQRISPayment implements PaymentInterface {
        double amount;
        Date paymentDate;
        
        @Override
        public boolean processPayment(double amount){
            // Simulate processing of dummy QRIS payment
            System.out.println("Dummy QRIS payment processed for amount: Rp." + amount);
            return true; // Return true to simulate successful payment
        }

        @Override
        public double getAmount() {
            return amount;
        }

        @Override
        public void setAmount(double amount) {
            this.amount = amount;
        }

        @Override
        public Date getDate() {
            return paymentDate;
        }

        @Override
        public void setDate(Date date) {
            this.paymentDate = date;
        }
    }
    // add implementations initialization and add to list methods here
    void addCashTransaction(double amount, Date date){
        CashPayment cashTransaction = new CashPayment();
        if(cashTransaction.processPayment(amount)){
            cashTransaction.setAmount(amount);
            cashTransaction.setDate(date);
        }
        else{
            System.out.println("Failed to process transaction.");
        }
        payments.add(cashTransaction);
    }
    
    void addQrisTransaction(double amount, Date date){
        DummyQRISPayment qris = new DummyQRISPayment();
        if(qris.processPayment(amount)){
            qris.setAmount(amount);
            qris.setDate(date);
        }
        else{
            System.out.println("Failed to process transaction.");
        }
        payments.add(qris);
    }
    
    public long paymentToDuration(double amount){
        double rate = 150; //rate per minute so Rp. 9000 per hour Rp. 4500 per hour with posssibility of custom amounts.
        long minuteForAmount = ((long)amount/(long)rate);
        long duration = minuteForAmount * 60000;
        return duration;
    }
    
    //methods that utilizes transaction history
    void displayTransactions(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("======================\n");
        for(PaymentInterface transactions: payments){
            System.out.println(sdf.format(transactions.getDate())+" "+transactions.getAmount());
        }
        System.out.println("\n======================");
    }
    
    void readTransactionHistory(){
        try{
            String fileName = "Transaction_History.txt";
            File transactions = new File(fileName);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(!transactions.exists()){
                System.out.println(fileName+" not found, creating new file...");
                transactions.createNewFile();
            }
            else{
                BufferedReader fileRead;
                fileRead = new BufferedReader(new FileReader(fileName));
                String crntLine;
                while((crntLine = fileRead.readLine()) != null){
                    String adminData[] = crntLine.split(";");
                    Date date = sdf.parse(adminData[0]);
                    double amount = Double.parseDouble(adminData[1]);
                    String paymentMethod = adminData[2];
                    if(paymentMethod.equalsIgnoreCase("cash")){
                        addCashTransaction(amount, date);
                    }
                    else if(paymentMethod.equalsIgnoreCase("qris")){
                        addQrisTransaction(amount, date);
                    }
                }
                fileRead.close();
            }
            
        }
        catch(IOException e){
            System.out.println("Failure while reading or writing file");
        }
        catch(ParseException p){
            System.out.println("Error while parsing file data");
        }
    }
    
    void writeTransactionHistory(){
        try{
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter("Transaction_History.txt"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for(PaymentInterface transactions : payments){
                if(transactions instanceof CashPayment){
                    fileWriter.write(sdf.format(transactions.getDate())+";"+transactions.getAmount()+";Cash\n");
                }
                else if (transactions instanceof DummyQRISPayment){
                    fileWriter.write(sdf.format(transactions.getDate())+";"+transactions.getAmount()+";QRIS\n");
                }
            }
            System.out.println("Succesfully saved transactions to file");
        }
        catch(IOException e){
            System.out.println("Error writing transactions to file");
        }
    }
}
