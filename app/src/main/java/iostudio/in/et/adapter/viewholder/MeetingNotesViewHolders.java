package iostudio.in.et.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import iostudio.in.et.R;


/**
 * Created by anantshah on 16/04/18.¡
 */

public class MeetingNotesViewHolders extends BaseViewHolder {


    public Context context;
    public AppCompatTextView tv_title;
    public LinearLayout ll_meeting_notes;


    public MeetingNotesViewHolders(View itemView, Context context) {
        super(itemView, context);
        this.context = context;
        tv_title=itemView.findViewById(R.id.tv_title);
        ll_meeting_notes=itemView.findViewById(R.id.ll_meeting_notes);
    }

}
