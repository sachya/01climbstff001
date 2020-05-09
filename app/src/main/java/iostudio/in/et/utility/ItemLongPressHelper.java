package iostudio.in.et.utility;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by adwait on 11/10/17.
 */

public class ItemLongPressHelper implements View.OnTouchListener {

    private GestureDetector.SimpleOnGestureListener listener;
    private Context mContext;
    private View mView;
    private GestureDetector mDetector;

    public void setView(View view,GestureDetector.SimpleOnGestureListener listener){
        mContext = view.getContext();
        this.listener = listener;
        mView = view;
        mView.setOnTouchListener(this);
        mDetector = new GestureDetector(mContext,this.listener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }
}
