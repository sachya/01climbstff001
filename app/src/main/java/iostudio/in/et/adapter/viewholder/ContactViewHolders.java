package iostudio.in.et.adapter.viewholder;

import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import iostudio.in.et.R;


/**
 * Created by anantshah on 16/04/18.¡
 */

public class ContactViewHolders extends BaseViewHolder {


    public Context context;
    public AppCompatTextView tv_title;
    public AppCompatTextView tv_name;
    public AppCompatTextView tv_type;
    public AppCompatTextView tv_options;
    public AppCompatCheckBox checkbox_client;
    public ConstraintLayout cl_lead;


    public ContactViewHolders(View itemView, Context context) {
        super(itemView, context);
        this.context = context;
        tv_title=itemView.findViewById(R.id.tv_title);
        tv_type=itemView.findViewById(R.id.tv_type);
        tv_options=itemView.findViewById(R.id.tv_options);
        tv_name=itemView.findViewById(R.id.tv_name);
        cl_lead=itemView.findViewById(R.id.cl_lead);
        checkbox_client=itemView.findViewById(R.id.checkbox_client);
    }

}
