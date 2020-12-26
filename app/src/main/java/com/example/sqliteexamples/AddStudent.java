package com.example.sqliteexamples;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddStudent extends AppCompatActivity {
    EditText editDate, editMssv, editHoTen, editEmail, editDiaChi;
    DatePickerDialog picker;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        editDate = findViewById(R.id.text_birthday);
        editDate.setInputType(InputType.TYPE_NULL);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddStudent.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                editDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        editMssv = findViewById(R.id.text_mssv);
        editHoTen = findViewById(R.id.text_name);
        editEmail = findViewById(R.id.text_email);
        editDiaChi = findViewById(R.id.text_address);
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editDiaChi.getText().toString().equals("") || editEmail.getText().toString().equals("") || editHoTen.getText().toString().equals("") || editMssv.getText().toString().equals("") || editDate.getText().toString().equals("")){
                    Toast toast = new Toast(AddStudent.this);
                    toast.setText("Bạn chưa điền đầy đủ thông tin!");
                    toast.show();
                } else {
                    String dataPath = getFilesDir() + "/student_data";
                    db = SQLiteDatabase.openDatabase(dataPath, null, SQLiteDatabase.OPEN_READWRITE);
                    String sql = String.format("insert into sinhvien(mssv, hoten, ngaysinh, email, diachi) " +
                            "values('%s', '%s', '%s', '%s', '%s')", editMssv.getText(), editHoTen.getText(), editDate.getText(), editEmail.getText(), editDiaChi.getText());
                    db.execSQL(sql);
                    Toast toast = new Toast(AddStudent.this);
                    toast.setText("Thêm thành công!");
                    toast.show();
                    editMssv.setText("");
                    editDate.setText("");
                    editDiaChi.setText("");
                    editEmail.setText("");
                    editHoTen.setText("");
                }
            }
        });

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStudent.this, StudentListActivity.class);
                startActivity(intent);
            }
        });
    }
}