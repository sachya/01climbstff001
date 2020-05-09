package iostudio.in.et.retrofit.response;

import java.util.ArrayList;

public class Type extends CommonResponse{

    private ArrayList<TypeData> data;

    public ArrayList<TypeData> getData() {
        return data;
    }

    public void setData(ArrayList<TypeData> data) {
        this.data = data;
    }
}
