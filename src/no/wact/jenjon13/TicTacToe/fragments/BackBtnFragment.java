package no.wact.jenjon13.TicTacToe.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import no.wact.jenjon13.TicTacToe.R;
import no.wact.jenjon13.TicTacToe.activities.MainMenuActivity;

public class BackBtnFragment extends Fragment implements OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.backbtnfragment, container, false);
        view.findViewById(R.id.btnBack).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBack) {
            startActivity(new Intent(getActivity(), MainMenuActivity.class));
        } else {
            Log.e("BackBtnFragment:onClick", "Unknown view: " + v.getId());
        }
    }
}
