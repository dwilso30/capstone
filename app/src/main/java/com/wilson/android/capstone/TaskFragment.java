package com.wilson.android.capstone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

public class TaskFragment extends Fragment {
    private Task mTask;
    private EditText mText;
    private Button mButton;
    private CheckBox mCheckBox;

    private static final String ARG_ID = "date_completed";
    private static final String DIA_DATE = "dia_date";
    private static final int REQ_DATE = 0;

    public static TaskFragment newInstance(UUID id){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_ID, id);

        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID id = (UUID) getArguments().getSerializable(ARG_ID);
        mTask = DailyTasks.get(getActivity()).getTask(id);
    }

    @Override
    public void onPause() {
        super.onPause();

        DailyTasks.get(getContext()).updateTask(mTask);
    }

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState) {
        View v = inflator.inflate(R.layout.fragment_planner, container, false);

        mText = (EditText) v.findViewById(R.id.task_title);
        mButton = (Button) v.findViewById(R.id.task_date);
        mCheckBox = (CheckBox) v.findViewById(R.id.task_completed);

        mText.setText(mTask.getTaskName());
        mText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTask.setmTaskName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mButton.setText(mTask.getDateCompleted().toString());
        mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager fm = getFragmentManager();
                DatePickerFragment fragment = DatePickerFragment.newInstance(mTask.getDateCompleted());
                fragment.setTargetFragment(TaskFragment.this, REQ_DATE);
                fragment.show(fm, DIA_DATE);
            }
        });

        mCheckBox.setChecked(mTask.getIsCompleted());
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTask.setIsCompleted(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK)
            return;

        if(requestCode == REQ_DATE){
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mTask.setDate(date);
            mButton.setText(date.toString());
        }
    }
}
