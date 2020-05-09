package iostudio.in.et.retrofit.response;

public class Home extends CommonResponse {
    private HomeData data;

    public HomeData getData() {
        return data;
    }

    public void setData(HomeData data) {
        this.data = data;
    }
}
