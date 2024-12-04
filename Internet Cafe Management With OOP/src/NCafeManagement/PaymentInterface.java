package NCafeManagement;
import java.util.Date;
interface PaymentInterface {
    boolean processPayment(double amount);
    public double getAmount();
    public void setAmount(double amount);
    public Date getDate();
    public void setDate(Date date);
}
