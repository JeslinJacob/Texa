package com.texaconnect.texa.ui.adddevice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.texaconnect.texa.R;
import com.texaconnect.texa.model.SelectedWifiItem;

import java.util.List;

public class WifiListRecyclerViewAdapter extends RecyclerView.Adapter<WifiListRecyclerViewAdapter.ListViewHolder>{

    private List<SelectedWifiItem> mSelectedWifiItem;
    private Context mContext;
    private View mView;

    public WifiListRecyclerViewAdapter(List<SelectedWifiItem> mSelectedWifiItem, Context mContext) {
        this.mSelectedWifiItem = mSelectedWifiItem;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public WifiListRecyclerViewAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        mView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wifi_list_item,viewGroup,false);
        ListViewHolder holder = new ListViewHolder(mView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WifiListRecyclerViewAdapter.ListViewHolder holder, int position) {

        holder.ssidTxt.setText(mSelectedWifiItem.get(position).getSSID());
        holder.securityTxt.setText(mSelectedWifiItem.get(position).getSecurityType());
    }

    @Override
    public int getItemCount() {
        return mSelectedWifiItem.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView ssidTxt,securityTxt;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            ssidTxt=itemView.findViewById(R.id.ssid);
            securityTxt=itemView.findViewById(R.id.security_type);
        }
    }
}
