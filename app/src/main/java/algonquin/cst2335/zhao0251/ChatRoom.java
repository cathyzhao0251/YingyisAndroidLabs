package algonquin.cst2335.zhao0251;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.zhao0251.data.ChatMessage;
import algonquin.cst2335.zhao0251.data.MessageViewModel;
import algonquin.cst2335.zhao0251.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.zhao0251.databinding.ReceiveRowBinding;
import algonquin.cst2335.zhao0251.databinding.SentRowBinding;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages = null;

    MessageViewModel chatModel;
    private RecyclerView.Adapter myAdapter;

    ChatMessageDAO mDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(MessageViewModel.class);
        messages = chatModel.theMessages.getValue();


        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        if (messages == null) {
            chatModel.theMessages.postValue(messages = new ArrayList<ChatMessage>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll(mDAO.getAllMessages()); //Once you get the data from database

                runOnUiThread(() -> binding.myRecyclerView.setAdapter(myAdapter)); //You can then load the RecyclerView
            });
        }

        binding.sendButton.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            String inputMessage = binding.newMessage.getText().toString();
            boolean sentButton = true;

            ChatMessage m = new ChatMessage(inputMessage, currentDateandTime, sentButton);
            messages.add(m);

            // clear teh previous text
            binding.newMessage.setText("");

            myAdapter.notifyDataSetChanged();

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                m.id = mDAO.insertMessage(m); //Once you get the data from database
                Log.d("TAG", "The id created is" + m.id);
            });

        });


        binding.receiveButton.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
            String inputMessage = binding.newMessage.getText().toString();
            boolean sentButton = false;

            ChatMessage m = new ChatMessage(inputMessage, currentDateAndTime, sentButton);
            messages.add(m);

            myAdapter.notifyDataSetChanged();

            // clear teh previous text
            binding.newMessage.setText("");

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                m.id = mDAO.insertMessage(m); //Once you get the data from database
                Log.d("TAG", "The id created is" + m.id);
            });

        });

        // will draw the recycle view.
        binding.myRecyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            @Override
            public int getItemViewType(int position) {
                // determine which layout to load at row position
                if (messages.get(position).isSentButton() == true) // for the first 5 rows
                {
                    return 0;
                } else return 1;
            }

            @NonNull
            @Override                                                       // which layout to load?
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                // viewType will either be 0 or 1

                if (viewType == 0) {
                    // 1. load a XML layout
                    SentRowBinding binding =                            // parent is incase matchparent
                            SentRowBinding.inflate(getLayoutInflater(), parent, false);

                    // 2. call our constructor below
                    return new MyRowHolder(binding.getRoot()); // getRoot returns a ConstraintLayout with TextViews inside
                } else {
                    // 1. load a XML layout
                    ReceiveRowBinding binding =                            // parent is incase matchparent
                            ReceiveRowBinding.inflate(getLayoutInflater(), parent, false);

                    // 2. call our constructor below
                    return new MyRowHolder(binding.getRoot()); // getRoot returns a ConstraintLayout with TextViews inside

                }
            }


            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage obj = messages.get(position);

                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }
        });

        binding.myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    class MyRowHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        public TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(
                    clk -> {

                        int position = getAbsoluteAdapterPosition();
                        ChatMessage toDelete = messages.get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                        builder.setMessage("Do you want to delete the message: " + messageText.getText())
                                .setTitle("Question: ")
                                .setPositiveButton("Yes", (dialog, cl) -> {
                                    Executor thread = Executors.newSingleThreadExecutor();
                                    thread.execute(() ->
                                    {
                                        mDAO.deleteMessage(toDelete);
                                    });

                                    messages.remove(position);
                                    myAdapter.notifyDataSetChanged();

                                    Snackbar.make(itemView, "You deleted message #" + (position+1), Snackbar.LENGTH_LONG)
                                            .setAction("Undo", click ->{
                                                Executor thread1 = Executors.newSingleThreadExecutor();
                                                thread.execute(() ->
                                                {
                                                    mDAO.insertMessage(toDelete);
                                                });
                                                messages.add(position, toDelete);
                                                myAdapter.notifyDataSetChanged();
                                            })
                                            .show();
                                })
                                .setNegativeButton("No", (dialog, cl) -> {
                                })
                                .create().show();
                    });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
}