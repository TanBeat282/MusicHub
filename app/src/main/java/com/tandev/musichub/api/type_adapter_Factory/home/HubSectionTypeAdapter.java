package com.tandev.musichub.api.type_adapter_Factory.home;

import com.tandev.musichub.model.hub.HubSection;
import com.tandev.musichub.model.hub.SectionHubArtist;
import com.tandev.musichub.model.hub.SectionHubPlaylist;
import com.tandev.musichub.model.hub.SectionHubSong;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tandev.musichub.model.hub.SectionHubVideo;

import java.lang.reflect.Type;

public class HubSectionTypeAdapter implements JsonDeserializer<HubSection> {
    @Override
    public HubSection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (!jsonObject.has("sectionType")) {
            throw new JsonParseException("Missing sectionType field");
        }

        String sectionType = jsonObject.get("sectionType").getAsString();

        switch (sectionType) {
            case "playlist":
                return context.deserialize(jsonObject, SectionHubPlaylist.class);
            case "song":
                return context.deserialize(jsonObject, SectionHubSong.class);
            case "video":
                return context.deserialize(jsonObject, SectionHubVideo.class);
            case "artist":
                return context.deserialize(jsonObject, SectionHubArtist.class);
            default:
                throw new JsonParseException("Unknown sectionType: " + sectionType);
        }
    }
}
