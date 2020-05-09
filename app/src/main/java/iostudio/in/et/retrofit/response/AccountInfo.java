package iostudio.in.et.retrofit.response;

public class AccountInfo extends CommonResponse{
    private AccountData data;

    public AccountData getData() {
        return data;
    }

    public void setData(AccountData data) {
        this.data = data;
    }
}
