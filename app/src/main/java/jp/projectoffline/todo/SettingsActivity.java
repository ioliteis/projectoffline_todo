package jp.projectoffline.todo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //設定の読み込み
        SharedPreferences data = getSharedPreferences("preference", MODE_PRIVATE);
        //ダークテーマ
        if (data.getBoolean("setting1", true)) {
            setTheme(R.style.AppTheme_Dark);
        }

        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //final SharedPreferences data = getSharedPreferences("preference", MODE_PRIVATE);
        final SharedPreferences.Editor editor = data.edit();
        final CheckBox checkBox1 = findViewById(R.id.checkBox1);

        //初期設定
        if (data.getBoolean("initial", true)) {
            editor.putBoolean("setting1", false);
            editor.putBoolean("initial", false);
            editor.apply();
        }
        //設定の値をチェックボックスに適用
        if (data.getBoolean("setting1", true)) {
            checkBox1.setChecked(true);
        }else {
            checkBox1.setChecked(false);
        }
        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //設定の書き込み
                if (checkBox1.isChecked()) {
                    editor.putBoolean("setting1", true);
                    editor.apply();
                }else {
                    editor.putBoolean("setting1", false);
                    editor.apply();
                }

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }
}
