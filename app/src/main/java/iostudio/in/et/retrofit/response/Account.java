package iostudio.in.et.retrofit.response;

import java.util.ArrayList;

public class Account extends CommonResponse {

    private ArrayList<AccountData> data;

    public ArrayList<AccountData> getData() {
        return data;
    }

    public void setData(ArrayList<AccountData> data) {
        this.data = data;
    }
}
