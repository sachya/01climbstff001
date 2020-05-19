package iostudio.in.et.retrofit.response;

public class AccountEntryData extends  CommonResponse {
    private EntryData data;

    public EntryData getData() {
        return data;
    }

    public void setData(EntryData data) {
        this.data = data;
    }
}
