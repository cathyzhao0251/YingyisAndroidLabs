package algonquin.cst2335.zhao0251.data;
public class ChatMessage {
    String message;
    String timeSent;
    boolean isSentButton;

    public ChatMessage(String message, String timeSent, boolean isSentButton){
        this.message = message;
        this.timeSent = timeSent;
        this.isSentButton = isSentButton;
    }
    public String getMessage(){
        return this.message;
    }
    public String getTimeSent(){
        return this.timeSent;
    }
    public boolean getIsSentButton(){
        return this.isSentButton;
    }
}
