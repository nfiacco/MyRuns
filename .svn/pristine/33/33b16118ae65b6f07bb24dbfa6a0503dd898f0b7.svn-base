package edu.dartmouth.cs.myruns;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProPicDialogFragment extends DialogFragment {

    public interface YesNoListener {
        void onYes();

        void onNo();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof YesNoListener)) {
            throw new ClassCastException(activity.toString() + " must implement YesNoListener");
        }
    }

    // basic dialog with yes/no button functions to be implemented
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] profile_select_options = new String[]{"Take from camera","Select from gallery"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.dialogTitle);
        alertDialogBuilder.setItems(profile_select_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int selected) {
                if (selected == 0){
                    ((YesNoListener) getActivity()).onYes();
                }
                else if (selected == 1){
                    ((YesNoListener) getActivity()).onNo();
                }
            }
        });

        return alertDialogBuilder.create();
    }
}