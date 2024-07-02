package com.azhar.geminiai.adapter;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.azhar.geminiai.fragment.FragmentGenerateText;
import com.azhar.geminiai.fragment.FragmentImageRecognition;

/*
 * Created by Azhar Rivaldi on 12-06-2024
 * Youtube Channel : https://bit.ly/2PJMowZ
 * Github : https://github.com/AzharRivaldi
 * Twitter : https://twitter.com/azharrvldi_
 * Instagram : https://www.instagram.com/azhardvls_
 * LinkedIn : https://www.linkedin.com/in/azhar-rivaldi
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FragmentGenerateText();
                break;
            case 1:
                fragment = new FragmentImageRecognition();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String strTitle = "";
        switch (position) {
            case 0:
                strTitle = "Generate Text";
                break;
            case 1:
                strTitle = "Generate Image";
                break;
        }
        return strTitle;
    }

}
