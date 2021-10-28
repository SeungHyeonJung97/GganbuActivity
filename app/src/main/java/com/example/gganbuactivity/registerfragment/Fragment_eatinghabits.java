package com.example.gganbuactivity.registerfragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.gganbuactivity.R;
import com.example.gganbuactivity.RegisterSingleton;
import com.example.gganbuactivity.databinding.FragmentEatinghabitsBinding;
import com.example.gganbuactivity.databinding.FragmentPasswordBinding;

public class Fragment_eatinghabits extends Fragment {
    private FragmentEatinghabitsBinding mBinding;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentEatinghabitsBinding.inflate(inflater, container, false);
        context = container.getContext();
        View view = mBinding.getRoot();
        setSpinner(mBinding.spEatingHabits, R.array.eatingHabits_array);

        mBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eatingHabits = mBinding.spEatingHabits.getSelectedItem().toString();
                RegisterSingleton.getInstance().setEatingHabits(eatingHabits);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment_lifepattern fragment_lifepattern = new Fragment_lifepattern();
                transaction.replace(R.id.register_layout, fragment_lifepattern);
                transaction.commit();
            }
        });
        return view;
    }

    public void setSpinner(Spinner spinner, int array){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, array, R.layout.custom_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
