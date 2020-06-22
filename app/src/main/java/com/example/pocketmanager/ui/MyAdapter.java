package com.example.pocketmanager.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.data.SMS;

import org.jetbrains.annotations.NotNull;

import java.util.List;

// Adapter to connect row.xml with the recycler view
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    List<SMS> smsList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView smsType;
        public TextView smsAmt;
        public TextView smsDate;
        public ImageView typeImage;

        public ViewHolder(View v) {
            super(v);

            typeImage = (ImageView) v.findViewById(R.id.typeImage);
            smsType = (TextView) v.findViewById(R.id.smsType);
            smsAmt = (TextView) v.findViewById(R.id.smsAmt);
            smsDate = (TextView) v.findViewById(R.id.smsDate);
        }
    }

    public MyAdapter(List<SMS> smsList, Context context) {
        this.smsList = smsList;
        this.context = context;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String t = smsList.get(position).getMsgType();
        holder.smsType.setText(t);
        if (t.equals("Personal Expenses") || t.equals("Food") || t.equals("Transport")) {
            holder.smsAmt.setTextColor(Color.RED);
            holder.typeImage.setBackgroundResource(R.drawable.ic_action_arrow_top);

        } else {
            holder.smsAmt.setTextColor(Color.parseColor("#b79be344"));
            holder.typeImage.setBackgroundResource(R.drawable.ic_action_arrow_bottom);
        }
        holder.smsAmt.setText("Rs " + smsList.get(position).getMsgAmt());
        holder.smsDate.setText(smsList.get(position).getFormatDate());
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

}
