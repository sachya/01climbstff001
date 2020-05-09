package iostudio.in.et.retrofit.response;

public class ClientInfo extends CommonResponse {
    private ClientData data;

    public ClientData getData() {
        return data;
    }

    public void setData(ClientData data) {
        this.data = data;
    }
}
