package jp.projectoffline.todo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private DialogFragment dialogFragment;
    private FragmentManager fragmentManager;
    private ArrayList<String> arrayItems;
    //データベースの利用
    private MyDBHelper helper = new MyDBHelper(this);
    //選択した要素を取得する変数
    private String item;

    private boolean theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Dark theme
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", true)){
            setTheme(R.style.AppTheme_Dark);
            theme = true;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //データベースの読み書き属性
        final SQLiteDatabase db = helper.getWritableDatabase();
        //arrayItemsに追加された要素をリストで表示
        arrayItems = new ArrayList<>();
        final ArrayAdapter<String> adapter;
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", true)){
            adapter = new ArrayAdapter<>(this, R.layout.text_color, arrayItems);
        } else {
              adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayItems);
        }
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        //要素の削除
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                //ダイアログの表示
                fragmentManager = getSupportFragmentManager();
                dialogFragment = new DeleteDialogFragment();
                dialogFragment.show(fragmentManager, "delete dialog");
                //選択した要素を取得
                item = (String)parent.getItemAtPosition(position);
            }
        });

        //ToDoの入力
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ダイアログの表示
                fragmentManager = getSupportFragmentManager();
                dialogFragment = new AlertDialogFragment();
                dialogFragment.show(fragmentManager, "input dialog");

                //リストを初期化して更新
                arrayItems.clear();
                adapter.notifyDataSetChanged();
                //カーソル
                Cursor c = db.query("todo_items", new String[] {"items"}, null,
                        null, null, null, null);
                //データベースの最初の行から最後の行までをarrayItemsに追加
                boolean end = c.moveToFirst();
                while (end) {
                    arrayItems.add(String.format("%s", c.getString(0)));
                    end = c.moveToNext();
                }
                //リストの更新
                adapter.notifyDataSetChanged();
                c.close();

            }
        });

        //アプリ開始時にデータベースを読み取りリストに表示
        arrayItems.clear();
        adapter.notifyDataSetChanged();

        Cursor c = db.query("todo_items", new String[] {"items"}, null,
                null, null, null, null);

        boolean end = c.moveToFirst();
        while (end) {
            arrayItems.add(String.format("%s", c.getString(0)));
            end = c.moveToNext();
        }

        adapter.notifyDataSetChanged();
        c.close();
    }
    //要素の削除
    public void deleteToDo(){
        final SQLiteDatabase db = helper.getWritableDatabase();

        arrayItems = new ArrayList<>();
        ArrayAdapter<String> adapter;
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", true)){
            adapter = new ArrayAdapter<>(this, R.layout.text_color, arrayItems);
        } else {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayItems);
        }
        listView.setAdapter(adapter);

        arrayItems.clear();
        adapter.notifyDataSetChanged();
        //データベース内の選択した要素を削除
        db.delete("todo_items", "items = ?" , new String[]{item});
        //リストを更新して表示
        Cursor c = db.query("todo_items", new String[]{"items"}, null,
                null, null, null, null);

        boolean end = c.moveToFirst();
        while (end) {
            arrayItems.add(String.format("%s", c.getString(0)));
            end = c.moveToNext();
        }

        adapter.notifyDataSetChanged();
        c.close();
    }
    //入力した文字をデータベースに追加
    public void addToDo(String text) {
        final SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("items", text);
        //データベースに値を追加
        db.insert("todo_items", null, values);

        arrayItems = new ArrayList<>();
        ArrayAdapter<String> adapter;
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", true)){
            adapter = new ArrayAdapter<>(this, R.layout.text_color, arrayItems);
        } else {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayItems);
        }
        listView.setAdapter(adapter);
        //リストを初期化して更新
        arrayItems.clear();
        adapter.notifyDataSetChanged();

        Cursor c = db.query("todo_items", new String[] {"items"}, null,
                null, null, null, null);
        boolean end = c.moveToFirst();
        while (end) {
            arrayItems.add(String.format("%s", c.getString(0)));
            end = c.moveToNext();
        }

        adapter.notifyDataSetChanged();
        c.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", true) && !theme) || (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", true) && theme)){
            overridePendingTransition(0, 0);
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        final SQLiteDatabase db = helper.getWritableDatabase();
        //データベースを閉じる
        db.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //intent
            Intent intent = new android.content.Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_about) {
            //intent
            Intent intent = new android.content.Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}