package com.tandev.musichub.api.type_adapter_Factory.section_bottom;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tandev.musichub.model.section_bottom.DataSectionBottom;
import com.tandev.musichub.model.section_bottom.DataSectionBottomAdBanner;
import com.tandev.musichub.model.section_bottom.DataSectionBottomArtist;
import com.tandev.musichub.model.section_bottom.DataSectionBottomPlaylist;
import com.tandev.musichub.model.section_bottom.DataSectionBottomPlaylistOfAritst;

import java.lang.reflect.Type;

public class SectionBottomTypeAdapter implements JsonDeserializer<DataSectionBottom> {
    @Override
    public DataSectionBottom deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (!jsonObject.has("sectionType")) {
            throw new JsonParseException("Missing sectionType field");
        }

        String sectionType = jsonObject.get("sectionType").getAsString();

        switch (sectionType) {
            case "artist":
                return context.deserialize(jsonObject, DataSectionBottomArtist.class);
            case "playlist":
                return context.deserialize(jsonObject, DataSectionBottomPlaylist.class);
            case "playlistOfArtist":
                return context.deserialize(jsonObject, DataSectionBottomPlaylistOfAritst.class);
            case "adBanner":
                return context.deserialize(jsonObject, DataSectionBottomAdBanner.class);
            default:
                throw new JsonParseException("Unknown sectionType: " + sectionType);
        }
    }
}
