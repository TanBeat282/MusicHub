package com.tandev.musichub.adapter.search.search_multi;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tandev.musichub.fragment.search.search_multi.AllSeachMultiFragment;
import com.tandev.musichub.fragment.search.search_multi.ArtistSearchMultiFragment;
import com.tandev.musichub.fragment.search.search_multi.MvSearchMultiFragment;
import com.tandev.musichub.fragment.search.search_multi.PlaylistAlbumSearchMultiFragment;
import com.tandev.musichub.fragment.search.search_multi.SongSearchMultiFragment;


public class SearchMultiViewPageAdapter extends FragmentPagerAdapter {

    private String query;

    public SearchMultiViewPageAdapter(@NonNull FragmentManager fm, int behavior, String query) {
        super(fm, behavior);
        this.query = query;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AllSeachMultiFragment.newInstance(query);
            case 1:
                return SongSearchMultiFragment.newInstance(query);
            case 2:
                return PlaylistAlbumSearchMultiFragment.newInstance(query);
            case 3:
                return ArtistSearchMultiFragment.newInstance(query);
            case 4:
                return MvSearchMultiFragment.newInstance(query);
            default:
                return AllSeachMultiFragment.newInstance(query);
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Tất cả";
                break;
            case 1:
                title = "Bài hát";
                break;
            case 2:
                title = "Playlist";
                break;
            case 3:
                title = "Nghệ sĩ";
                break;
            case 4:
                title = "MV";
                break;
        }
        return title;
    }
}
