package com.example.trabalhofinapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.trabalhofinapp.R;
import com.example.trabalhofinapp.ui.gallery.GalleryFragment;
import com.example.trabalhofinapp.ui.slideshow.SlideshowFragment;

public class HomeFragment extends Fragment {
    private Button buttonCambio, buttonCripto;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        buttonCambio = root.findViewById(R.id.buttonCambio);
        buttonCripto = root.findViewById(R.id.buttonCripto);


        return root;
    }
}