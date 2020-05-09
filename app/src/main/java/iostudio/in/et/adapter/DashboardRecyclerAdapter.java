package iostudio.in.et.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import iostudio.in.et.R;
import iostudio.in.et.activity.LeadActivity;
import iostudio.in.et.activity.LeaveActivity;
import iostudio.in.et.activity.TourActivity;
import iostudio.in.et.adapter.viewholder.DashboardViewHolders;
import iostudio.in.et.utility.Utility;

/**
 * Created by Anant Shah on 24/04/15.
 */
public class DashboardRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private String currency;
    private ArrayList<String> dataArrayList;
    private ArrayList<String> dataArrayList1;
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


    public DashboardRecyclerAdapter(Context context, ArrayList<String> dataArrayList,
                                    ItemClickedListener itemClickedListener) {
        this.context = context;
        this.itemClickedListener = itemClickedListener;
        this.dataArrayList = dataArrayList;
    }

    public DashboardRecyclerAdapter(Context context, ArrayList<String> dataArrayList, ArrayList<String> dataArrayList1,ItemClickedListener itemClickedListener) {
        this.context = context;
        this.dataArrayList = dataArrayList;
        this.dataArrayList1 = dataArrayList1;
        this.itemClickedListener = itemClickedListener;
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
        int size = dataArrayList != null ? dataArrayList.size() : 1;


        if (isErrorView) {
            size = 1;
        }
        return size;
    }

    public void swap(ArrayList<String> dataArrayList) {
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
                        .inflate(R.layout.list_item_dashboard, parent, false);
                DashboardViewHolders viewHolder = new DashboardViewHolders(view, context);
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
                DashboardViewHolders mVHolder = (DashboardViewHolders) holder;
                populateAdapterView(mVHolder, position);
                break;
            }
         /*   case VIEW_TYPE_LOADING: {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }*/
        }
    }


    private String getItem(final int position) {
        return dataArrayList.get(position);
    }

    @SuppressLint("DefaultLocale")
    private void populateAdapterView(DashboardViewHolders viewHolders, final int position) {
        try {
            String title = getItem(position);
            viewHolders.textViewTitle.setText(title);
            viewHolders.textViewSubTitle.setText(dataArrayList1.get(position));
            viewHolders.linear_layout_profile.setOnClickListener(new ClickHandler(viewHolders, position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ClickHandler implements View.OnClickListener {

        int position;
        DashboardViewHolders viewHolders;

        public ClickHandler(DashboardViewHolders viewHolders,
                            int position) {
            this.position = position;
            this.viewHolders = viewHolders;
        }

        @Override
        public void onClick(View v) {
            itemClickedListener.onItemClicked(v,position);
        }
    }


    public interface ItemClickedListener {
        void onItemClicked(View v, int position);
    }


}
