package iostudio.in.et.utility;



import java.util.ArrayList;

import iostudio.in.et.retrofit.response.CommonResponse;


public final class PortfolioUtils {
    static int currentOffset;

    PortfolioUtils() {
    }

    public static ArrayList<CommonResponse> moarItems(ArrayList<String> images, int qty) {
        ArrayList<CommonResponse> items = new ArrayList<>();
        boolean isVertical= true;
        int span1 = 2;
        int span2 = 1;
        int width,height;

        for (int position = 0; position < qty; position++) {
           // int colSpan = Math.random() < 0.2f ? 2 : 1;
            // Swap the next 2 lines to have items with variable
            // column/row span.
            // int rowSpan = Math.random() < 0.2f ? 2 : 1;
            //int rowSpan = colSpan;

            if ((position == 0) || (position % 6) == 0 || (position % 6) == 4) {
                span1 = 2;
                span2 = 2;
                //  width = holder.relativeLayout_now.getWidth();
                width = 200;
                height = 200;
            } else {
                span1 = 1;
                span2 = 1;
                width = 60;
                height = 60;
            }
            final int colSpan = (isVertical ? span2 : span1);
            final int rowSpan = (isVertical ? span1 : span2);




            //CommonResponse item = new CommonResponse(colSpan, rowSpan, currentOffset + position, images.get(position),width,height);
            //items.add(item);
        }

        currentOffset += qty;

        return items;
    }
}
