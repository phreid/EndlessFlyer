package com.phreid.endlessflyer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class HowToFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_howto, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.dialog_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
