package iostudio.in.et.retrofit.response;

import java.util.ArrayList;

public class Customer extends CommonResponse {
    private ArrayList<CustomerData> data;

    public ArrayList<CustomerData> getData() {
        return data;
    }

    public void setData(ArrayList<CustomerData> data) {
        this.data = data;
    }
}
