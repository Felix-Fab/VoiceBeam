package tk.felixfab.voicebeam.Adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import tk.felixfab.voicebeam.Adapter.Data.UserData;
import tk.felixfab.voicebeam.R;

public class UserAdapter implements ListAdapter {

    ArrayList<UserData> arrayList;
    Context context;
    public UserAdapter(Context context, ArrayList<UserData> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public UserData getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        UserData userData = arrayList.get(position);
        if(convertView == null) {
            holder = new ViewHolder();

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.audio_send_listview, null);

            holder.username = convertView.findViewById(R.id.audio_send_listview_textview_username);
            holder.details = convertView.findViewById(R.id.audio_send_listview_textview_details);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if(userData.Sended){
            holder.username.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.details.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.username.setTextColor(Color.BLUE);
        }else{
            holder.username.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.details.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.username.setTextColor(Color.RED);
        }

        holder.username.setText(userData.Text);
        holder.details.setText(userData.Details);

        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }


    protected static class ViewHolder{
        TextView username;
        TextView details;
    }
}
