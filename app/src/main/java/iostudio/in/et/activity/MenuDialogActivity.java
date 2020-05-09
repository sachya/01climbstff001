package iostudio.in.et.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import iostudio.in.et.R;
import iostudio.in.et.adapter.MenuDialogRecyclerAdapter;
import iostudio.in.et.retrofit.response.ClientData;
import iostudio.in.et.retrofit.response.ContactMenu;
import iostudio.in.et.retrofit.response.MeetingData;
import iostudio.in.et.utility.Constant;

public class MenuDialogActivity extends BaseActivity {

    private Context context;
    RecyclerView recyclerViewData;
    private ArrayList<ContactMenu> dataArrayList;
    MeetingData meetingData;
    ClientData clientData;
    AppCompatTextView tv_name;
    AppCompatTextView tv_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dialog);
        context = this;
        initBase();
    }

    @Override
    protected void initView() {
        recyclerViewData = findViewById(R.id.recyclerViewData);
        tv_name = findViewById(R.id.tv_name);
        tv_type = findViewById(R.id.tv_type);
    }

    @Override
    protected void initData() {
        dataArrayList = new ArrayList<>();

        //0 is contact
        //1 is meeting
        int menuType = 0;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constant.INTENT.TYPE)) {
                menuType = bundle.getInt(Constant.INTENT.TYPE);
            }
            if (bundle.containsKey(Constant.INTENT.MEETING_OBJECT)) {
                meetingData = bundle.getParcelable(Constant.INTENT.MEETING_OBJECT);
                if (meetingData != null) {
                    if (!isEmptyString(meetingData.getName())) {
                        tv_name.setText(meetingData.getName());
                    }
                    if (!isEmptyString(meetingData.getType_name())) {
                        tv_type.setText(meetingData.getType_name());
                    }
                }
            }
            if (bundle.containsKey(Constant.INTENT.CLIENT_OBJECT)) {
                clientData = bundle.getParcelable(Constant.INTENT.CLIENT_OBJECT);
                if (clientData != null) {
                    if (!isEmptyString(clientData.getOwner_name())) {
                        tv_name.setText(clientData.getOwner_name());
                    }
                    if (!isEmptyString(clientData.getClient_type())) {
                        tv_type.setText(clientData.getClient_type());
                    }
                }
            }
        }
        if (menuType == 0) {
            dataArrayList.add(new ContactMenu(R.drawable.ic_info, "More Details", "more_details"));
            dataArrayList.add(new ContactMenu(R.drawable.ic_navigation, "Update Location", "update_location"));
            dataArrayList.add(new ContactMenu(R.drawable.ic_edit, "Edit Contact", "edit_contact"));
            dataArrayList.add(new ContactMenu(R.drawable.ic_library_add, "Add To Meeting", "add_meeting"));
            //dataArrayList.add(new ContactMenu(R.drawable.ic_close, "Remove Contact", "remove_contact"));
        } else {
            dataArrayList.add(new ContactMenu(R.drawable.ic_info, "More Details", "more_details_meeting"));
            dataArrayList.add(new ContactMenu(R.drawable.ic_event_note, "Add Meeting Note", "add_meeting_note"));
            if (meetingData != null && (meetingData.getStatus().equalsIgnoreCase("3") || meetingData.getStatus().equalsIgnoreCase("2"))) {

            } else {
                dataArrayList.add(new ContactMenu(R.drawable.ic_navigation, "Meeting held on Other Location", "meeting_other_location"));
                dataArrayList.add(new ContactMenu(R.drawable.ic_close, "Reschedule/ Cancel Meeting", "reschedule_cancel_meeting"));
            }
        }

        MenuDialogRecyclerAdapter adapter = new MenuDialogRecyclerAdapter(context, dataArrayList, meetingData, clientData);
        setRecyclerViewWithAdapter(recyclerViewData,
                new LinearLayoutManager(context),
                adapter,
                false);

    }

    @Override
    protected void bindEvent() {

    }
}
