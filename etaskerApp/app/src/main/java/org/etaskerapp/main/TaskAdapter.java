package org.etaskerapp.main;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.etaskerapp.R;
import org.etaskerapp.model.Task;
import org.etaskerapp.tool.TaskType;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

    TaskAdapter(Context c, List<Task> list) {
        super(c, R.layout.task_row, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.task_row, parent, false);

        Task task = getItem(position);
        TextView taskTitle = (TextView)v.findViewById(R.id.taskListTaskTitle);
        taskTitle.setText(task.getTitle());
        TextView taskAddress = (TextView)v.findViewById(R.id.taskListTaskAddress);
        taskAddress.setText(task.getObjectAddress());
        TextView taskStart = (TextView)v.findViewById(R.id.taskListTaskStart);
        taskStart.setText(task.getPlanned_time());
        TextView taskStatus = (TextView)v.findViewById(R.id.taskListTaskStatus);
        switch (task.getStatus()) {
            case 0:
                taskStatus.setText(TaskType.Unassigned.toString());
                taskStatus.setBackgroundColor(Color.GRAY);
                break;
            case 1:
                taskStatus.setText(TaskType.Assigned.toString());
                taskStatus.setBackgroundColor(Color.BLUE);
                break;
            case 2:
                taskStatus.setText(TaskType.InProgress.toString());
                taskStatus.setBackgroundColor(Color.parseColor("#0d3e11"));
                break;
            case 3:
                taskStatus.setText(TaskType.Done.toString());
                taskStatus.setBackgroundColor(Color.MAGENTA);
                break;
            case 4:
                taskStatus.setText(TaskType.Aborted.toString());
                taskStatus.setBackgroundColor(Color.RED);
                break;
            case 5:
                taskStatus.setText(TaskType.Stopped.toString());
                taskStatus.setBackgroundColor(Color.parseColor("#ab2a25"));
                break;
        }
        return v;
    }
}
