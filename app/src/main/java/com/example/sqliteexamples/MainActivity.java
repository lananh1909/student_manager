package com.example.sqliteexamples;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import io.bloco.faker.Faker;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String dataPath = getFilesDir() + "/mydata";
        db = SQLiteDatabase.openDatabase(dataPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);

        findViewById(R.id.btn_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.beginTransaction();
                try {
                    // Thuc hien cac cau truy van
                    // Tao bang
                    db.execSQL("create table tblAMIGO(" +
                            "recID integer primary key autoincrement," +
                            "name text," +
                            "phone text);");

                    // Them ban ghi moi
                    db.execSQL("insert into tblAMIGO(name, phone) values ('AAA', '555-1111')");
                    db.execSQL("insert into tblAMIGO(name, phone) values ('BBB', '555-2222')");
                    db.execSQL("insert into tblAMIGO(name, phone) values ('CCC', '555-3333')");

                    db.setTransactionSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        });

        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.beginTransaction();
                try {
//                    // Them ban ghi moi
//                    Faker faker = new Faker();
//                    String name = faker.name.name();
//                    String phone = "555-" + faker.number.number(4);
//                    String sql = "insert into tblAMIGO(name, phone) values ('" + name + "', '" + phone + "')";
//                    db.execSQL(sql);

                    ContentValues cv = new ContentValues();
                    cv.put("name", "ABC");
                    cv.put("phone", "555-1010");
                    long r = db.insert("tblAMIGO", null, cv);
                    Log.v("TAG", "Result: " + r);

                    cv.put("name", "DEF");
                    cv.put("phone", "555-2020");
                    r = db.insert("tblAMIGO", null, cv);
                    Log.v("TAG", "Result: " + r);

                    cv.clear();
                    r = db.insert("tblAMIGO", null, cv);
                    Log.v("TAG", "Result: " + r);

                    r = db.insert("tblAMIGO", "name", cv);
                    Log.v("TAG", "Result: " + r);

                    db.setTransactionSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        });

        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.beginTransaction();
                try {
//                    String sql = "delete from tblAMIGO where recID = '1'";
//                    db.execSQL(sql);

                    int r = db.delete("tblAMIGO", "recID > 2 and recID < 7", null);
                    Log.v("TAG", "Result: "+ r);

                    db.setTransactionSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.beginTransaction();
                try {
//                    String sql = "update tblAMIGO set name='ABC', phone='555-1234' where recID='2'";
//                    db.execSQL(sql);

                    ContentValues cv = new ContentValues();
                    cv.put("name", "Maria");

                    int r = db.update("tblAMIGO", cv,
                            "recID > 2 and recID < 7", null);
                    Log.v("TAG", "Result: " + r);

                    db.setTransactionSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        });

        findViewById(R.id.btn_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Raw query
//                String sql = "select * from tblAMIGO";
//                Cursor cs = db.rawQuery(sql, null);

                // Simple query
                String[] columns = {"recID", "name", "phone"};
                Cursor cs = db.query("tblAMIGO", columns,
                        null, null, null, null, null);

                Log.v("TAG", "# records: " + cs.getCount());
                cs.moveToFirst();
                do {
                    int recID = cs.getInt(0);
                    String name = cs.getString(1);
                    String phone = cs.getString(cs.getColumnIndex("phone"));

                    Log.v("TAG", recID + " - " + name + " - " + phone);
                } while (cs.moveToNext());
            }
        });
    }

    @Override
    protected void onStop() {
        db.close();
        super.onStop();
    }
}