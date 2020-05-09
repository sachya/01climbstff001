package iostudio.in.et.retrofit.response;

import java.util.ArrayList;

public class Meeting extends CommonResponse {

    private ArrayList<MeetingData> data;

    public ArrayList<MeetingData> getData() {
        return data;
    }

    public void setData(ArrayList<MeetingData> data) {
        this.data = data;
    }
}
