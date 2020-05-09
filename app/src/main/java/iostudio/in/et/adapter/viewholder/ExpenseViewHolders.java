package iostudio.in.et.adapter.viewholder;

import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import iostudio.in.et.R;


/**
 * Created by anantshah on 16/04/18.¡
 */

public class ExpenseViewHolders extends BaseViewHolder {


    public Context context;
    public ConstraintLayout cl_expense;
    public AppCompatTextView tv_date;
    public AppCompatTextView tv_expense;
    public AppCompatTextView tv_expense_price;


    public ExpenseViewHolders(View itemView, Context context) {
        super(itemView, context);
        this.context = context;
        cl_expense=itemView.findViewById(R.id.cl_expense);
        tv_date=itemView.findViewById(R.id.tv_date);
        tv_expense=itemView.findViewById(R.id.tv_expense);
        tv_expense_price=itemView.findViewById(R.id.tv_expense_price);
    }

}
