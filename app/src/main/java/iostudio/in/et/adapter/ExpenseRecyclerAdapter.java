package iostudio.in.et.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import iostudio.in.et.R;
import iostudio.in.et.activity.EntryInformationActivity;
import iostudio.in.et.adapter.viewholder.ExpenseViewHolders;
import iostudio.in.et.retrofit.response.AccountData;
import iostudio.in.et.utility.Constant;

/**
 * Created by Anant Shah on 24/04/15.
 */
public class ExpenseRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private AccountData currency;
    private ArrayList<AccountData> dataArrayList;
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


    public ExpenseRecyclerAdapter(Context context, ArrayList<AccountData> dataArrayList,
                                  ItemClickedListener itemClickedListener) {
        this.context = context;
        this.itemClickedListener = itemClickedListener;
        this.dataArrayList = dataArrayList;
    }

    public ExpenseRecyclerAdapter(Context context, ArrayList<AccountData> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
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

    public void swap(ArrayList<AccountData> dataArrayList) {
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
                        .inflate(R.layout.list_item_expense, parent, false);
                ExpenseViewHolders viewHolder = new ExpenseViewHolders(view, context);
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
                ExpenseViewHolders mVHolder = (ExpenseViewHolders) holder;
                populateAdapterView(mVHolder, position);
                break;
            }
         /*   case VIEW_TYPE_LOADING: {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }*/
        }
    }


    private AccountData getItem(final int position) {
        return dataArrayList.get(position);
    }

    @SuppressLint("DefaultLocale")
    private void populateAdapterView(ExpenseViewHolders viewHolders, final int position) {
        try {
            AccountData data = getItem(position);
            if (data != null) {
                String name = data.getName();
                if (!TextUtils.isEmpty(name)) {
                    viewHolders.tv_expense.setText(name);
                    viewHolders.tv_expense.setVisibility(View.VISIBLE);
                } else {
                    viewHolders.tv_expense.setVisibility(View.GONE);
                }
                String amount = data.getAmount();
                if (!TextUtils.isEmpty(amount)) {
                    viewHolders.tv_expense_price.setText(amount + " ₹");
                    viewHolders.tv_expense.setVisibility(View.VISIBLE);
                } else {
                    viewHolders.tv_expense.setVisibility(View.GONE);
                }


                try {
                    if (position == 0) {
                        if (!TextUtils.isEmpty(data.getDate())) {
                            viewHolders.tv_date.setText(data.getDate());
                            viewHolders.tv_date.setVisibility(View.VISIBLE);
                        } else {
                            viewHolders.tv_date.setVisibility(View.GONE);
                        }
                    } else {
                        String oldPos = dataArrayList.get(position - 1).getDate();
                        String newPos = dataArrayList.get(position).getDate();
                        if (!TextUtils.isEmpty(oldPos) && !TextUtils.isEmpty(newPos)) {
                            if (oldPos.equalsIgnoreCase(newPos)) {
                                viewHolders.tv_date.setVisibility(View.GONE);
                            } else {
                                viewHolders.tv_date.setText(data.getDate());
                                viewHolders.tv_date.setVisibility(View.VISIBLE);
                            }
                        } else {
                            viewHolders.tv_date.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            viewHolders.cl_expense.setOnClickListener(new ClickHandler(viewHolders, position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ClickHandler implements View.OnClickListener {

        int position;
        ExpenseViewHolders viewHolders;

        public ClickHandler(ExpenseViewHolders viewHolders,
                            int position) {
            this.position = position;
            this.viewHolders = viewHolders;
        }

        @Override
        public void onClick(View v) {
            final Intent intent;
            switch (v.getId()) {
                case R.id.cl_expense:
                    intent = new Intent();
                    intent.putExtra(Constant.INTENT.ID,dataArrayList.get(position).getAccount_id());
                    intent.setClass(context, EntryInformationActivity.class);
                    ((Activity)context).startActivityForResult(intent,Constant.INTENT_RESULT.ACCOUNT_INFO);
                    break;
            }
        }
    }


    public interface ItemClickedListener {
        void onItemClicked(AccountData countryCode);
    }


}
