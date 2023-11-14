package algonquin.cst2335.zhao0251.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MessageViewModel extends ViewModel {
    public MutableLiveData<ArrayList<ChatMessage>> theMessages = new MutableLiveData<>();
    public MutableLiveData<ChatMessage> selectedmessages = new MutableLiveData< >();
}
