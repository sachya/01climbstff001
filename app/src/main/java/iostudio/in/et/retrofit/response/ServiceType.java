package iostudio.in.et.retrofit.response;

import java.util.ArrayList;

public class ServiceType extends CommonResponse {
    private ArrayList<ServiceTypeData> data;

    public ArrayList<ServiceTypeData> getData() {
        return data;
    }

    public void setData(ArrayList<ServiceTypeData> data) {
        this.data = data;
    }
}
