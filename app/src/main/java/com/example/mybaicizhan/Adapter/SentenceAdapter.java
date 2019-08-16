package com.example.mybaicizhan.Adapter;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mybaicizhan.Http.WordValue;
import com.example.mybaicizhan.R;

import org.w3c.dom.Text;

import java.util.List;

public class SentenceAdapter extends ArrayAdapter<WordValue.Sentence> {

    private int resourceId;

    public SentenceAdapter(Context context, int viewId, List<WordValue.Sentence> objects){
        super(context,viewId,objects);
        resourceId = viewId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        WordValue.Sentence sent = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.origTv = (TextView) view.findViewById(R.id.si_orig);
            viewHolder.transTv = (TextView) view.findViewById(R.id.si_trans);
            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.origTv.setText(sent.getOrig());
        viewHolder.transTv.setText(sent.getTrans());
        return view;
    }

    class ViewHolder{
        TextView origTv;
        TextView transTv;
    }

}
