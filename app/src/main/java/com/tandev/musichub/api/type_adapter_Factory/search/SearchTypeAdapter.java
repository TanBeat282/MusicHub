package com.tandev.musichub.api.type_adapter_Factory.search;

import com.tandev.musichub.model.search.search_suggestion.Tracking;
import com.tandev.musichub.model.search.search_suggestion.SearchSuggestionsDataItem;
import com.tandev.musichub.model.search.search_suggestion.keyword.SearchSuggestionsDataItemKeyWords;
import com.tandev.musichub.model.search.search_suggestion.playlist.SearchSuggestionsDataItemSuggestionsPlaylist;
import com.tandev.musichub.model.search.search_suggestion.suggestion.SearchSuggestionsDataItemSuggestions;
import com.tandev.musichub.model.search.search_suggestion.suggestion.SearchSuggestionsDataItemSuggestionsArtist;
import com.tandev.musichub.model.search.search_suggestion.suggestion.SearchSuggestionsDataItemSuggestionsItem;
import com.tandev.musichub.model.search.search_suggestion.suggestion.SearchSuggestionsDataItemSuggestionsSong;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SearchTypeAdapter implements JsonDeserializer<SearchSuggestionsDataItem> {
    @Override
    public SearchSuggestionsDataItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (jsonObject.has("keywords")) {
            return context.deserialize(jsonObject, SearchSuggestionsDataItemKeyWords.class);
        } else if (jsonObject.has("suggestions")) {
            SearchSuggestionsDataItemSuggestions searchSuggestionsDataItemSuggestions = new SearchSuggestionsDataItemSuggestions();
            JsonArray suggestionsArray = jsonObject.getAsJsonArray("suggestions");
            ArrayList<SearchSuggestionsDataItemSuggestionsItem> suggestions = new ArrayList<>();

            for (JsonElement element : suggestionsArray) {
                JsonObject suggestionObject = element.getAsJsonObject();
                int type = suggestionObject.get("type").getAsInt();

                switch (type) {
                    case 4:
                        SearchSuggestionsDataItemSuggestionsArtist artist = context.deserialize(suggestionObject, SearchSuggestionsDataItemSuggestionsArtist.class);
                        suggestions.add(artist);
                        break;
                    case 1:
                        SearchSuggestionsDataItemSuggestionsSong song = context.deserialize(suggestionObject, SearchSuggestionsDataItemSuggestionsSong.class);
                        suggestions.add(song);
                        break;
                    case 3:
                        SearchSuggestionsDataItemSuggestionsPlaylist playlist = context.deserialize(suggestionObject, SearchSuggestionsDataItemSuggestionsPlaylist.class);
                        suggestions.add(playlist);
                        break;
                    default:
                        throw new JsonParseException(">>>>>>>>>>>>  Unknown sectionType: " + type);
                }
            }

            searchSuggestionsDataItemSuggestions.setSuggestions(suggestions);
            return searchSuggestionsDataItemSuggestions;
        } else {
            return context.deserialize(jsonObject, Tracking.class);
        }

    }
}
