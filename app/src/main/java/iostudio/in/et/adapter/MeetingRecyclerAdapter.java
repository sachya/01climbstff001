package iostudio.in.et.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import iostudio.in.et.R;
import iostudio.in.et.activity.MenuDialogActivity;
import iostudio.in.et.adapter.viewholder.MeetingViewHolders;
import iostudio.in.et.retrofit.response.MeetingData;
import iostudio.in.et.utility.Constant;
import iostudio.in.et.utility.UtilDate;

/**
 * Created by Anant Shah on 24/04/15.
 */
public class MeetingRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private String currency;
    private ArrayList<MeetingData> dataArrayList;
    private ArrayList<MeetingData> dataArrayList1;
    private boolean isErrorView = false;
    private final int TYPE_ERROR = -1;
    private final int TYPE_CELL = 1;
    private int isShowAll = 0;
    private final int VIEW_TYPE_LOADING = 2;
    ItemClickedListener itemClickedListener;

    public void setAndShowError() {
        isErrorView = true;
        notifyDataSetChanged();
    }


    public MeetingRecyclerAdapter(Context context, ArrayList<MeetingData> dataArrayList,
                                  ItemClickedListener itemClickedListener) {
        this.context = context;
        this.itemClickedListener = itemClickedListener;
        this.dataArrayList = dataArrayList;
    }

    public MeetingRecyclerAdapter(Context context, ArrayList<MeetingData> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
        this.dataArrayList1 = dataArrayList1;
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
        //  size = 10;
        return size;
    }

    public void swap(ArrayList<MeetingData> dataArrayList) {
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
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_meeting, parent, false);
                MeetingViewHolders viewHolder = new MeetingViewHolders(view, context);
                return viewHolder;
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
                MeetingViewHolders mVHolder = (MeetingViewHolders) holder;
                populateAdapterView(mVHolder, position);
                break;
            }
         /*   case VIEW_TYPE_LOADING: {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }*/
        }
    }


    private MeetingData getItem(final int position) {
        return dataArrayList.get(position);
    }

    @SuppressLint("DefaultLocale")
    private void populateAdapterView(MeetingViewHolders holders, final int position) {
        try {
            MeetingData data = getItem(position);
            if (data != null) {
                if (!TextUtils.isEmpty(data.getName())) {
                    holders.tv_name.setText(data.getName());
                    holders.tv_name.setVisibility(View.VISIBLE);
                } else {
                    holders.tv_name.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(data.getType_name())) {
                    holders.tv_type.setText(data.getType_name());
                    holders.tv_type.setVisibility(View.VISIBLE);
                } else {
                    holders.tv_type.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(data.getAddress())) {
                    holders.tv_address.setText(data.getAddress());
                    holders.tv_address.setVisibility(View.VISIBLE);
                } else {
                    holders.tv_address.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(data.getStatus_name())) {
                    holders.tv_status.setText(data.getStatus_name());
                    holders.tv_status.setVisibility(View.VISIBLE);
                } else {
                    holders.tv_status.setVisibility(View.GONE);
                }

                String status = data.getStatus();
                if (!TextUtils.isEmpty(status)) {
                    if (status.equalsIgnoreCase("1")) {
                        holders.tv_status.setBackgroundResource(R.drawable.status_pending);
                    } else if (status.equalsIgnoreCase("2")) {
                        holders.tv_status.setBackgroundResource(R.drawable.status_complete);
                    } else {
                        holders.tv_status.setBackgroundResource(R.drawable.status_cancelled);
                    }
                }

                try {
                    if (position==0){
                        holders.tv_date.setText(UtilDate.getDateFromDate(data.getMeeting_date()));
                        holders.tv_date.setVisibility(View.VISIBLE);
                    }else {
                        String oldDate = dataArrayList.get(position-1).getMeeting_date();
                        String newDate = dataArrayList.get(position).getMeeting_date();
                        if (!oldDate.equalsIgnoreCase(newDate)){
                            holders.tv_date.setVisibility(View.VISIBLE);
                        }else {
                            holders.tv_date.setText(UtilDate.getDateFromDate(data.getMeeting_date()));
                            holders.tv_date.setVisibility(View.GONE);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holders.cl_meeting.setOnClickListener(new ClickHandler(holders, position));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ClickHandler implements View.OnClickListener {

        int position;
        MeetingViewHolders viewHolders;

        public ClickHandler(MeetingViewHolders viewHolders,
                            int position) {
            this.position = position;
            this.viewHolders = viewHolders;
        }

        @Override
        public void onClick(View v) {
            final Intent intent;
            switch (v.getId()) {
                case R.id.cl_meeting:
                    intent = new Intent();
                    intent.putExtra(Constant.INTENT.TYPE, 1);
                    intent.putExtra(Constant.INTENT.MEETING_OBJECT, dataArrayList.get(position));
                    intent.setClass(context, MenuDialogActivity.class);
                    context.startActivity(intent);
                    break;
            }
        }
    }


    public interface ItemClickedListener {
        void onItemClicked(String countryCode);
    }


}
