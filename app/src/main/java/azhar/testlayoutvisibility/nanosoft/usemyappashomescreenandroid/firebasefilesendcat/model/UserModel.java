package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.model;

import com.google.firebase.database.Exclude;

/**
 * Created by Alessandro Barreto on 22/06/2016.
 */
public class UserModel {

    private String id;
    private String name;
    private String photo_profile;
    private String email;
    private String firebaseToken;

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

    public UserModel() {
    }

    public UserModel(String name, String photo_profile, String id,String email, String firebaseToken) {
        this.name = name;
        this.photo_profile = photo_profile;
        this.id = id;
        this.firebaseToken = firebaseToken;
        this.email = email;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto_profile() {
        return photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
