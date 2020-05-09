package iostudio.in.et.retrofit.response;

public class Profile extends CommonResponse{

    private ProfileData data;

    public ProfileData getData() {
        return data;
    }

    public void setData(ProfileData data) {
        this.data = data;
    }
}
