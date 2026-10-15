package com.sayuti.deeplinknavigationlab;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnOpenProduct)
                .setOnClickListener(v -> {
                    Bundle args = new Bundle();
                    args.putString("productId", "111");
                    args.putString("source", "in-app");

                    Navigation.findNavController(v)
                            .navigate(
                                    R.id.action_homeFragment_to_productFragment,
                                    args
                            );
                });

        Button btnNotification =
                view.findViewById(R.id.btnProductNotification);

        btnNotification.setOnClickListener(v ->
                ((MainActivity) requireActivity())
                        .requestPermissionAndShowNotification()
        );
    }
}
