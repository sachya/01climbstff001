package iostudio.in.et.retrofit.response;

import java.util.ArrayList;

public class MeetingType extends CommonResponse {

    private ArrayList<MeetingTypeData>data;

    public ArrayList<MeetingTypeData> getData() {
        return data;
    }

    public void setData(ArrayList<MeetingTypeData> data) {
        this.data = data;
    }
}
