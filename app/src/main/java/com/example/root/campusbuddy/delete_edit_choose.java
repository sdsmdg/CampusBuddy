package com.example.root.campusbuddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by rc on 18/5/15.
 */
public class delete_edit_choose extends DialogFragment {

    public interface ChooseDialogListener {
        public void DeleteClickListener();
        public void EditClickListener();
    }

    // Use this instance of the interface to deliver action events
    ChooseDialogListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ChooseDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ChooseDialogListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.activity_deleteand_edit_events2, null));

        AlertDialog dialog = builder.create();

        TextView tv_edit = (TextView) dialog.findViewById(R.id.text_edit);
        TextView tv_delete = (TextView) dialog.findViewById(R.id.text_delete);

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.DeleteClickListener();
            dismiss();
            }
        });
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.EditClickListener();
                dismiss();
            }
        });


        return dialog;
    }

}
