package iostudio.in.et.retrofit.response;

import android.os.Parcel;
import android.os.Parcelable;

public class ContractorData implements Parcelable {

    private String contractor_id;
    private String contractor_name;
    private String attendace_date;
    private String service_type;
    private String shift;
    private String status;
    private boolean isSelected;

    private ContractorData contractorData;

    public ContractorData() {

    }

    public ContractorData(ContractorData contractorData) {
        this.contractorData = contractorData;
    }

    protected ContractorData(Parcel in) {
        contractor_id = in.readString();
        contractor_name = in.readString();
        attendace_date = in.readString();
        service_type = in.readString();
        shift = in.readString();
        status = in.readString();
        isSelected = in.readByte() != 0;
        contractorData = in.readParcelable(ContractorData.class.getClassLoader());
    }

    public static final Creator<ContractorData> CREATOR = new Creator<ContractorData>() {
        @Override
        public ContractorData createFromParcel(Parcel in) {
            return new ContractorData(in);
        }

        @Override
        public ContractorData[] newArray(int size) {
            return new ContractorData[size];
        }
    };

    public String getContractor_id() {
        return contractor_id;
    }

    public void setContractor_id(String contractor_id) {
        this.contractor_id = contractor_id;
    }

    public String getName() {
        return contractor_name;
    }

    public void setName(String contractor_name) {
        this.contractor_name = contractor_name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object instanceof ContractorData)
        {
            sameSame = this.contractor_id.equals(((ContractorData) object).contractor_id);
        }

        return sameSame;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getAttendace_date() {
        return attendace_date;
    }

    public void setAttendace_date(String attendace_date) {
        this.attendace_date = attendace_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contractor_id);
        dest.writeString(contractor_name);
        dest.writeString(attendace_date);
        dest.writeString(service_type);
        dest.writeString(shift);
        dest.writeString(status);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeParcelable(contractorData, flags);
    }
}
