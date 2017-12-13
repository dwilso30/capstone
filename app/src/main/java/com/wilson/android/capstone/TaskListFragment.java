package com.wilson.android.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;


public class TaskListFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private TaskAdapter mTaskAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState){
        View v = inflator.inflate(R.layout.fragment_recycler_view, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.menu_item_task_list){
            Task t = new Task();
            DailyTasks.get(getActivity()).addTask(t);
            Intent intent = TaskPagerActivity.newIntent(getActivity(), t.getId());
            startActivity(intent);

            return true;
        }
        else
            return false;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        List<Task> dailyTasks  = DailyTasks.get(getContext()).getTasks();

        if(mTaskAdapter == null) {
            mTaskAdapter = new TaskAdapter(dailyTasks);
            mRecyclerView.setAdapter(mTaskAdapter);
        }
        else {
            mTaskAdapter.setTasks(dailyTasks);
            mTaskAdapter.notifyDataSetChanged();
        }
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Task mTask;

        private CheckBox mCheckBox;
        private TextView mTaskTitle;
        private TextView mTaskDate;


        public TaskHolder(View itemView){
            super(itemView);

            itemView.setOnClickListener(this);

            mCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_finished_box);
            mTaskTitle = (TextView) itemView.findViewById(R.id.list_item_task_title);
            mTaskDate = (TextView) itemView.findViewById(R.id.list_item_date);
        }

        public void onBindTask(Task task){
            mTask = task;

            mTaskTitle.setText(mTask.getTaskName());
            mTaskDate.setText(mTask.getDateCompleted().toString());

            mCheckBox.setChecked(mTask.getIsCompleted());
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mTask.setIsCompleted(isChecked);
                }
            });
        }

        @Override
        public void onClick(View view){
            Intent intent = TaskPagerActivity.newIntent(getContext(), mTask.getId());
            startActivity(intent);
        }

    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder>{
        private List<Task> tasks;

        public TaskAdapter(List<Task> tasks){
            this.tasks = tasks;
        }

        @Override
        public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.list_item_task, parent, false);

            return new TaskHolder(v);
        }

        @Override
        public void onBindViewHolder(TaskHolder holder, int position) {
            holder.onBindTask(tasks.get(position));
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        public void setTasks(List<Task> tasks){
            this.tasks = tasks;
        }
    }
}
