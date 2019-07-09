package com.tal.hide.ui.game;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tal.hide.GameActivity;
import com.tal.hide.R;
import com.tal.hide.WebClient;
import com.tal.hide.utils.Constants;

public class GameFragment extends Fragment {
    private boolean swap = true;
    final String TAG = "Knowingly GameFragment ";

    private GameViewModel mViewModel;

    public static GameFragment newInstance() {
        return new GameFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game_fragment, container, false);
        //setup image button for player to spin coin and get heads/tails
        final ImageButton spin = view.findViewById(R.id.btn_spin);
        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swap){
                    spin.setBackgroundResource(R.drawable.ic_sentiment_very_dissatisfied_black_24dp);
                    swap = false;
                }else{
                    spin.setBackgroundResource(R.drawable.ic_sentiment_neutral_orange_192dp);
                    swap = true;
                }
            }
        });
        //setup submit button
        final Button submit = view.findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send move to activity for submission
                ((GameActivity) getActivity()).submit(swap);
            }
        });
        //get opponent name
        Bundle args = getArguments();
        if(args != null) {
            //setup textfield for opponent
            TextView opponentTextView = view.findViewById(R.id.txt_opponent);
            opponentTextView.setText("Your opponent is :" + args.getString(Constants.keyOpponent));
            //setup textfield for role
            TextView roleTextView = view.findViewById(R.id.txt_role);
            roleTextView.setText("Your role is :" + args.getString(Constants.keyRole));
        }else{
            Log.d(TAG, " args are null ");
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        // TODO: Use the ViewModel
    }
}
