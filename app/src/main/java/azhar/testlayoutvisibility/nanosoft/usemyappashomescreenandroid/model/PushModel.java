package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NanoSoft on 7/27/2017.
 */

public class PushModel {

    private Data data = new Data();
    private List<String> registraion_ids = new ArrayList<>();

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<String> getRegistraion_ids() {
        return registraion_ids;
    }

    public void setRegistraion_ids(List<String> registraion_ids) {
        this.registraion_ids = registraion_ids;
    }
}
