package com.tandev.musichub.model.lyric;

import com.tandev.musichub.model.lyric.Word;

import java.util.ArrayList;

public class Sentence {
    private ArrayList<Word> words;

    public Sentence(ArrayList<Word> words) {
        this.words = words;
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    // Method to get the start time of the sentence
    public long getStartTime() {
        return words.isEmpty() ? 0 : words.get(0).getStartTime();
    }

    // Method to get the end time of the sentence
    public long getEndTime() {
        return words.isEmpty() ? 0 : words.get(words.size() - 1).getEndTime();
    }
}
