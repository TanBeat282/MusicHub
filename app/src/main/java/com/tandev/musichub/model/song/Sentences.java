package com.tandev.musichub.model.song;

import java.io.Serializable;
import java.util.ArrayList;

public class Sentences implements Serializable {
    private ArrayList<Word> words;

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }
}
