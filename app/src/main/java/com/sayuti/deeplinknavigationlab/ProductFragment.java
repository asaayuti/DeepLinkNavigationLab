package com.sayuti.deeplinknavigationlab;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

public class ProductFragment extends Fragment {

    public ProductFragment() {
        super(R.layout.fragment_product);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();

        String productId = args != null
                ? args.getString("productId")
                : null;

        String source = args != null
                ? args.getString("source")
                : "unknown";

        if (source == null) {
            source = "unknown";
        }

        String[] allowedSources = {
                "browser",
                "notification",
                "promo",
                "custom-scheme",
                "in-app",
                "unknown"
        };

        if (!java.util.Arrays.asList(allowedSources).contains(source)) {
            Log.w("DeepLinkLab", "Unknown source: " + source);
            source = "unknown";
        }

        if (productId == null || !productId.matches("\\d+")) {
            Log.e("DeepLinkLab", "Invalid productId: " + productId);

            Toast.makeText(
                    requireContext(),
                    "ID produk tidak valid",
                    Toast.LENGTH_SHORT
            ).show();

            NavHostFragment.findNavController(this)
                    .popBackStack(R.id.homeFragment, false);

            return;
        }

        Log.d("DeepLinkLab", "Product ID: " + productId);
        Log.d("DeepLinkLab", "Source: " + source);

        TextView tvProductId = view.findViewById(R.id.tvProductId);
        TextView tvSource = view.findViewById(R.id.tvSource);

        tvProductId.setText("Product ID: " + productId);
        tvSource.setText("Source: " + source);

        view.findViewById(R.id.btnBackHome)
                .setOnClickListener(v ->
                        Navigation.findNavController(v).navigateUp()
                );

        Button btnSecondProduct =
                view.findViewById(R.id.btnOpenSecondProduct);

        btnSecondProduct.setOnClickListener(v -> {
            Bundle productArgs = new Bundle();
            productArgs.putString("productId", "222");
            productArgs.putString("source", "in-app");

            Navigation.findNavController(v).navigate(
                    R.id.action_productFragment_self,
                    productArgs,
                    new NavOptions.Builder()
//                            .setLaunchSingleTop(true)
                            .setPopUpTo(R.id.homeFragment, true)
                            .build()
            );
        });
    }
}
