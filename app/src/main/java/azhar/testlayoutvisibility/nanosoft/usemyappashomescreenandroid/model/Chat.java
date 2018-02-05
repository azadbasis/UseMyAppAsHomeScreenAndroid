package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model;

/**
 * Created by delaroy on 4/13/17.
 */
public class Chat {
    public String sender;
    public String receiver;
    public String senderUid;
    public String receiverUid;
    public String message;
    public long timestamp;

    public Chat(){

    }
    public Chat(String message,String senderUid){
        this.message = message;
        this.senderUid = senderUid;
    }
    public Chat(String sender, String receiver, String senderUid, String receiverUid, String message){
        this.sender = sender;
        this.receiver = receiver;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.message = message;

    }





}
