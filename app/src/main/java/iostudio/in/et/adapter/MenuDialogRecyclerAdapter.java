package iostudio.in.et.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import iostudio.in.et.R;
import iostudio.in.et.activity.AddMeetingNoteActivity;
import iostudio.in.et.activity.ContactDetailsActivity;
import iostudio.in.et.activity.CreateNewContactActivity;
import iostudio.in.et.activity.CreateNewMeetingActivity1;
import iostudio.in.et.activity.MapsActivity;
import iostudio.in.et.activity.MeetingInformationActivity;
import iostudio.in.et.activity.MenuDialogActivity;
import iostudio.in.et.activity.OtherLocationMeetingActivity;
import iostudio.in.et.activity.RescheduleCancelMeetingActivity;
import iostudio.in.et.adapter.viewholder.ContactDialogViewHolders;
import iostudio.in.et.retrofit.response.ClientData;
import iostudio.in.et.retrofit.response.ContactMenu;
import iostudio.in.et.retrofit.response.MeetingData;
import iostudio.in.et.utility.Constant;

/**
 * Created by Anant Shah on 24/04/15.
 */
public class MenuDialogRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ContactMenu currency;
    private ArrayList<ContactMenu> dataArrayList;
    private boolean isErrorView = false;
    private final int TYPE_ERROR = -1;
    private final int TYPE_CELL = 1;
    private int isShowAll = 0;
    private final int VIEW_TYPE_LOADING = 2;
    ItemClickedListener itemClickedListener;
    MeetingData meetingData;
    ClientData clientData;

    public void setAndShowError() {
        isErrorView = true;
        notifyDataSetChanged();
    }


    public MenuDialogRecyclerAdapter(Context context, ArrayList<ContactMenu> dataArrayList, ItemClickedListener itemClickedListener) {
        this.context = context;
        this.itemClickedListener = itemClickedListener;
        this.dataArrayList = dataArrayList;
    }

    public MenuDialogRecyclerAdapter(Context context, ArrayList<ContactMenu> dataArrayList, MeetingData meetingData, ClientData clientData) {
        this.context = context;
        this.dataArrayList = dataArrayList;
        this.meetingData = meetingData;
        this.clientData = clientData;
    }


    @Override
    public int getItemViewType(int position) {
        switch (position) {
            default: {
                try {
                    if (isErrorView) {
                        return TYPE_ERROR;
                    } else {
                       /* return dataArrayList.get(position) == null ? VIEW_TYPE_LOADING :
                                TYPE_CELL;*/
                        return TYPE_CELL;
                    }
                } catch (Exception e) {
                    return TYPE_CELL;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        int size = dataArrayList != null ? dataArrayList.size() : 0;
        if (isErrorView) {
            size = 1;
        }
        return size;
    }

    public void swap(ArrayList<ContactMenu> dataArrayList) {
        try {
            this.dataArrayList = dataArrayList;
            this.currency = currency;
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact_dialog, parent, false);
                return new ContactDialogViewHolders(view, context);
            }
          /*  case VIEW_TYPE_LOADING: {
                view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.layout_loading_item, parent, false);
                return new LoadingViewHolder(view);
            }*/

          /*  case TYPE_ERROR: {
                view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.layout_loading_item, parent, false);
                return new LoadingViewHolder(view);
            }*/

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_ERROR: {
                break;
            }
            case TYPE_CELL: {
                ContactDialogViewHolders mVHolder = (ContactDialogViewHolders) holder;
                populateAdapterView(mVHolder, position);
                break;
            }
         /*   case VIEW_TYPE_LOADING: {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }*/
        }
    }


    private ContactMenu getItem(final int position) {
        return dataArrayList.get(position);
    }

    @SuppressLint("DefaultLocale")
    private void populateAdapterView(ContactDialogViewHolders viewHolders, final int position) {
        try {

            ContactMenu data = getItem(position);
            if (data != null) {

                String name = data.getTitle();
                viewHolders.tv_title.setText(name);
                viewHolders.iv_icon.setBackgroundResource(data.getIcon());

            }

            viewHolders.ll_contact_dialog.setOnClickListener(new ClickHandler(viewHolders, position));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ClickHandler implements View.OnClickListener {

        int position;
        ContactDialogViewHolders viewHolders;

        public ClickHandler(ContactDialogViewHolders viewHolders,
                            int position) {
            this.position = position;
            this.viewHolders = viewHolders;
        }

        @Override
        public void onClick(View v) {
            final Intent intent;
            switch (v.getId()) {
                case R.id.ll_contact_dialog:
                    try {
                        switch (dataArrayList.get(position).getAction()) {
                            case "more_details":
                                intent = new Intent();
                                intent.putExtra(Constant.INTENT.CLIENT_OBJECT, clientData);
                                intent.setClass(context, ContactDetailsActivity.class);
                                context.startActivity(intent);
                                ((MenuDialogActivity) context).finish();
                                break;
                            case "more_details_meeting":
                                intent = new Intent();
                                intent.putExtra(Constant.INTENT.MEETING_OBJECT, meetingData);
                                intent.setClass(context, MeetingInformationActivity.class);
                                context.startActivity(intent);
                                ((MenuDialogActivity) context).finish();
                                break;
                            case "add_meeting_note":
                                intent = new Intent();
                                intent.putExtra(Constant.INTENT.MEETING_OBJECT, meetingData);
                                intent.setClass(context, AddMeetingNoteActivity.class);
                                context.startActivity(intent);
                                ((MenuDialogActivity) context).finish();
                                break;
                            case "meeting_other_location":
                                intent = new Intent();
                                intent.putExtra(Constant.INTENT.MEETING_OBJECT, meetingData);
                                intent.setClass(context, OtherLocationMeetingActivity.class);
                                context.startActivity(intent);
                                ((MenuDialogActivity) context).finish();
                                break;
                            case "reschedule_cancel_meeting":
                                intent = new Intent();
                                intent.putExtra(Constant.INTENT.MEETING_OBJECT, meetingData);
                                intent.setClass(context, RescheduleCancelMeetingActivity.class);
                                context.startActivity(intent);
                                ((MenuDialogActivity) context).finish();
                                break;
                            case "update_location":
                                if (((MenuDialogActivity) context).isGPSEnabled(context)) {
                                    intent = new Intent();
                                    intent.putExtra(Constant.INTENT.CLIENT_OBJECT, clientData);
                                    intent.setClass(context, MapsActivity.class);
                                    context.startActivity(intent);
                                    ((MenuDialogActivity) context).finish();
                                }else {
                                    ((MenuDialogActivity)context).showMessage(context.getString(R.string.error_turn_on_gps));
                                }
                                break;
                            case "edit_contact":
                                intent = new Intent();
                                intent.putExtra(Constant.INTENT.CLIENT_OBJECT, clientData);
                                intent.setClass(context, CreateNewContactActivity.class);
                                context.startActivity(intent);
                                ((MenuDialogActivity) context).finish();
                                break;
                            case "add_meeting":
                                intent = new Intent();
                                intent.putExtra(Constant.INTENT.CLIENT_OBJECT, clientData);
                                intent.setClass(context, CreateNewMeetingActivity1.class);
                                context.startActivity(intent);
                                ((MenuDialogActivity) context).finish();
                                break;
                            case "remove_contact":

                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.cl_lead:
                    try {
                        intent = new Intent();
                        intent.setClass(context, MenuDialogActivity.class);
                        context.startActivity(intent);
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }


    public interface ItemClickedListener {
        void onItemClicked(ContactMenu countryCode);
    }


}
