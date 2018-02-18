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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
