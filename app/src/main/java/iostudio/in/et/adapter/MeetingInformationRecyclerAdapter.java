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
import iostudio.in.et.activity.MeetingInformationActivity;
import iostudio.in.et.adapter.viewholder.MeetingNotesViewHolders;
import iostudio.in.et.retrofit.response.Note;

/**
 * Created by Anant Shah on 24/04/15.
 */
public class MeetingInformationRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Note currency;
    private ArrayList<Note> dataArrayList;
    private ArrayList<Note> dataArrayList1;
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


    public MeetingInformationRecyclerAdapter(Context context, ArrayList<Note> dataArrayList,
                                             ItemClickedListener itemClickedListener) {
        this.context = context;
        this.itemClickedListener = itemClickedListener;
        this.dataArrayList = dataArrayList;
    }

    public MeetingInformationRecyclerAdapter(Context context, ArrayList<Note> dataArrayList) {
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
        return size;
    }

    public void swap(ArrayList<Note> dataArrayList) {
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
                        .inflate(R.layout.list_item_meeting_notes, parent, false);
                MeetingNotesViewHolders viewHolder = new MeetingNotesViewHolders(view, context);
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
                MeetingNotesViewHolders mVHolder = (MeetingNotesViewHolders) holder;
                populateAdapterView(mVHolder, position);
                break;
            }
         /*   case VIEW_TYPE_LOADING: {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }*/
        }
    }


    private Note getItem(final int position) {
        return dataArrayList.get(position);
    }

    @SuppressLint("DefaultLocale")
    private void populateAdapterView(MeetingNotesViewHolders holders, final int position) {
        try {
            Note note = getItem(position);
            if (note != null) {
                if (!TextUtils.isEmpty(note.getNote())) {
                    holders.tv_title.setText(note.getNote());
                    holders.tv_title.setVisibility(View.VISIBLE);
                } else {
                    holders.tv_title.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ClickHandler implements View.OnClickListener {

        int position;
        MeetingNotesViewHolders viewHolders;

        public ClickHandler(MeetingNotesViewHolders viewHolders,
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
                    intent.setClass(context, MeetingInformationActivity.class);
                    context.startActivity(intent);
                    break;
            }
        }
    }


    public interface ItemClickedListener {
        void onItemClicked(Note countryCode);
    }


}
