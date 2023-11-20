package algonquin.cst2335.zhao0251;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.zhao0251.data.ChatMessage;
import algonquin.cst2335.zhao0251.ChatMessageDAO;
import algonquin.cst2335.zhao0251.MessageDatabase;
import algonquin.cst2335.zhao0251.data.MessageDetailsFragment;

import algonquin.cst2335.zhao0251.data.MessageViewModel;
import algonquin.cst2335.zhao0251.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.zhao0251.databinding.ReceiveRowBinding;
import algonquin.cst2335.zhao0251.databinding.SentRowBinding;

public class ChatRoom extends AppCompatActivity {
    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages = null;

    MessageViewModel chatModel;
    private RecyclerView.Adapter myAdapter;

    ChatMessageDAO mDAO;
    ChatMessage clickedMessage;
    TextView globalMessageText;
    int position;
    MessageDetailsFragment chatFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

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
                if (messages.get(position).getIsSentButton() == true) // for the first 5 rows
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
        chatModel.selectedmessages.observe(this, (newMessageValue) -> {

            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.addToBackStack("");
            tx.add(R.id.fragmentLocation, chatFragment);
            tx.commit();
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
                        ChatMessage clickedMessage = messages.get(position);
                        chatModel.selectedmessages.postValue(clickedMessage);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                        builder.setMessage("Do you want to delete the message: " + messageText.getText())
                                .setTitle("Question: ")
                                .setPositiveButton("Yes", (dialog, cl) -> {
                                    Executor thread = Executors.newSingleThreadExecutor();
                                    thread.execute(() ->
                                    {
                                        mDAO.deleteMessage(clickedMessage);
                                    });

                                    messages.remove(position);
                                    myAdapter.notifyDataSetChanged();

                                    Snackbar.make(itemView, "You deleted message #" + (position+1), Snackbar.LENGTH_LONG)
                                            .setAction("Undo", click ->{
                                                Executor thread1 = Executors.newSingleThreadExecutor();
                                                thread.execute(() ->
                                                {
                                                    mDAO.insertMessage(clickedMessage);
                                                });
                                                messages.add(position, clickedMessage);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_1:
                String messageText = chatModel.selectedmessages.getValue().getMessage();
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete the message:" + messageText);
                builder.setTitle("Question:")
                        .setPositiveButton("Yes", (dialog, cl) -> {
                            ChatMessage m = messages.get(position);

                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() -> {
                                mDAO.deleteMessage(clickedMessage);
                            });

                            messages.remove(position);
                            myAdapter.notifyItemRemoved(position);
                            removeFragment();
                            Snackbar.make(globalMessageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk2 -> thread.execute(() -> {
                                        mDAO.insertMessage(clickedMessage);
                                        messages.add(position, clickedMessage);
                                        runOnUiThread(() -> myAdapter.notifyItemInserted(position));
                                    }))
                                    .show();
                        })
                        .setNegativeButton("No", (dialog, cl) -> {
                        })
                        .create().show();
                break;

            case R.id.item_2:
                Toast.makeText(this, "Version 1.0. create by Yingyi Zhao", Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new IllegalStateException("Unexpected value" + item.getItemId());
        }
        return true;
    }

    public void removeFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();

        }
    }
}