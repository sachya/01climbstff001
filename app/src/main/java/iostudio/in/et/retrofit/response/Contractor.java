package iostudio.in.et.retrofit.response;

import java.util.ArrayList;

public class Contractor extends CommonResponse {

    private ArrayList<ContractorData> data;

    public ArrayList<ContractorData> getData() {
        return data;
    }

    public void setData(ArrayList<ContractorData> data) {
        this.data = data;
    }
}
