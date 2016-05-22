package pranesh.realtytask;

/**
 * Created by MOHIT on 29-Apr-16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PetitionListAdapter extends BaseAdapter{
    String [] petition_addressed_to;
    String [] petition_title;
    String [] petition_desc;
    String [] petition_started_by;
    String [] petition_supporters;
    Context context;
    private static LayoutInflater inflater=null;
    public PetitionListAdapter(Activity activity, String[] addressed_to, String[] title, String[] desc, String[] started_by, String[] supporters) {
        // TODO Auto-generated constructor stub
        petition_addressed_to = addressed_to;
        petition_title = title;
        petition_desc = desc;
        petition_started_by = started_by;
        petition_supporters = supporters;
        context=activity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return petition_addressed_to.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView textView_petition_addressed_to;
        TextView textView_petition_title;
        TextView textView_petition_desc;
        TextView textView_petition_started_by;
        TextView textView_petition_supporters;
    }
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_petitions_item, null);
        holder.textView_petition_addressed_to = (TextView) rowView.findViewById(R.id.textview_petition_to);
        holder.textView_petition_title = (TextView) rowView.findViewById(R.id.textview_petition_title);
        holder.textView_petition_desc = (TextView) rowView.findViewById(R.id.textview_petition_desc);
        holder.textView_petition_started_by = (TextView) rowView.findViewById(R.id.textview_petition_started_by);

        holder.textView_petition_addressed_to.setText("Petition to "+petition_addressed_to[position]);
        holder.textView_petition_title.setText(petition_title[position]);
        holder.textView_petition_desc.setText(petition_desc[position]);
        holder.textView_petition_started_by.setText(petition_started_by[position]);

        return rowView;
    }

}