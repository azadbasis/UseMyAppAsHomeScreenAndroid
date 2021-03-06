package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils;



import java.util.ArrayList;
import java.util.List;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.model.UserModel;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.LeaveTypeInfo;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.LoginResponse;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.User;

public class AppConstant {
    public static  String ARG_CHAT_ROOMS = "chat_room";
    public static String ARG_USERS = "users";

    //for notification
    public static String ARG_RECEIVER = "";
    public static String ARG_RECEIVER_UID = "";
    public static String ARG_RECEIVER_FIREBASE_TOKEN = "";

    public static String employee_id ="employee_id";
    public static String userEmail ="userEmail";
    public static String userPassword ="userPassword";
    public static String photo ="photo";
    public static String uid ="uid";
    public static String userName ="userName";
    public static String lat ="lat";
    public static String lng ="lng";
    public static String userFcm ="userFcm";
    public static String activity ="activity";
    public static String fcm_token ="fcm_token";
    public static int CAMERA_RUNTIME_PERMISSION=2,WRITEEXTERNAL_PERMISSION_RUNTIME=3,LOCATION_PERMISSION=4;
    public static boolean isGallery=false;
    public static List<User> userListSend =new ArrayList<>();
    public static User user_receiver = new User();
    public static List<String> registraion_ids = new ArrayList<>();


    public static LoginResponse loginResponse = new LoginResponse();
    public static List<LeaveTypeInfo> liveTypeList = new ArrayList<>();
    public static List<String> leaveTypeName = new ArrayList<>();
    public static ArrayList<UserModel> fUserList = new ArrayList<>();
}
