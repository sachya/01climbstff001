package iostudio.in.et.retrofit.response;

public class HomeData {
    private String quote;
    private String user;
    private String actionLabel;
    private String meetingCount;
    private  IncomeExpense income;
    private  IncomeExpense expense;
    private GEO geo;

    public IncomeExpense getExpense() {
        return expense;
    }

    public void setExpense(IncomeExpense expense) {
        this.expense = expense;
    }

    public IncomeExpense getIncome() {
        return income;
    }

    public void setIncome(IncomeExpense income) {
        this.income = income;
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

    public GEO getGeo() {
        return geo;
    }

    public void setGeo(GEO geo) {
        this.geo = geo;
    }

    public String getMeetingCount() {
        return meetingCount;
    }

    public void setMeetingCount(String meetingCount) {
        this.meetingCount = meetingCount;
    }
}
