package com.example.sqliteexamples;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import io.bloco.faker.Faker;

public class StudentListActivity extends AppCompatActivity {

    SQLiteDatabase db;
    StudentAdapter adapter;
    StudentAdapter newAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        String dataPath = getFilesDir() + "/student_data";
        db = SQLiteDatabase.openDatabase(dataPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);

//        createRandomData();

        listView = findViewById(R.id.list_students);
        adapter = new StudentAdapter(db);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);
    }

    private void createRandomData() {
        db.beginTransaction();
        try {
            db.execSQL("create table sinhvien(" +
                    "mssv char(8) primary key," +
                    "hoten text," +
                    "ngaysinh date," +
                    "email text," +
                    "diachi text);");

            Faker faker = new Faker();
            for (int i = 0; i < 50; i++) {
                String mssv = "2017" + faker.number.number(4);
                String hoten = faker.name.name().replaceAll("'", "");
                String ngaysinh = faker.date.birthday(18, 22).toString();
                String email = faker.internet.email();
                String diachi = faker.address.city().replaceAll("'", "") + ", " + faker.address.country().replaceAll("'", "");

                String sql = String.format("insert into sinhvien(mssv, hoten, ngaysinh, email, diachi) " +
                        "values('%s', '%s', '%s', '%s', '%s')", mssv, hoten, ngaysinh, email, diachi);

                db.execSQL(sql);
            }

            db.setTransactionSuccessful();
            Log.v("TAG", "thanh cong");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                newAdapter = new StudentAdapter(db);
                String [] columns = {"mssv", "hoten", "ngaysinh", "email", "diachi"};
                String [] args = {query, query};
                String q = "mssv like '" + query + "%' or hoten like '%" + query + "%'";
                newAdapter.cs = db.query("sinhvien", columns,q, null, null, null, null);
                listView.setAdapter(newAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newAdapter = new StudentAdapter(db);
                String [] columns = {"mssv", "hoten", "ngaysinh", "email", "diachi"};
                String query = "mssv like '" + newText + "%' or hoten like '%" + newText + "%'";
                String [] args = {newText, newText};
                newAdapter.cs = db.query("sinhvien", columns, query, null, null, null, null);
                listView.setAdapter(newAdapter);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                listView.setAdapter(adapter);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 101, 0, "Update");
        menu.add(0, 102, 0, "Delete");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Cursor selectedItem = (Cursor) listView.getItemAtPosition(info.position);

        int id = item.getItemId();
        if (id == 101) {
            Intent intent = new Intent(StudentListActivity.this, UpdateStudent.class);
            Bundle bundle = new Bundle();
            bundle.putString("mssv", selectedItem.getString(0));
            bundle.putString("hoten", selectedItem.getString(1));
            bundle.putString("ngaysinh", selectedItem.getString(2));
            bundle.putString("email", selectedItem.getString(3));
            bundle.putString("diachi", selectedItem.getString(4));
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == 102) {
            AlertDialog dialog = new AlertDialog.Builder(StudentListActivity.this)
                        .setMessage("Are you sure to detete?")
                        .setTitle("Delete student")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String id = selectedItem.getString(0);
                                db.execSQL("delete from sinhvien where mssv = " + id);
                                adapter = new StudentAdapter(db);
                                listView.setAdapter(adapter);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .create();

                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_add){
            Intent intent = new Intent(StudentListActivity.this, AddStudent.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_delete){
            for(int i = 0; i < listView.getCount(); i++){
                ViewGroup v = (ViewGroup) listView.getChildAt(i);
                if(v != null){
                    String mssv = ((TextView) v.getChildAt(0)).getText().toString();
                    CheckBox check = (CheckBox) v.getChildAt(3);
                    if(check.isChecked()) {
                        db.execSQL("delete from sinhvien where mssv = " + mssv);
                    }
                }
            }
            adapter = new StudentAdapter(db);
            listView.setAdapter(adapter);
        }
        return super.onOptionsItemSelected(item);
    }
}