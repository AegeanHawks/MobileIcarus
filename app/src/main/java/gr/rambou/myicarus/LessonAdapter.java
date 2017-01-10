package gr.rambou.myicarus;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by rambou on 10/1/2017.
 */

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {
    private Object[] lessonDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public LessonAdapter(Object[] myDataset) {
        lessonDataset = myDataset;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cv;
        TextView textViewTitle;
        TextView textViewMark;
        TextView textViewSemester;
        ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            textViewTitle = (TextView) itemView.findViewById(R.id.Grade_Title);
            textViewMark = (TextView) itemView.findViewById(R.id.Grade_Value);
            textViewSemester = (TextView) itemView.findViewById(R.id.Grade_Semester);
            imageView = (ImageView) itemView.findViewById(R.id.Lesson_Passed);
        }

    }

    // Create new views (invoked by the layout manager)
    @Override
    public LessonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grade_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Lesson rowItem = (Lesson) lessonDataset[position];

        holder.textViewTitle.setText(rowItem.Get_Title());
        holder.textViewMark.setText(String.valueOf(rowItem.Get_Mark()));
        holder.textViewSemester.setText(rowItem.Get_Semester() + "ο Εξάμηνο");
        switch (rowItem.Get_Status()) {
            case PASSED: {
                holder.imageView.setImageResource(R.drawable.ic_success);
                break;
            }
            case FAILED: {
                holder.imageView.setImageResource(R.drawable.ic_fail);
                break;
            }
            case NOT_GIVEN: {
                holder.imageView.setImageResource(R.drawable.ic_uknown);
                break;
            }
            case CANCELLED: {
                holder.imageView.setImageResource(R.drawable.ic_uknown);
                break;
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return lessonDataset.length;
    }
}

