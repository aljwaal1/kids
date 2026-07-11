package com.explapp.kidslegacy;

public final class LearnItem {
    public static final int TYPE_LETTER = 1;
    public static final int TYPE_NUMBER = 2;
    public static final int TYPE_COLOR = 3;
    public static final int TYPE_LION = 10;
    public static final int TYPE_ELEPHANT = 11;
    public static final int TYPE_CAT = 12;
    public static final int TYPE_DOG = 13;
    public static final int TYPE_HORSE = 14;
    public static final int TYPE_RABBIT = 15;
    public static final int TYPE_COW = 16;
    public static final int TYPE_MONKEY = 17;
    public static final int TYPE_GIRAFFE = 18;
    public static final int TYPE_TIGER = 19;
    public static final int TYPE_BEAR = 20;
    public static final int TYPE_FISH = 21;

    public final String id;
    public final String title;
    public final String subtitle;
    public final String speakText;
    public final String locale;
    public final int illustrationType;
    public final int accentColor;

    public LearnItem(
            String id,
            String title,
            String subtitle,
            String speakText,
            String locale,
            int illustrationType,
            int accentColor
    ) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.speakText = speakText;
        this.locale = locale;
        this.illustrationType = illustrationType;
        this.accentColor = accentColor;
    }
}
