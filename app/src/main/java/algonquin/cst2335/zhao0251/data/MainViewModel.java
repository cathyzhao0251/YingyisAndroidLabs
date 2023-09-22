package algonquin.cst2335.zhao0251.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    //list data to save off-screen
    public MutableLiveData<String> userString = new MutableLiveData("");

    public MutableLiveData<Boolean> onOrOff = new MutableLiveData<Boolean>(false);

}
