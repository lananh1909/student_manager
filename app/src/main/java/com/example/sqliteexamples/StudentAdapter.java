package com.example.sqliteexamples;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StudentAdapter extends BaseAdapter implements View.OnCreateContextMenuListener {

    Cursor cs;

    public StudentAdapter(SQLiteDatabase db) {
        cs = db.rawQuery("select * from sinhvien", null);
    }

    @Override
    public int getCount() {
        return cs.getCount();
    }

    @Override
    public Object getItem(int i) {
        cs.moveToPosition(i);
        return cs;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item, viewGroup, false);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StudentDetail.class);
                Cursor item = (Cursor) getItem(i);
                Bundle bundle = new Bundle();
                bundle.putString("mssv", item.getString(0));
                bundle.putString("hoten", item.getString(1));
                bundle.putString("ngaysinh", item.getString(2));
                bundle.putString("email", item.getString(3));
                bundle.putString("diachi", item.getString(4));
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        TextView textMSSV = view.findViewById(R.id.text_mssv);
        TextView textHoten = view.findViewById(R.id.text_hoten);
        TextView textEmail = view.findViewById(R.id.text_email);

        cs.moveToPosition(i);

        textMSSV.setText(cs.getString(cs.getColumnIndex("mssv")));
        textHoten.setText(cs.getString(cs.getColumnIndex("hoten")));
        textEmail.setText(cs.getString(cs.getColumnIndex("email")));

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

    }
}
