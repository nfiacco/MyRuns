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
import android.widget.RadioGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class UnitFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Unit Preference:");

        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.unit_dialog_layout, null);

        RadioGroup unitGroup = (RadioGroup) view.findViewById(R.id.unitGroup);

        unitGroup.check(settings.getInt("unit_checked_key", 0));

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = settings.edit();

                RadioGroup unitGroup = (RadioGroup) view.findViewById(R.id.unitGroup);

                editor.putInt("unit_checked_key", unitGroup.getCheckedRadioButtonId());
                if (unitGroup.getCheckedRadioButtonId() == R.id.metric_check){
                    editor.putInt("unit_type", 0);
                }
                else if (unitGroup.getCheckedRadioButtonId() == R.id.american_check){
                    editor.putInt("unit_type", 1);
                }
                editor.commit();
                ((DismissListener) getActivity()).setUnits();
                UnitFragment.this.getDialog().cancel();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                UnitFragment.this.getDialog().cancel();
            }
        });

        return alertDialogBuilder.create();

    }

    public interface DismissListener {
        public void setUnits();
    }

}