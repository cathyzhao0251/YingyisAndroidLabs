package algonquin.cst2335.zhao0251;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.zhao0251.data.ChatMessage;
import algonquin.cst2335.zhao0251.data.MessageViewModel;
import algonquin.cst2335.zhao0251.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.zhao0251.databinding.ReceiveRowBinding;
import algonquin.cst2335.zhao0251.databinding.SentRowBinding;

public class ChatRoom extends AppCompatActivity {

    ArrayList<ChatMessage> theMessages = null;
    MessageViewModel chatModel;

    ActivityChatRoomBinding binding ;
    RecyclerView.Adapter myAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get the data from the ViewModel:
        chatModel = new ViewModelProvider(this).get(MessageViewModel.class);
        theMessages = chatModel.theMessages.getValue();
        if(theMessages == null){
            chatModel.theMessages.postValue( theMessages = new ArrayList<ChatMessage>());
        }

        binding.sendButton.setOnClickListener( click ->{
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
            String newMessage = binding.newMessage.getText().toString();
            theMessages.add(new ChatMessage(binding.newMessage.getText().toString(), currentDateAndTime, true));
            binding.newMessage.setText("");//remove what you typed
            //tell the recycle view to update:
            //myAdapter.notifyDataSetChanged();//will redraw
            myAdapter.notifyItemInserted(theMessages.size()-1);
        });
        binding.receiveButton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
//            messages.add(binding.textInput.getText().toString());
            theMessages.add(new ChatMessage(binding.newMessage.getText().toString(), currentDateAndTime, false));
            myAdapter.notifyItemInserted(theMessages.size()-1);
            binding.newMessage.setText("");
        });

        //creates rows 0 to 50
        binding.myRecyclerView.setAdapter(
                myAdapter = new RecyclerView.Adapter<MyRowHolder>() {

                    //just inflate the XML
                    @NonNull @Override                                              // implement multiple layouts
                    public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        //viewType will be 0 for the first 3 rows, 1 for everything after

                        if(viewType == 0) {
                            SentRowBinding rowBinding = SentRowBinding.inflate(getLayoutInflater(), parent, false);
                            return new MyRowHolder(rowBinding.getRoot()); //call your constructor below
                        }
                        else {  //after row 3
                            ReceiveRowBinding rowBinding = ReceiveRowBinding.inflate(getLayoutInflater(), parent, false);
                            return new MyRowHolder(rowBinding.getRoot());
                        }
                    }

                    @Override
                    public int getItemViewType(int position) {
                        //given the row, return an layout id for that row

                        if(theMessages.get(position).getIsSentButton()) {
                            return 0;
                        }else
                            return 1;
                    }

                    @Override
                    public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                        //replace the default text with text at row position

                        String messageString = theMessages.get(position).getMessage();
                        String timeString = theMessages.get(position).getTimeSent();
                        holder.message.setText(messageString);
                        holder.time.setText(timeString);

                    }

                    //number of rows you want
                    @Override
                    public int getItemCount() {
                        return theMessages.size();
                    }
                }
        ); //populate the list

        binding.myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    //this represents a single row on the list
    class MyRowHolder extends RecyclerView.ViewHolder {

        public TextView message;
        public TextView time;

        public ImageView image;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            //like onCreate above
            image = itemView.findViewById(R.id.image);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time); //find the ids from XML to java
        }
    }
}