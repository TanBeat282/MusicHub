package com.tandev.musichub.api.type_adapter_Factory.artist;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tandev.musichub.model.artist.SectionArtist;
import com.tandev.musichub.model.artist.artist.SectionArtistArtist;
import com.tandev.musichub.model.artist.playlist.SectionArtistPlaylist;
import com.tandev.musichub.model.artist.song.SectionArtistSong;
import com.tandev.musichub.model.artist.video.SectionArtistVideo;
import com.tandev.musichub.model.hub.HubSection;
import com.tandev.musichub.model.hub.SectionHubArtist;
import com.tandev.musichub.model.hub.SectionHubPlaylist;
import com.tandev.musichub.model.hub.SectionHubSong;
import com.tandev.musichub.model.hub.SectionHubVideo;

import java.lang.reflect.Type;

public class ArtistTypeAdapter implements JsonDeserializer<SectionArtist> {
    @Override
    public SectionArtist deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (!jsonObject.has("sectionType")) {
            throw new JsonParseException("Missing sectionType field");
        }

        String sectionType = jsonObject.get("sectionType").getAsString();

        switch (sectionType) {
            case "song":
                return context.deserialize(jsonObject, SectionArtistSong.class);
            case "playlist":
                return context.deserialize(jsonObject, SectionArtistPlaylist.class);
            case "video":
                return context.deserialize(jsonObject, SectionArtistVideo.class);
            case "artist":
                return context.deserialize(jsonObject, SectionArtistArtist.class);
            default:
                throw new JsonParseException("Unknown sectionType: " + sectionType);
        }
    }
}
