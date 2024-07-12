package com.tandev.musichub.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tandev.musichub.model.playlist.DataItems;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.model.search.search_recommend.DataSearchRecommend;

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

    public void updatePositionSongOfArrayList(Items item, int newPosition) {
        ArrayList<Items> arrayList = restoreSongArrayList();
        if (arrayList != null) {
            if (arrayList.contains(item)) {
                int oldPosition = arrayList.indexOf(item);
                if (oldPosition != -1 && oldPosition != newPosition) {
                    arrayList.remove(oldPosition);
                    arrayList.add(newPosition, item);
                    saveSongArrayList(arrayList);

                    if (item.getEncodeId().equals(restoreSongState().getEncodeId())) {
                        saveSongPosition(newPosition);
                    } else {
                        for (int i = 0; i < arrayList.size(); i++) {
                            if (arrayList.get(i).getEncodeId().equals(restoreSongState().getEncodeId())) {
                                saveSongPosition(i);
                                break;
                            }
                        }
                    }
                }
            }
        }
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

    public void addSongToEndOfArrayList(Items item) {
        ArrayList<Items> arrayList = restoreSongArrayList();
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }

        if (arrayList.contains(item)) {
            // Nếu bài hát đã có trong danh sách, di chuyển nó tới cuối danh sách
            int existingPosition = arrayList.indexOf(item);
            if (existingPosition != -1) {
                // Xóa bài hát khỏi vị trí hiện tại
                arrayList.remove(existingPosition);
                // Thêm bài hát vào cuối danh sách
                arrayList.add(item);
                Log.d("SharedPreferencesManager", "Moved song to the end of the list: " + new Gson().toJson(arrayList));
            }
        } else {
            // Nếu bài hát không có trong danh sách, thêm bài hát vào cuối danh sách
            arrayList.add(item);
            Log.d("SharedPreferencesManager", "Added new song to the end of the list: " + new Gson().toJson(arrayList));
        }

        saveSongArrayList(arrayList);
    }


    public void addArrayListItemsAfterCurrent(ArrayList<Items> newItems) {
        ArrayList<Items> arrayList = restoreSongArrayList();

        if (arrayList != null && !arrayList.isEmpty()) {
            int lastIndex = arrayList.size() - 1;
            arrayList.addAll(lastIndex + 1, newItems);

            saveSongArrayList(arrayList);
            Log.d("SharedPreferencesManager", "Added new items after the last item: " + new Gson().toJson(arrayList));
        } else {
            Log.d("SharedPreferencesManager", "ArrayList is null or empty");
        }
    }

    public void addSongAfterCurrentPlaying(Items newSong) {
        ArrayList<Items> arrayList = restoreSongArrayList();
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }

        Items currentPlayingSong = restoreSongState();
        if (currentPlayingSong != null) {
            int currentPlayingPosition = arrayList.indexOf(currentPlayingSong);
            if (currentPlayingPosition != -1) {
                if (arrayList.contains(newSong)) {
                    // Nếu bài hát đã có trong danh sách, di chuyển nó tới vị trí phát tiếp theo
                    int existingPosition = arrayList.indexOf(newSong);
                    if (existingPosition != -1 && existingPosition != currentPlayingPosition + 1) {
                        // Xóa bài hát khỏi vị trí hiện tại
                        arrayList.remove(existingPosition);
                        // Thêm bài hát vào vị trí phát tiếp theo
                        int insertPosition = currentPlayingPosition + 1;
                        if (insertPosition > arrayList.size()) {
                            insertPosition = arrayList.size();
                        }
                        arrayList.add(insertPosition, newSong);
                        Log.d("SharedPreferencesManager", "Moved song to position " + insertPosition + ": " + new Gson().toJson(arrayList));
                    }
                } else {
                    // Nếu bài hát không có trong danh sách, thêm bài hát vào vị trí phát tiếp theo
                    int insertPosition = currentPlayingPosition + 1;
                    if (insertPosition > arrayList.size()) {
                        insertPosition = arrayList.size();
                    }
                    arrayList.add(insertPosition, newSong);
                    Log.d("SharedPreferencesManager", "Added new song after current playing song: " + new Gson().toJson(arrayList));
                }
            } else {
                Log.d("SharedPreferencesManager", "Current playing song not found in arrayList");
            }
        } else {
            Log.d("SharedPreferencesManager", "No song is currently playing");
        }

        saveSongArrayList(arrayList);
    }


    public void removeSongFromList(Items item) {
        // Xóa bài hát khỏi danh sách và cập nhật SharedPreferences
        ArrayList<Items> arrayList = restoreSongArrayList();
        if (arrayList != null) {
            arrayList.remove(item);
            saveSongArrayList(arrayList);
            Log.d("SharedPreferencesManager", "Removed song: " + new Gson().toJson(arrayList));
        }
    }


    //
    //playlist_user
    private String generateUniqueEncodeId() {
        ArrayList<DataPlaylist> playlistUsers = restorePlaylistUser();
        int maxId = 0;

        // Lấy ID cao nhất hiện tại để sinh ID mới
        for (DataPlaylist dataPlaylist : playlistUsers) {
            String encodeId = dataPlaylist.getEncodeId();
            if (encodeId.startsWith("playlist")) {
                try {
                    int id = Integer.parseInt(encodeId.substring(8));  // Lấy phần số sau "playlist"
                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (NumberFormatException e) {
                    Log.e("SharedPreferencesManager", "Failed to parse encodeId: " + encodeId, e);
                }
            }
        }

        // Sinh ID mới
        String newEncodeId = "playlist" + String.format("%02d", maxId + 1);
        Log.e("SharedPreferencesManager", "Failed to parse encodeId: " + newEncodeId);
        return newEncodeId;
    }

    public void savePlaylistUser(DataPlaylist dataPlaylist) {
        ArrayList<DataPlaylist> dataPlaylistArrayList = restorePlaylistUser();

        // Khởi tạo danh sách nếu không có
        if (dataPlaylistArrayList == null) {
            dataPlaylistArrayList = new ArrayList<>();
        }

        // Thêm playlistUser vào danh sách nếu không phải null
        dataPlaylist.setEncodeId(generateUniqueEncodeId());
        dataPlaylistArrayList.add(dataPlaylist);

        // Lưu danh sách PlaylistUser đã cập nhật vào SharedPreferences
        Gson gson = new Gson();  // Có thể xem xét đưa ra ngoài để tái sử dụng
        String playlist_user_json = gson.toJson(dataPlaylistArrayList);

        SharedPreferences sharedPreferences = context.getSharedPreferences("playlist_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("playlist_user", playlist_user_json);
        editor.apply();

        Log.d("SharedPreferencesManager", "Saved playlistUser: " + playlist_user_json);
    }

    public ArrayList<DataPlaylist> restorePlaylistUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("playlist_user", Context.MODE_PRIVATE);
        String playlist_user_gson = sharedPreferences.getString("playlist_user", null);

        if (playlist_user_gson != null) {
            try {
                Gson gson = new Gson();  // Có thể xem xét đưa ra ngoài để tái sử dụng
                Type type = new TypeToken<ArrayList<DataPlaylist>>() {
                }.getType();
                return gson.fromJson(playlist_user_gson, type);
            } catch (JsonSyntaxException e) {
                // Log the error
                Log.e("SharedPreferencesManager", "Failed to parse playlist user JSON", e);
                // Xóa dữ liệu bị hỏng và trả về danh sách rỗng
                sharedPreferences.edit().remove("playlist_user").apply();
            }
        }

        // Trả về danh sách rỗng nếu không có dữ liệu hoặc dữ liệu bị hỏng
        return new ArrayList<>();
    }

    public DataPlaylist getPlaylistUserByEncodeId(String encodeId) {
        ArrayList<DataPlaylist> dataPlaylistArrayList = restorePlaylistUser();

        for (DataPlaylist dataPlaylist : dataPlaylistArrayList) {
            if (dataPlaylist.getEncodeId().equals(encodeId)) {
                return dataPlaylist;
            }
        }

        return null;  // Trả về null nếu không tìm thấy playlist với encodeId này
    }


    //playlist_user song
    private void savePlaylistUserList(ArrayList<DataPlaylist> playlistUsers) {
        Gson gson = new Gson();
        String playlistUserJson = gson.toJson(playlistUsers);

        SharedPreferences sharedPreferences = context.getSharedPreferences("playlist_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("playlist_user", playlistUserJson);
        editor.apply();

        Log.d("SharedPreferencesManager", "Saved playlist user list: " + playlistUserJson);
    }

    public void addSongToPlaylistByEncodeId(String encodeId, Items newSong) {
        // Lấy danh sách các playlist
        ArrayList<DataPlaylist> dataPlaylistArrayList = restorePlaylistUser();

        // Tìm playlist theo encodeId
        DataPlaylist targetPlaylist = null;
        for (DataPlaylist dataPlaylist : dataPlaylistArrayList) {
            if (dataPlaylist.getEncodeId().equals(encodeId)) {
                targetPlaylist = dataPlaylist;
                break;
            }
        }

        if (targetPlaylist != null) {
            // Kiểm tra xem bài hát đã tồn tại trong playlist chưa
            boolean isSongExist = false;
            DataItems dataItems = targetPlaylist.getSong();
            if (dataItems != null) {
                for (Items item : dataItems.getItems()) {
                    if (item.getEncodeId().equals(newSong.getEncodeId())) {
                        isSongExist = true;
                        break;
                    }
                }
            } else {
                dataItems = new DataItems();
            }

            // Nếu bài hát chưa tồn tại thì thêm mới
            if (!isSongExist) {
                dataItems.addItem(newSong);
                targetPlaylist.setSong(dataItems);

                // Lưu danh sách playlist đã cập nhật vào SharedPreferences
                savePlaylistUserList(dataPlaylistArrayList);

                Log.d("SharedPreferencesManager", "Added song to playlist with encodeId " + encodeId + ": " + new Gson().toJson(newSong));
            } else {
                Log.d("SharedPreferencesManager", "Song with encodeId " + newSong.getEncodeId() + " already exists in playlist with encodeId " + encodeId);
            }
        } else {
            Log.e("SharedPreferencesManager", "Playlist with encodeId " + encodeId + " not found");
        }
    }

    public void removeSongFromPlaylistByEncodeId(String playlistEncodeId, String songEncodeId) {
        // Lấy danh sách các playlist
        ArrayList<DataPlaylist> dataPlaylistArrayList = restorePlaylistUser();

        // Tìm playlist theo encodeId
        DataPlaylist targetPlaylist = null;
        for (DataPlaylist dataPlaylist : dataPlaylistArrayList) {
            if (dataPlaylist.getEncodeId().equals(playlistEncodeId)) {
                targetPlaylist = dataPlaylist;
                break;
            }
        }

        if (targetPlaylist != null) {
            // Lấy DataItems từ playlist
            DataItems dataItems = targetPlaylist.getSong();
            if (dataItems != null) {
                // Tìm và xóa bài hát từ DataItems
                Items itemToRemove = null;
                for (Items item : dataItems.getItems()) {
                    if (item.getEncodeId().equals(songEncodeId)) {
                        itemToRemove = item;
                        break;
                    }
                }

                if (itemToRemove != null) {
                    dataItems.getItems().remove(itemToRemove);
                    dataItems.updateTotal(); // Cập nhật tổng số bài hát
                    dataItems.updateTotalDuration(); // Cập nhật tổng thời gian

                    // Nếu không còn bài hát nào, xóa DataItems khỏi playlist
                    if (dataItems.getTotal() == 0) {
                        targetPlaylist.setSong(null);
                    }

                    // Lưu danh sách playlist đã cập nhật vào SharedPreferences
                    savePlaylistUserList(dataPlaylistArrayList);

                    Log.d("SharedPreferencesManager", "Removed song with encodeId " + songEncodeId + " from playlist with encodeId " + playlistEncodeId);
                } else {
                    Log.d("SharedPreferencesManager", "Song with encodeId " + songEncodeId + " not found in playlist with encodeId " + playlistEncodeId);
                }
            } else {
                Log.d("SharedPreferencesManager", "No songs in playlist with encodeId " + playlistEncodeId);
            }
        } else {
            Log.e("SharedPreferencesManager", "Playlist with encodeId " + playlistEncodeId + " not found");
        }
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
        int maxSearchHistorySize = 50; // Số lượng tối đa mục trong danh sách lịch sử tìm kiếm
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
                Type type = new TypeToken<ArrayList<DataSearchRecommend>>() {
                }.getType();
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

    // Delete a specific DataSearchRecommend by keyword
    public void deleteSearchHistoryItem(DataSearchRecommend dataSearchRecommend) {
        ArrayList<DataSearchRecommend> searchHistory = restoreSearchHistory();

        // Xóa mục khỏi danh sách nếu có
        searchHistory.remove(dataSearchRecommend);

        // Lưu danh sách lịch sử tìm kiếm đã cập nhật vào SharedPreferences
        Gson gson = new Gson();
        String searchHistoryJson = gson.toJson(searchHistory);

        SharedPreferences sharedPreferences = context.getSharedPreferences("searchHistory", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("search_history", searchHistoryJson);
        editor.apply();
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
}
