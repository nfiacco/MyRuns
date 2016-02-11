package edu.dartmouth.cs.myruns;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by Nick on 2/2/15.
 */
public class DynamicDialog extends DialogFragment {

    private String title;
    private int type;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReturnType(int type) {
        this.type=type;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        switch (type){
            case 0:
                alertDialogBuilder.setView(R.layout.double_dialog_layout);
                break;
            case 1:
                alertDialogBuilder.setView(R.layout.double_dialog_layout);
                break;
            case 2:
                alertDialogBuilder.setView(R.layout.int_dialog_layout);
                break;
            case 3:
                alertDialogBuilder.setView(R.layout.int_dialog_layout);
                break;
            case 4:
                alertDialogBuilder.setView(R.layout.comment_dialog_layout);
                break;
            default:
                alertDialogBuilder.setView(R.layout.int_dialog_layout);
                break;
        }

        alertDialogBuilder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                EditText editText;
                Dialog f = (Dialog) dialog;
                switch (type){
                    case 0:
                        editText = (EditText) f.findViewById(R.id.dialogDoubleEntry);
                        ((PositiveListener) getActivity()).setDuration(Double.parseDouble(editText.getText().toString()));
                        break;
                    case 1:
                        editText = (EditText) f.findViewById(R.id.dialogDoubleEntry);
                        ((PositiveListener) getActivity()).setDistance(Double.parseDouble(editText.getText().toString()));
                        break;
                    case 2:
                        editText = (EditText) f.findViewById(R.id.dialogIntEntry);
                        ((PositiveListener) getActivity()).setCalories(Integer.parseInt(editText.getText().toString()));
                        break;
                    case 3:
                        editText = (EditText) f.findViewById(R.id.dialogIntEntry);
                        ((PositiveListener) getActivity()).setHeartRate(Integer.parseInt(editText.getText().toString()));
                        break;
                    case 4:
                        editText = (EditText) f.findViewById(R.id.dialogCommentEntry);
                        ((PositiveListener) getActivity()).setComment(editText.getText().toString());
                        break;
                    default:
                        break;
                }
                DynamicDialog.this.getDialog().cancel();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                DynamicDialog.this.getDialog().cancel();
            }
        });

        return alertDialogBuilder.create();
    }

    public interface PositiveListener{
        public void setDuration(double value);
        public void setDistance(double value);
        public void setCalories(int value);
        public void setHeartRate(int value);
        public void setComment(String value);
    }

}