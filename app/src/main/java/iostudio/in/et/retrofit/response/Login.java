package iostudio.in.et.retrofit.response;

public class Login extends CommonResponse {
    private LoginData data;

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }
}