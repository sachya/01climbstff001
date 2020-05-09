package iostudio.in.et.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import iostudio.in.et.R;


/**
 * Created by anantshah on 16/04/18.¡
 */

public class ContactDialogViewHolders extends BaseViewHolder {


    public Context context;
    public AppCompatTextView tv_title;
    public AppCompatImageView iv_icon;
    public LinearLayout ll_contact_dialog;


    public ContactDialogViewHolders(View itemView, Context context) {
        super(itemView, context);
        this.context = context;
        tv_title=itemView.findViewById(R.id.tv_title);
        ll_contact_dialog=itemView.findViewById(R.id.ll_contact_dialog);
        iv_icon=itemView.findViewById(R.id.iv_icon);
    }

}
