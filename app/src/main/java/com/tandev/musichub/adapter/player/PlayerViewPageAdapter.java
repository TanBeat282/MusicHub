package com.tandev.musichub.adapter.player;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tandev.musichub.fragment.new_release.album.NewReleaseAlbumFragment;
import com.tandev.musichub.fragment.new_release.song.NewReleaseSongFragment;
import com.tandev.musichub.fragment.player.ContinueFragment;
import com.tandev.musichub.fragment.player.LyricFragment;
import com.tandev.musichub.fragment.player.RelateFragment;


public class PlayerViewPageAdapter extends FragmentStateAdapter {

    public PlayerViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ContinueFragment();
            case 1:
                return new LyricFragment();
            case 2:
                return new RelateFragment();
            default:
                return new LyricFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
