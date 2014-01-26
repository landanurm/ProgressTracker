package com.landanurm.progress_tracker.ui.helpers.helper_to_test_fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.landanurm.progress_tracker.R;

/**
 * Created by Leonid on 25.01.14.
 */
public class CurrentTestedFragmentBuilder {

    private static class DummyFragmentBuilder implements FragmentBuilder {
        @Override
        public Fragment build() {
            Fragment dummyFragment = new Fragment() {
                @Override
                public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                    return inflater.inflate(R.layout.fragment_dummy, container, false);
                }
            };
            return dummyFragment;
        }
    }

    private static FragmentBuilder currentFragmentBuilder = new DummyFragmentBuilder();

    public static void setCurrentBuilder(FragmentBuilder fragmentBuilder) {
        if (fragmentBuilder == null) {
            throw new IllegalArgumentException("fragmentBuilder should be not null");
        }
        currentFragmentBuilder = fragmentBuilder;
    }

    public static Fragment build() {
        return currentFragmentBuilder.build();
    }
}
