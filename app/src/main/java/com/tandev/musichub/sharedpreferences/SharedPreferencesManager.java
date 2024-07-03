package com.tandev.musichub.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tandev.musichub.model.search.search_recommend.DataSearchRecommend;
import com.tandev.musichub.model.search.search_recommend.SearchRecommend;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreferencesManager {
    private final Context context;

    public SharedPreferencesManager(Context context) {
        this.context = context;
    }

    public void saveSongState(Items song) {
        if (song != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("music_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            Gson gson = new Gson();
            String jsonSong = gson.toJson(song);

            editor.putString("song", jsonSong);
            editor.apply();
        }
    }

    public Items restoreSongState() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_prefs", Context.MODE_PRIVATE);
        String jsonSong = sharedPreferences.getString("song", null);

        if (jsonSong != null) {
            Gson gson = new Gson();
            return gson.fromJson(jsonSong, Items.class);
        } else {
            return null;
        }
    }

    public void deleteSongState() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }

    public void saveSongArrayList(ArrayList<Items> songArrayList) {
        Gson gson = new Gson();
        String songListJson = gson.toJson(songArrayList);

        SharedPreferences sharedPreferences = context.getSharedPreferences("songList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("song_list", songListJson);
        editor.apply();
    }

    public ArrayList<Items> restoreSongArrayList() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences("songList", Context.MODE_PRIVATE);
        String song_list = sharedPreferences.getString("song_list", "");

        ArrayList<Items> restoreSongList = gson.fromJson(song_list, new TypeToken<ArrayList<Items>>() {
        }.getType());
        return new ArrayList<Items>(restoreSongList);
    }

    //history song
    public ArrayList<Items> restoreSongArrayListHistory() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences("songListHistory", Context.MODE_PRIVATE);
        String song_list = sharedPreferences.getString("song_list_history", "");

        ArrayList<Items> restoreSongList = gson.fromJson(song_list, new TypeToken<ArrayList<Items>>() {
        }.getType());
        if (restoreSongList == null) {
            restoreSongList = new ArrayList<>();
        }
        return restoreSongList;
    }


    public void saveSongArrayListHistory(Items song) {
        ArrayList<Items> songArrayList = restoreSongArrayListHistory();
        if (songArrayList == null) {
            songArrayList = new ArrayList<>();
        }

        // Tìm xem bài hát đã có trong danh sách lịch sử chưa
        boolean found = false;
        for (Items item : songArrayList) {
            if (item.getEncodeId().equals(song.getEncodeId())) {
                // Nếu tìm thấy, tăng historyCount lên 1
                item.setHistoryCount(item.getHistoryCount() + 1);
                found = true;
                break;
            }
        }

        // Nếu không tìm thấy, thêm bài hát mới vào danh sách lịch sử
        if (!found) {
            song.setHistoryCount(1); // Khởi tạo historyCount với giá trị 1 cho bài hát mới
            songArrayList.add(0, song); // Thêm bài hát vào đầu danh sách
        }

        // Lưu danh sách lịch sử đã cập nhật vào SharedPreferences
        Gson gson = new Gson();
        String songListJson = gson.toJson(songArrayList);

        SharedPreferences sharedPreferences = context.getSharedPreferences("songListHistory", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("song_list_history", songListJson);
        editor.apply();
    }

    // save data search
    public void saveSearchHistory(String keyword) {
        // Lấy danh sách lịch sử tìm kiếm hiện tại
        ArrayList<DataSearchRecommend> searchHistory = restoreSearchHistory();

        // Kiểm tra nếu danh sách lịch sử tìm kiếm là null
        if (searchHistory == null) {
            searchHistory = new ArrayList<>();
        }

        // Tạo đối tượng DataSearchRecommend mới
        DataSearchRecommend newSearch = new DataSearchRecommend(keyword, "");

        // Kiểm tra nếu từ khóa đã có trong danh sách
        if (searchHistory.contains(newSearch)) {
            // Di chuyển từ khóa lên đầu danh sách
            searchHistory.remove(newSearch);
        }

        // Thêm từ khóa tìm kiếm mới vào đầu danh sách
        searchHistory.add(0, newSearch);

        // Giới hạn số lượng mục trong danh sách lịch sử tìm kiếm (nếu cần)
        int maxSearchHistorySize = 20; // Số lượng tối đa mục trong danh sách lịch sử tìm kiếm
        if (searchHistory.size() > maxSearchHistorySize) {
            searchHistory.subList(maxSearchHistorySize, searchHistory.size()).clear();
        }

        // Lưu danh sách lịch sử tìm kiếm đã cập nhật vào SharedPreferences
        Gson gson = new Gson();
        String searchHistoryJson = gson.toJson(searchHistory);

        SharedPreferences sharedPreferences = context.getSharedPreferences("searchHistory", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("search_history", searchHistoryJson);
        editor.apply();
    }


    // restoreSearchHistory function
    public ArrayList<DataSearchRecommend> restoreSearchHistory() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("searchHistory", Context.MODE_PRIVATE);
        String searchHistoryJson = sharedPreferences.getString("search_history", null);

        if (searchHistoryJson != null) {
            Gson gson = new Gson();
            try {
                Type type = new TypeToken<ArrayList<DataSearchRecommend>>() {}.getType();
                return gson.fromJson(searchHistoryJson, type);
            } catch (JsonSyntaxException e) {
                // Log the error
                Log.e("SharedPreferencesManager", "Failed to parse search history JSON", e);
                // Optionally clear the corrupted JSON
                sharedPreferences.edit().remove("search_history").apply();
            }
        }
        return new ArrayList<>();
    }
    //delete data search
    public void deleteSearchHistory() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("searchHistory", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }



    //position song
    public void saveSongPosition(int positionSong) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("songPosition", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("position_song", positionSong);
        editor.apply();
    }

    public int restoreSongPosition() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("songPosition", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("position_song", 0);
    }

    public void saveActionState(int action) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_action", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("action", action).apply();
    }

    public int restoreActionState() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_action", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("action", 0);
    }

    public void saveIsPlayState(boolean is_play) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_is_play", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_play", is_play).apply();
    }

    public boolean restoreIsPlayState() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_is_play", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("is_play", false);
    }


    public void saveIsShuffleState(boolean is_shuffle) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_is_shuffle", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_shuffle", is_shuffle).apply();
    }

    public boolean restoreIsShuffleState() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_is_shuffle", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("is_shuffle", false);
    }

    public void saveIsRepeatState(boolean is_repeat) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_is_repeat", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_repeat", is_repeat).apply();
    }

    public boolean restoreIsRepeatState() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_is_repeat", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("is_repeat", false);
    }

    public void saveIsRepeatOneState(boolean is_repeat_one) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_is_repeat_one", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_repeat_one", is_repeat_one).apply();
    }

    public boolean restoreIsRepeatOneState() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_is_repeat_one", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("is_repeat_one", false);
    }

    public void saveIsNavigationTransparentState(boolean is_navigation_transparent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_is_navigation_transparent", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_navigation_transparent", is_navigation_transparent).apply();
    }

    public boolean restoreIsNavigationTransparentState() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_is_navigation_transparent", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("is_navigation_transparent", false);
    }

    public void saveIsStatusBarGrayState(boolean is_status_bar_gray) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_is_status_bar_gray", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_status_bar_gray", is_status_bar_gray).apply();
    }

    public boolean restoreIsStatusBarGrayStateState() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_is_status_bar_gray", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("is_status_bar_gray", false);
    }


    public void saveColorBackgroundState(int color_background, int color_bottomsheet) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_color", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("color_background", color_background);
        editor.putInt("color_bottomsheet", color_bottomsheet).apply();
    }

    public int[] restoreColorBackgrounState() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("music_color", Context.MODE_PRIVATE);
        return new int[]{sharedPreferences.getInt("color_background", 0), sharedPreferences.getInt("color_bottomsheet", 0)};
    }


    // 0 low 1 high 2 lossless
    public void saveQualityAudioState(int quality_audio) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("quality_audio", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("quality_audio", quality_audio).apply();
    }

    public int restoreQualityAudioState() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("quality_audio", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("quality_audio", 0);
    }


    public void clearAll() {
        SharedPreferences musicPrefs = context.getSharedPreferences("music_prefs", Context.MODE_PRIVATE);
        SharedPreferences musicActionPrefs = context.getSharedPreferences("music_action", Context.MODE_PRIVATE);
        SharedPreferences musicIsPlayPrefs = context.getSharedPreferences("music_is_play", Context.MODE_PRIVATE);

        SharedPreferences.Editor musicPrefsEditor = musicPrefs.edit();
        SharedPreferences.Editor musicActionPrefsEditor = musicActionPrefs.edit();
        SharedPreferences.Editor musicIsPlayPrefsEditor = musicIsPlayPrefs.edit();

        musicPrefsEditor.clear().apply();
        musicActionPrefsEditor.clear().apply();
        musicIsPlayPrefsEditor.clear().apply();
    }

}
