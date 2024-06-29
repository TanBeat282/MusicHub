package com.tandev.musichub.model.sectionBottom;


import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.playlist.DataPlaylist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class SectionBottom implements Serializable {
    private int err;
    private String msg;
    private DataSectionBottom data;
    private long timestamp;
    private DataSectionBottomArtist artist;
    private DataSectionBottomPlaylist playlist;

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataSectionBottom getData() {
        return data;
    }

    public void setData(DataSectionBottom data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void parseFromJson(JSONObject jsonObject) throws JSONException {
        setErr(jsonObject.getInt("err"));
        setMsg(jsonObject.getString("msg"));
        setTimestamp(jsonObject.getLong("timestamp"));

        JSONArray dataArray = jsonObject.getJSONArray("data");

        JSONObject firstItemObject = dataArray.getJSONObject(0);
        artist = parseArtistData(firstItemObject);

        JSONObject trueItemObject = dataArray.getJSONObject(1);
        playlist = (parsePlaylistData(trueItemObject));

        data = parseDataBottom(artist, playlist);
    }

    private DataSectionBottomArtist parseArtistData(JSONObject jsonObject) throws JSONException {
        DataSectionBottomArtist dataSectionBottomArtist = new DataSectionBottomArtist();
        dataSectionBottomArtist.setSectionType(jsonObject.getString("sectionType"));
        dataSectionBottomArtist.setViewType(jsonObject.getString("viewType"));
        dataSectionBottomArtist.setTitle(jsonObject.getString("title"));
        dataSectionBottomArtist.setLink(jsonObject.getString("link"));
        dataSectionBottomArtist.setSectionId(jsonObject.getString("sectionId"));

        JSONArray innerItemsArrayArtist = jsonObject.getJSONArray("items");
        ArrayList<Artists> innerArtist = new ArrayList<>();
        for (int i = 0; i < innerItemsArrayArtist.length(); i++) {
            JSONObject innerArtistObject = innerItemsArrayArtist.getJSONObject(i);
            Artists artists = Artists.fromJson(innerArtistObject.toString());
            innerArtist.add(artists);
        }
        dataSectionBottomArtist.setItems(innerArtist);
        return dataSectionBottomArtist;
    }

    private DataSectionBottomPlaylist parsePlaylistData(JSONObject jsonObject) throws JSONException {
        DataSectionBottomPlaylist dataSectionBottomPlaylist = new DataSectionBottomPlaylist();
        dataSectionBottomPlaylist.setSectionType(jsonObject.getString("sectionType"));
        dataSectionBottomPlaylist.setViewType(jsonObject.getString("viewType"));
        dataSectionBottomPlaylist.setTitle(jsonObject.getString("title"));
        dataSectionBottomPlaylist.setLink(jsonObject.getString("link"));
        dataSectionBottomPlaylist.setSectionId(jsonObject.getString("sectionId"));

        // Kiểm tra xem trường "items" có tồn tại và không null
        if (jsonObject.has("items") && !jsonObject.isNull("items")) {
            JSONArray innerItemsArrayPlaylist = jsonObject.getJSONArray("items");
            ArrayList<DataPlaylist> dataPlaylistArrayList = new ArrayList<>();
            for (int i = 0; i < innerItemsArrayPlaylist.length(); i++) {
                JSONObject innerArtistObject = innerItemsArrayPlaylist.getJSONObject(i);
                DataPlaylist dataPlaylist = DataPlaylist.fromJson(innerArtistObject.toString());
                dataPlaylistArrayList.add(dataPlaylist);
            }
            dataSectionBottomPlaylist.setItems(dataPlaylistArrayList);
        } else {
            dataSectionBottomPlaylist.setItems(new ArrayList<>()); // Hoặc có thể giữ null tùy theo yêu cầu của bạn
        }

        return dataSectionBottomPlaylist;
    }


    private DataSectionBottom parseDataBottom(DataSectionBottomArtist dataSectionBottomArtist, DataSectionBottomPlaylist dataSectionBottomPlaylist) throws JSONException {
        DataSectionBottom dataSectionBottom = new DataSectionBottom();
        dataSectionBottom.setArtist(dataSectionBottomArtist);
        dataSectionBottom.setPlaylist(dataSectionBottomPlaylist);
        return dataSectionBottom;
    }
}
