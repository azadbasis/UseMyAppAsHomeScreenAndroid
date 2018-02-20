package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model;

/**
 * Created by NanoSoft on 2/19/2018.
 */

public class PushData {

    private String message;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
}
