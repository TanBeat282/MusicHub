package com.tandev.musichub.api.type_adapter_Factory.home;

import com.tandev.musichub.model.chart.home.home_new.ad_banner.HomeDataItemAdBanner;
import com.tandev.musichub.model.chart.home.home_new.album.HomeDataItemPlaylistAlbum;
import com.tandev.musichub.model.chart.home.home_new.banner.HomeDataItemBanner;
import com.tandev.musichub.model.chart.home.home_new.editor_theme.HomeDataItemPlaylistEditorTheme;
import com.tandev.musichub.model.chart.home.home_new.editor_theme_3.HomeDataItemPlaylistEditorTheme3;
import com.tandev.musichub.model.chart.home.home_new.history.HomeDataItemHistory;
import com.tandev.musichub.model.chart.home.home_new.item.HomeDataItem;
import com.tandev.musichub.model.chart.home.home_new.new_release.HomeDataItemNewRelease;
import com.tandev.musichub.model.chart.home.home_new.new_release_chart.HomeDataItemNewReleaseChart;
import com.tandev.musichub.model.chart.home.home_new.radio.HomeDataItemRadio;
import com.tandev.musichub.model.chart.home.home_new.rt_chart.HomeDataItemRTChart;
import com.tandev.musichub.model.chart.home.home_new.season_theme.HomeDataItemPlaylistSeasonTheme;
import com.tandev.musichub.model.chart.home.home_new.top100.HomeDataItemPlaylistTop100;
import com.tandev.musichub.model.chart.home.home_new.week_chart.HomeDataItemWeekChart;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class HomeDataItemTypeAdapter implements JsonDeserializer<HomeDataItem> {
    @Override
    public HomeDataItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String sectionType = jsonObject.get("sectionType").getAsString();

        switch (sectionType) {
            case "banner":
                return context.deserialize(jsonObject, HomeDataItemBanner.class);
            case "recentPlaylist":
                return context.deserialize(jsonObject, HomeDataItemHistory.class);
            case "new-release":
                return context.deserialize(jsonObject, HomeDataItemNewRelease.class);
            case "playlist":
                switch (jsonObject.get("sectionId").getAsString()) {
                    case "hSeasonTheme":
                        return context.deserialize(jsonObject, HomeDataItemPlaylistSeasonTheme.class);
                    case "hEditorTheme":
                        return context.deserialize(jsonObject, HomeDataItemPlaylistEditorTheme.class);
                    case "hEditorTheme3":
                        return context.deserialize(jsonObject, HomeDataItemPlaylistEditorTheme3.class);
                    case "h100":
                        return context.deserialize(jsonObject, HomeDataItemPlaylistTop100.class);
                    case "hAlbum":
                        return context.deserialize(jsonObject, HomeDataItemPlaylistAlbum.class);
                    default:
                        throw new JsonParseException("Unknown sectionId for playlist: " + jsonObject.get("sectionId").getAsString());
                }
            case "newReleaseChart":
                return context.deserialize(jsonObject, HomeDataItemNewReleaseChart.class);
            case "RTChart":
                return context.deserialize(jsonObject, HomeDataItemRTChart.class);
            case "weekChart":
                return context.deserialize(jsonObject, HomeDataItemWeekChart.class);
            case "adBanner":
                return context.deserialize(jsonObject, HomeDataItemAdBanner.class);
            case "livestream":
                return context.deserialize(jsonObject, HomeDataItemRadio.class);
            default:
                throw new JsonParseException("Unknown sectionType: " + sectionType);
        }
    }
}
