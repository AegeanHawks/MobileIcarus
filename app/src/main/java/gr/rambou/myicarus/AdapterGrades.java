package gr.rambou.myicarus;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterGrades extends ArrayAdapter<Lesson> {

    Context context;

    public AdapterGrades(Context context, int resourceId, //resourceId=your layout
                                   List<Lesson> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView textViewTitle ;
        TextView textViewMark;
        TextView textViewSemester;
        ImageView imageView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Lesson rowItem = getItem(position);

        //LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater mInflater = new LayoutInflater(context) {
            @Override
            public LayoutInflater cloneInContext(Context newContext) {
                return null;
            }
        };
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grade_layout, null);
            holder = new ViewHolder();
            holder.textViewTitle = (TextView) convertView.findViewById(R.id.Grade_Title);
            holder.textViewMark = (TextView) convertView.findViewById(R.id.Grade_Value);
            holder.textViewSemester = (TextView) convertView.findViewById(R.id.Grade_Semester);
            holder.imageView = (ImageView) convertView.findViewById(R.id.Lesson_Passed);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.textViewTitle.setText(rowItem.Get_Title());
        holder.textViewMark.setText(String.valueOf(rowItem.Get_Mark()));
        holder.textViewSemester.setText(rowItem.Get_Semester()+"ο Εξάμηνο");
        switch(rowItem.Get_Status()){
            case PASSED:
            {
                holder.imageView.setImageResource(R.drawable.ic_success);
                break;
            }
            case FAILED:
            {
                holder.imageView.setImageResource(R.drawable.ic_fail);
                break;
            }
            case NOT_GIVEN:
            {
                holder.imageView.setImageResource(R.drawable.ic_fail);
                break;
            }
            case CANCELLED:
            {
                holder.imageView.setImageResource(R.drawable.ic_success);
                break;
            }
        }

        return convertView;
    }
}
