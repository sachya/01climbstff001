package iostudio.in.et.retrofit.response;

import android.os.Parcel;
import android.os.Parcelable;

public class ClientData implements Parcelable {

    private boolean isChecked;
    private String client_id;
    private String client_type;
    private String client_type_id;
    private String user_id;
    private String company_name;
    private String company_address;
    private String owner_name;
    private String owner_mobile;
    private String created_at;
    private String active;
    private String deleted_at;
    private String meetings_completed;
    private String remark;
    private String address;

    public ClientData() {

    }

    protected ClientData(Parcel in) {
        client_id = in.readString();
        client_type_id = in.readString();
        client_type = in.readString();
        user_id = in.readString();
        company_name = in.readString();
        company_address = in.readString();
        owner_name = in.readString();
        owner_mobile = in.readString();
        created_at = in.readString();
        active = in.readString();
        deleted_at = in.readString();
    }

    public static final Creator<ClientData> CREATOR = new Creator<ClientData>() {
        @Override
        public ClientData createFromParcel(Parcel in) {
            return new ClientData(in);
        }

        @Override
        public ClientData[] newArray(int size) {
            return new ClientData[size];
        }
    };

    public String getClient_type() {
        return client_type;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_mobile() {
        return owner_mobile;
    }

    public void setOwner_mobile(String owner_mobile) {
        this.owner_mobile = owner_mobile;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(client_id);
        dest.writeString(client_type_id);
        dest.writeString(client_type);
        dest.writeString(user_id);
        dest.writeString(company_name);
        dest.writeString(company_address);
        dest.writeString(owner_name);
        dest.writeString(owner_mobile);
        dest.writeString(created_at);
        dest.writeString(active);
        dest.writeString(deleted_at);
    }

    public String getMeetings_completed() {
        return meetings_completed;
    }

    public void setMeetings_completed(String meetings_completed) {
        this.meetings_completed = meetings_completed;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClient_type_id() {
        return client_type_id;
    }

    public void setClient_type_id(String client_type_id) {
        this.client_type_id = client_type_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
