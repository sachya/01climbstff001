package iostudio.in.et.retrofit.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AccountData implements Parcelable {

    private String account_id;
    private String type_id;
    private String type_name;
    private String name;
    private String amount;
    private String date;
    private ArrayList<Note> notes;


    private String quote;
    private String user;
    private String actionLabel;
    private AccountIncome monthincome;
    private AccountIncome monthexpense;
    private AccountIncome dayincome;
    private AccountIncome dayexpense;
    private ArrayList<AccountData> expenses;

    public AccountData() {

    }


    protected AccountData(Parcel in) {
        account_id = in.readString();
        name = in.readString();
        amount = in.readString();
        date = in.readString();
        type_id = in.readString();
        type_name = in.readString();
    }

    public static final Creator<AccountData> CREATOR = new Creator<AccountData>() {
        @Override
        public AccountData createFromParcel(Parcel in) {
            return new AccountData(in);
        }

        @Override
        public AccountData[] newArray(int size) {
            return new AccountData[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(account_id);
        dest.writeString(name);
        dest.writeString(amount);
        dest.writeString(date);
        dest.writeString(type_id);
        dest.writeString(type_name);
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public ArrayList<AccountData> getExpenses() {
        return expenses;
    }

    public void setExpenses(ArrayList<AccountData> expenses) {
        this.expenses = expenses;
    }

    public AccountIncome getDayexpense() {
        return dayexpense;
    }

    public void setDayexpense(AccountIncome dayexpense) {
        this.dayexpense = dayexpense;
    }

    public AccountIncome getDayincome() {
        return dayincome;
    }

    public void setDayincome(AccountIncome dayincome) {
        this.dayincome = dayincome;
    }

    public AccountIncome getMonthexpense() {
        return monthexpense;
    }

    public void setMonthexpense(AccountIncome monthexpense) {
        this.monthexpense = monthexpense;
    }

    public AccountIncome getMonthincome() {
        return monthincome;
    }

    public void setMonthincome(AccountIncome monthincome) {
        this.monthincome = monthincome;
    }

    public String getActionLabel() {
        return actionLabel;
    }

    public void setActionLabel(String actionLabel) {
        this.actionLabel = actionLabel;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }
}
