package jp.projectoffline.todo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

//入力時のfragment
public class AlertDialogFragment extends DialogFragment {
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final EditText editText = new EditText(getActivity());
        //入力制限 (1行, 256文字まで)
        InputFilter[] inputFilter = new InputFilter[1];
        inputFilter[0] = new InputFilter.LengthFilter(256);

        editText.setFilters(inputFilter);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(editText);
        alert.setTitle(R.string.input);

        alert.setPositiveButton(R.string.dialog_positive1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //入力した文字を取得
                String str = editText.getText().toString();
                //MainActivityの関数と連携
                MainActivity mainActivity = (MainActivity) getActivity();
                //空白であればデータベースに追加しない
                if (!str.equals("")) {
                    mainActivity.addToDo(str);
                }
            }
        });
        alert.setNeutralButton(R.string.dialog_neutral, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Cancel
            }
        });
        //ダイアログを表示
        return alert.create();
    }
}