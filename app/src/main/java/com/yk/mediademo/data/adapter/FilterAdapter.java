package com.yk.mediademo.data.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.mediademo.R;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private Context context;

    private final List<BaseRenderBean> filterList;

    private int curClickIndex = -1;

    public FilterAdapter(List<BaseRenderBean> filterList) {
        this.filterList = filterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_filter, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                BaseRenderBean filter = filterList.get(position);
                if (onClickFilterListener != null) {
                    onClickFilterListener.onClickFilter(filter);
                }
                updateSelect(position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaseRenderBean filter = filterList.get(position);
        holder.tvName.setText(filter.getName());
        if (position == curClickIndex) {
            holder.tvName.setTextColor(Color.RED);
        } else {
            holder.tvName.setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    private void updateSelect(int position) {
        if (curClickIndex == -1) {
            curClickIndex = position;
            notifyItemChanged(position);
            return;
        }

        int lastClickIndex = curClickIndex;
        curClickIndex = position;
        notifyItemChanged(lastClickIndex);
        notifyItemChanged(curClickIndex);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    private OnClickFilterListener onClickFilterListener;

    public void setOnClickFilterListener(OnClickFilterListener onClickFilterListener) {
        this.onClickFilterListener = onClickFilterListener;
    }

    public interface OnClickFilterListener {
        void onClickFilter(BaseRenderBean filter);
    }
}
