package iostudio.in.et.utility;

import android.widget.AbsListView;

/**
 * Created by rohit on 20/04/15.
 */
public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {


    String tag = "EndlessScrollListener";
    private int currentPage = 0;
    private boolean isLoading = false;
    private int mLastFirstVisibleItem;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (mLastFirstVisibleItem < firstVisibleItem) {
            upScrolling();
        }
        if (mLastFirstVisibleItem > firstVisibleItem) {
            downScrolling();
        }


        mLastFirstVisibleItem = firstVisibleItem;


    }

    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(int page, EndlessScrollListener endlessScrollListener);


    public abstract void upScrolling();

    public abstract void downScrolling();


    //public abstract void onLoadMore(EndlessScrollListener endlessScrollListener);

    public void stopLoading() {
        isLoading = false;
    }
    /*public boolean stopLoading() {
        isLoading = false;
        return isLoading;
    }
*/
    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollState) {
        // Don't take any action on changed

        // This doesn't work properly because scroll position is changed

        if (scrollState == SCROLL_STATE_IDLE) {
            //Log.e("getLastVisiblePosition", ":" + listView.getLastVisiblePosition() + " getCount:" + (listView.getCount() - 1) + " isLoading:" + !isLoading);
            if ((listView.getLastVisiblePosition() >= listView.getCount() - 1) && (!isLoading)) {

                isLoading = true;
                currentPage++;
                //load more list items:
                onLoadMore(currentPage, this);
            }
        }

    }
}
