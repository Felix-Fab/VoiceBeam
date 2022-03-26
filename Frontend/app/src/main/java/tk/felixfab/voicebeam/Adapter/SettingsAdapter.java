package tk.felixfab.voicebeam.Adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tk.felixfab.voicebeam.Adapter.Data.SettingsData;
import tk.felixfab.voicebeam.R;

public class SettingsAdapter implements ListAdapter {

    ArrayList<SettingsData> arrayList;
    Context context;

    public SettingsAdapter(Context context,ArrayList<SettingsData> arrayList){
        this.arrayList = arrayList;
        this.context = context;
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
    public Object getItem(int position) {
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

        SettingsData settingsData = arrayList.get(position);
        if(convertView == null){
            holder = new ViewHolder();

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.settings_menu_listview,null);

            holder.title = convertView.findViewById(R.id.settings_menu_listview_title);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(settingsData.Text);
        holder.title.setTextColor(Color.parseColor(settingsData.Color));

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
        TextView title;
    }
}
