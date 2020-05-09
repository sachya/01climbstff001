package iostudio.in.et.retrofit.response;

public class MeetingInfo extends CommonResponse {
    private MeetingData data;

    public MeetingData getData() {
        return data;
    }

    public void setData(MeetingData data) {
        this.data = data;
    }
}
