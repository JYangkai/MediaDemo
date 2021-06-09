package com.yk.mediademo.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.yk.mediademo.R;
import com.yk.mediademo.data.bean.Function;

import java.util.List;

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.ViewHolder> {
    private static final String TAG = "FunctionAdapter";

    private Context context;

    private final List<Function> functionList;

    public FunctionAdapter(List<Function> functionList) {
        this.functionList = functionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_function, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Function function = functionList.get(position);
                Log.d(TAG, "onClick: function:" + function);
                context.startActivity(new Intent(context, function.getCls()));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Function function = functionList.get(position);
        holder.btnEnter.setText(function.getName());
    }

    @Override
    public int getItemCount() {
        return functionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatButton btnEnter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnEnter = itemView.findViewById(R.id.btnEnter);
        }
    }
}
