package com.fct.neec.oficial.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fct.neec.oficial.Fragments.ClassesFragment;
import com.fct.neec.oficial.R;

public class ClassListViewAdapter extends ArrayAdapter<Object> {
    private Context mContext;

    public ClassListViewAdapter(Context context) {
        super(context, 0);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_class, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.class_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ClassesFragment.ListViewItem item = (ClassesFragment.ListViewItem) getItem(position);
        viewHolder.name.setText(item.name);

        return convertView;
    }

    static class ViewHolder {

        TextView name;
    }

}