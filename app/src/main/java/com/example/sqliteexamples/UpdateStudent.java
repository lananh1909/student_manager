package com.example.sqliteexamples;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateStudent extends AppCompatActivity {
    EditText editMssv, editHoTen, editNgaySinh, editEmail, editDiacChi;
    DatePickerDialog picker;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);

        editNgaySinh = findViewById(R.id.edit_textbirthday);
        editNgaySinh.setInputType(InputType.TYPE_NULL);
        editNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(UpdateStudent.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                editNgaySinh.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        Bundle student = getIntent().getExtras();
        editMssv = findViewById(R.id.edit_textmssv);
        editHoTen = findViewById(R.id.edit_textname);
        editEmail = findViewById(R.id.edit_textemail);
        editDiacChi = findViewById(R.id.edit_textaddress);
        String date = student.getString("ngaysinh");
        SimpleDateFormat sdf3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        try{
            d1 = sdf3.parse(date);

        }catch (Exception e){ e.printStackTrace(); }

        editNgaySinh.setText(dateFormat.format(d1));

        editMssv.setText(student.getString("mssv"));
        editMssv.setEnabled(false);
        editHoTen.setText(student.getString("hoten"));
        editEmail.setText(student.getString("email"));
        editDiacChi.setText(student.getString("diachi"));

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editDiacChi.getText().toString().equals("") || editEmail.getText().toString().equals("") || editHoTen.getText().toString().equals("") || editMssv.getText().toString().equals("") || editNgaySinh.getText().toString().equals("")){
                    Toast toast = new Toast(UpdateStudent.this);
                    toast.setText("Bạn chưa điền đầy đủ thông tin!");
                    toast.show();
                } else {
                    String dataPath = getFilesDir() + "/student_data";
                    db = SQLiteDatabase.openDatabase(dataPath, null, SQLiteDatabase.OPEN_READWRITE);
                    String sql = String.format("update sinhvien set hoten = '%s', ngaysinh = '%s', email = '%s', diachi = '%s' where mssv = '%s'", editHoTen.getText(), editNgaySinh.getText(), editEmail.getText(), editDiacChi.getText(), editMssv.getText());
                    db.execSQL(sql);
                    AlertDialog dialog = new AlertDialog.Builder(UpdateStudent.this)
                            .setMessage("Sửa thành công! Bạn có muốn quay lại trang trước?")
                            .setTitle("Update student")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(UpdateStudent.this, StudentListActivity.class);
                                    startActivity(intent);
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
            }
        });

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateStudent.this, StudentListActivity.class);
                startActivity(intent);
            }
        });
    }
}