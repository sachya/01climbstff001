package iostudio.in.et.retrofit.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MeetingData  implements Parcelable {
    private String code;
    private String message;

    private String meeting_id;
    private String name;
    private String type_name;
    private String status_id;
    private String status_name;
    private String address;
    private String start_time;
    private String meeting_date;
    private String other_location;
    private ArrayList<Note>notes;

    public MeetingData() {

    }


    protected MeetingData(Parcel in) {
        meeting_id = in.readString();
        name = in.readString();
        type_name = in.readString();
        status_id = in.readString();
        status_name = in.readString();
        address = in.readString();
        start_time = in.readString();
        other_location = in.readString();
        notes = in.createTypedArrayList(Note.CREATOR);
    }

    public static final Creator<MeetingData> CREATOR = new Creator<MeetingData>() {
        @Override
        public MeetingData createFromParcel(Parcel in) {
            return new MeetingData(in);
        }

        @Override
        public MeetingData[] newArray(int size) {
            return new MeetingData[size];
        }
    };

    public String getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(String meeting_id) {
        this.meeting_id = meeting_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getStatus() {
        return status_id;
    }

    public void setStatus(String status) {
        this.status_id = status;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public String getOther_location() {
        return other_location;
    }

    public void setOther_location(String other_location) {
        this.other_location = other_location;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(meeting_id);
        dest.writeString(name);
        dest.writeString(type_name);
        dest.writeString(status_id);
        dest.writeString(status_name);
        dest.writeString(address);
        dest.writeString(start_time);
        dest.writeString(other_location);
        dest.writeTypedList(notes);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMeeting_date() {
        return meeting_date;
    }

    public void setMeeting_date(String meeting_date) {
        this.meeting_date = meeting_date;
    }
}
