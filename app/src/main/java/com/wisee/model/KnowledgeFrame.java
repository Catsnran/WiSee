package com.wisee.model;

public class KnowledgeFrame {
    public final String word;
    public final String category;
    public final String emoji;
    public final String audioText;    // teks TTS bahasa Indonesia
    public final String imagePrompt;  // prompt AI image generation
    public final boolean safe;        // false = konten negatif

    public KnowledgeFrame(String word, String category, String emoji,
                          String audioText, String imagePrompt, boolean safe) {
        this.word = word;
        this.category = category;
        this.emoji = emoji;
        this.audioText = audioText;
        this.imagePrompt = imagePrompt;
        this.safe = safe;
    }
}
