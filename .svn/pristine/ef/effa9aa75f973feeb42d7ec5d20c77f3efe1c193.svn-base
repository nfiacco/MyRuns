package edu.dartmouth.cs.myruns;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Comment:");

        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.comment_dialog_layout, null);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        EditText commentText = (EditText) view.findViewById(R.id.dialogCommentEntry);

        commentText.setText(settings.getString("comment_key",""));

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = settings.edit();

                EditText commentText = (EditText) view.findViewById(R.id.dialogCommentEntry);

                editor.putString("comment_key", commentText.getText().toString());
                editor.commit();

                CommentFragment.this.getDialog().cancel();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                CommentFragment.this.getDialog().cancel();
            }
        });

        return alertDialogBuilder.create();
    }

}

