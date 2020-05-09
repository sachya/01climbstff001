package iostudio.in.et.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import iostudio.in.et.R;
import iostudio.in.et.activity.CreateNewContactActivity;
import iostudio.in.et.activity.MeetingActivity;
import iostudio.in.et.activity.MenuDialogActivity;
import iostudio.in.et.adapter.viewholder.ContactViewHolders;
import iostudio.in.et.retrofit.response.ClientData;
import iostudio.in.et.utility.Constant;

/**
 * Created by Anant Shah on 24/04/15.
 */
public class ContactRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Context context;
    private ClientData currency;
    private ArrayList<ClientData> dataArrayList;
    private ArrayList<ClientData> dataArrayListAll;
    private boolean isShowCheckBox;
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

    public ContactRecyclerAdapter(Context context, ArrayList<ClientData> dataArrayList, boolean isShowCheckBox) {
        this.context = context;
        this.dataArrayList = dataArrayList;
        this.dataArrayListAll = dataArrayList;
        this.isShowCheckBox = isShowCheckBox;
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

    public void swap(ArrayList<ClientData> dataArrayList) {
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact, parent, false);
                ContactViewHolders viewHolder = new ContactViewHolders(view, context);
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
                ContactViewHolders mVHolder = (ContactViewHolders) holder;
                populateAdapterView(mVHolder, position);
                break;
            }
         /*   case VIEW_TYPE_LOADING: {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }*/
        }
    }


    private ClientData getItem(final int position) {
        return dataArrayList.get(position);
    }

    @SuppressLint("DefaultLocale")
    private void populateAdapterView(ContactViewHolders viewHolders, final int position) {
        try {

            ClientData data = getItem(position);
            if (data != null) {

                if (isShowCheckBox) {
                    viewHolders.checkbox_client.setVisibility(View.VISIBLE);
                    viewHolders.checkbox_client.setChecked(data.isChecked());
                } else {
                    viewHolders.checkbox_client.setVisibility(View.GONE);
                }


                String companyName = ""; //= data.getCompany_name();
                if (!TextUtils.isEmpty(data.getCompany_address())) {
                    companyName += data.getCompany_address();
                }
                viewHolders.tv_title.setText(companyName);


                String type = data.getClient_type();
                if (!TextUtils.isEmpty(type)) {
                    viewHolders.tv_type.setText(type);
                    viewHolders.tv_type.setVisibility(View.VISIBLE);
                } else {
                    viewHolders.tv_type.setVisibility(View.GONE);
                }


                String name = data.getOwner_name();
                if (!TextUtils.isEmpty(name)) {
                    viewHolders.tv_name.setText(name);
                    viewHolders.tv_name.setVisibility(View.VISIBLE);
                } else {
                    viewHolders.tv_name.setVisibility(View.GONE);
                }


            }

            viewHolders.tv_options.setText(Html.fromHtml(context.getString(R.string.more_icon)));

            viewHolders.cl_lead.setOnClickListener(new ClickHandler(viewHolders, position));
            viewHolders.checkbox_client.setOnClickListener(new ClickHandler(viewHolders, position));
            viewHolders.tv_options.setOnClickListener(new ClickHandler(viewHolders, position));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataArrayList = (ArrayList<ClientData>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<ClientData> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = dataArrayListAll;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected ArrayList<ClientData> getFilteredResults(String constraint) {
        ArrayList<ClientData> results = new ArrayList<>();

        for (ClientData item : dataArrayListAll) {
            if (!TextUtils.isEmpty(item.getOwner_name()) &&
                    item.getOwner_name().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }


    private class ClickHandler implements View.OnClickListener {

        int position;
        ContactViewHolders viewHolders;

        public ClickHandler(ContactViewHolders viewHolders,
                            int position) {
            this.position = position;
            this.viewHolders = viewHolders;
        }

        @Override
        public void onClick(View v) {
            final Intent intent;
            switch (v.getId()) {
                case R.id.tv_options:
                    try {
                        //creating a popup menu
                        PopupMenu popup = new PopupMenu(context, v);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.lead_list_menu);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent intentM;
                                switch (item.getItemId()) {
                                    case R.id.action_edit_lead:
                                        intentM = new Intent();
                                        intentM.setClass(context, CreateNewContactActivity.class);
                                        context.startActivity(intentM);
                                        //handle menu1 click
                                        return true;
                                    case R.id.action_schedule_meeting:
                                        //handle menu2 click
                                        intentM = new Intent();
                                        intentM.setClass(context, MeetingActivity.class);
                                        context.startActivity(intentM);
                                        return true;
                                    case R.id.action_mark_as_imp:
                                        //handle menu3 click
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        //displaying the popup
                        popup.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.checkbox_client:
                case R.id.cl_lead:
                    try {
                        if (!isShowCheckBox) {
                            intent = new Intent();
                            intent.putExtra(Constant.INTENT.CLIENT_OBJECT, dataArrayList.get(position));
                            intent.setClass(context, MenuDialogActivity.class);
                            context.startActivity(intent);
                        } else {
                            if (dataArrayList.get(position).isChecked()) {
                                dataArrayList.get(position).setChecked(false);
                            } else {
                                dataArrayList.get(position).setChecked(true);
                            }
                            Log.e("isChecked", " " + dataArrayList.get(position).isChecked());
                            notifyItemChanged(position);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }


    public interface ItemClickedListener {
        void onItemClicked(ClientData countryCode);
    }


}
