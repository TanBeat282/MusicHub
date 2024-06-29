package com.tandev.musichub.adapter.new_release_song;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tandev.musichub.fragment.new_release.album.NewReleaseAlbumFragment;
import com.tandev.musichub.fragment.new_release.song.NewReleaseSongFragment;


public class NewReleaseViewPageAdapter extends FragmentPagerAdapter {

    public NewReleaseViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new NewReleaseAlbumFragment();
            default:
                return new NewReleaseSongFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Bài hát";
                break;
            case 1:
                title = "Album";
                break;
        }
        return title;
    }

}
