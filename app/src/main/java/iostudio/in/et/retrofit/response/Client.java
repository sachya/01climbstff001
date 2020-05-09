package iostudio.in.et.retrofit.response;

import java.util.ArrayList;

public class Client extends CommonResponse {
    private ArrayList<ClientData> data;

    public ArrayList<ClientData> getData() {
        return data;
    }

    public void setData(ArrayList<ClientData> data) {
        this.data = data;
    }
}
