package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model;

/**
 * Created by NanoSoft on 7/24/2017.
 */

public class User {

    public String uid;
    public String name;
    public String email;
    public String firebaseToken;

    public User(){

    }

    public User(String uid, String email,String name, String firebaseToken){
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.firebaseToken = firebaseToken;
    }
}
