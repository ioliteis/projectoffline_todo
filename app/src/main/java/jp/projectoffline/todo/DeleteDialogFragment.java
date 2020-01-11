package jp.projectoffline.todo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

//削除時のfragment
public class DeleteDialogFragment extends DialogFragment {
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage(R.string.confirm);
        alert.setPositiveButton(R.string.dialog_positive2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //MainActivityの関数と連携
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.deleteToDo();
            }
        });
        alert.setNeutralButton(R.string.dialog_neutral, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        //ダイアログを表示
        return alert.create();
    }
}
