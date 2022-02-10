package com.example.userpass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class List_Dialog extends AppCompatDialogFragment {
    private EditText editList;
    private EDialog listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.activity_list_dialog,null);
        builder.setView(view).setTitle("Name")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String L_Name=editList.getText().toString();
                listener.applyText(L_Name);
            }
        });
        editList=view.findViewById(R.id.list_add);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener=(EDialog) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement example dialogue listener");
        }
    }

    public interface EDialog{
        void applyText(String L_Name);
    }
}
