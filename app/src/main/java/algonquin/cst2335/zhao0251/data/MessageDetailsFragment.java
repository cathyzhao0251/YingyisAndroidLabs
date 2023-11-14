package algonquin.cst2335.zhao0251.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.zhao0251.R;
import algonquin.cst2335.zhao0251.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {
    ChatMessage selected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);

        binding.message.setText(selected.getMessage());
        binding.time.setText(selected.getTimeSent());
        binding.sendReceive.setText(selected.getIsSentButton() ? "Send" : "Receive");
        binding.databaseID.setText("Id = " + selected.id);

        View rootView = binding.getRoot();
        if (getResources().getConfiguration().smallestScreenWidthDp < 600) {
            rootView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        } else {
            return rootView;
        }
        return rootView;
    }

    public MessageDetailsFragment(ChatMessage m) {
        selected = m;
    }
}
