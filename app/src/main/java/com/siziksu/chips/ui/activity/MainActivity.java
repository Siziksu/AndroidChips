package com.siziksu.chips.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.MultiAutoCompleteTextView;

import com.siziksu.chips.R;
import com.siziksu.chips.ui.manager.MultiTextManager;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.multiText)
    MultiAutoCompleteTextView multiText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        List<String> entries = Arrays.asList("Matt Damon",
                                             "Nicolas Cage",
                                             "Brad Pitt",
                                             "Johnny Depp",
                                             "George Clooney",
                                             "Bruce Willis",
                                             "Tom Cruise",
                                             "Will Smith",
                                             "Leonardo DiCaprio");
        MultiTextManager manager = new MultiTextManager(this, multiText);
        manager.init();
        manager.setEntries(entries);
    }
}
