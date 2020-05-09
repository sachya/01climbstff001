package iostudio.in.et.adapter.viewholder;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView, Context activity) {
        super(itemView);

    }

    public void setRecyclerViewWithAdapter(RecyclerView recyclerView,
            RecyclerView.LayoutManager layoutManager,
            RecyclerView.Adapter<RecyclerView.ViewHolder> recyclerAdapter,
            boolean isFixedSize,
            boolean isAutoMeasureEnabled,
            boolean isNestedScrollingEnabled) {
        recyclerView.setHasFixedSize(isFixedSize);
        layoutManager.setAutoMeasureEnabled(isAutoMeasureEnabled);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(isNestedScrollingEnabled);
        recyclerView.setAdapter(recyclerAdapter);

    }
}
