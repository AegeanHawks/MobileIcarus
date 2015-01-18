package gr.rambou.myicarus;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/*created using Android Studio (Beta) 0.8.14
www.101apps.co.za*/

public class AdapterGrades extends ArrayAdapter<Lesson> {

    /*//private ArrayList<PersonData> peopleDataSet;
    private ArrayList<Lesson> lessonsDataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewGrade;

        TextView textViewSemester;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.Grade_Title);
            this.textViewGrade = (TextView) itemView.findViewById(R.id.Grade_Value);
            this.textViewSemester = (TextView) itemView.findViewById(R.id.Grade_Semester);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.Lesson_Passed);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grade_layout, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewMark = holder.textViewGrade;
        TextView textViewSemester = holder.textViewSemester;
        ImageView imageView = holder.imageViewIcon;

        textViewTitle.setText(lessonsDataSet.get(listPosition).Get_Title());
        textViewMark.setText(String.valueOf(lessonsDataSet.get(listPosition).Get_Mark()));
        textViewSemester.setText(lessonsDataSet.get(listPosition).Get_Semester());
        switch(lessonsDataSet.get(listPosition).Get_Status()){
            case PASSED:
            {
                imageView.setImageResource(R.drawable.ic_success);
                break;
            }
            case FAILED:
            {
                imageView.setImageResource(R.drawable.ic_fail);
                break;
            }
            case NOT_GIVEN:
            {
                imageView.setImageResource(R.drawable.ic_fail);
                break;
            }
            case CANCELLED:
            {
                imageView.setImageResource(R.drawable.ic_success);
                break;
            }
        }
    }*/


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
