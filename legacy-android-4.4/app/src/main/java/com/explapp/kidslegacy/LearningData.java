package com.explapp.kidslegacy;

import android.graphics.Color;

public final class LearningData {
    private LearningData() {}

    private static final int PURPLE = Color.rgb(108, 99, 255);
    private static final int BLUE = Color.rgb(53, 132, 228);
    private static final int GREEN = Color.rgb(52, 168, 83);
    private static final int ORANGE = Color.rgb(245, 124, 54);
    private static final int PINK = Color.rgb(232, 93, 142);
    private static final int GOLD = Color.rgb(237, 180, 35);

    public static final LearnItem[] ARABIC_LETTERS = buildArabicLetters();
    public static final LearnItem[] ENGLISH_LETTERS = buildEnglishLetters();
    public static final LearnItem[] ARABIC_NUMBERS = buildArabicNumbers();
    public static final LearnItem[] ENGLISH_NUMBERS = buildEnglishNumbers();
    public static final LearnItem[] ARABIC_COLORS = buildArabicColors();
    public static final LearnItem[] ENGLISH_COLORS = buildEnglishColors();
    public static final LearnItem[] ANIMALS_ARABIC = buildAnimalsArabic();
    public static final LearnItem[] ANIMALS_ENGLISH = buildAnimalsEnglish();

    private static LearnItem[] buildArabicLetters() {
        String[] letters = {"أ","ب","ت","ث","ج","ح","خ","د","ذ","ر","ز","س","ش","ص","ض","ط","ظ","ع","غ","ف","ق","ك","ل","م","ن","هـ","و","ي"};
        String[] words = {"أسد","بطة","تفاحة","ثعلب","جمل","حصان","خروف","دجاجة","ذرة","رمان","زرافة","سمكة","شمس","صقر","ضفدع","طائرة","ظرف","عصفور","غزال","فيل","قطة","كلب","ليمون","موز","نمر","هلال","وردة","يد"};
        LearnItem[] result = new LearnItem[letters.length];
        for (int i = 0; i < letters.length; i++) {
            result[i] = new LearnItem(
                    "ar_letter_" + i,
                    letters[i],
                    words[i],
                    "حرف " + letters[i] + ". " + words[i],
                    "ar",
                    LearnItem.TYPE_LETTER,
                    colorForIndex(i)
            );
        }
        return result;
    }

    private static LearnItem[] buildEnglishLetters() {
        String[] letters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        String[] words = {"Apple","Ball","Cat","Dog","Elephant","Fish","Giraffe","Horse","Ice Cream","Juice","Kite","Lion","Monkey","Nest","Orange","Panda","Queen","Rabbit","Sun","Tiger","Umbrella","Van","Water","Xylophone","Yo-yo","Zebra"};
        LearnItem[] result = new LearnItem[letters.length];
        for (int i = 0; i < letters.length; i++) {
            result[i] = new LearnItem(
                    "en_letter_" + i,
                    letters[i],
                    words[i],
                    letters[i] + ". " + words[i],
                    "en",
                    LearnItem.TYPE_LETTER,
                    colorForIndex(i)
            );
        }
        return result;
    }

    private static LearnItem[] buildArabicNumbers() {
        String[] names = {"واحد","اثنان","ثلاثة","أربعة","خمسة","ستة","سبعة","ثمانية","تسعة","عشرة"};
        LearnItem[] result = new LearnItem[10];
        for (int i = 0; i < 10; i++) {
            int number = i + 1;
            result[i] = new LearnItem(
                    "ar_number_" + number,
                    String.valueOf(number),
                    names[i],
                    names[i],
                    "ar",
                    LearnItem.TYPE_NUMBER,
                    colorForIndex(i)
            );
        }
        return result;
    }

    private static LearnItem[] buildEnglishNumbers() {
        String[] names = {"One","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten"};
        LearnItem[] result = new LearnItem[10];
        for (int i = 0; i < 10; i++) {
            int number = i + 1;
            result[i] = new LearnItem(
                    "en_number_" + number,
                    String.valueOf(number),
                    names[i],
                    names[i],
                    "en",
                    LearnItem.TYPE_NUMBER,
                    colorForIndex(i)
            );
        }
        return result;
    }

    private static LearnItem[] buildArabicColors() {
        return new LearnItem[] {
                color("color_red", "أحمر", "Red", Color.rgb(231, 76, 60), "ar"),
                color("color_blue", "أزرق", "Blue", Color.rgb(52, 152, 219), "ar"),
                color("color_green", "أخضر", "Green", Color.rgb(46, 204, 113), "ar"),
                color("color_yellow", "أصفر", "Yellow", Color.rgb(241, 196, 15), "ar"),
                color("color_black", "أسود", "Black", Color.rgb(45, 45, 45), "ar"),
                color("color_white", "أبيض", "White", Color.rgb(235, 235, 235), "ar"),
                color("color_brown", "بني", "Brown", Color.rgb(141, 85, 36), "ar"),
                color("color_purple", "بنفسجي", "Purple", Color.rgb(142, 68, 173), "ar")
        };
    }

    private static LearnItem[] buildEnglishColors() {
        return new LearnItem[] {
                color("color_red", "Red", "أحمر", Color.rgb(231, 76, 60), "en"),
                color("color_blue", "Blue", "أزرق", Color.rgb(52, 152, 219), "en"),
                color("color_green", "Green", "أخضر", Color.rgb(46, 204, 113), "en"),
                color("color_yellow", "Yellow", "أصفر", Color.rgb(241, 196, 15), "en"),
                color("color_black", "Black", "أسود", Color.rgb(45, 45, 45), "en"),
                color("color_white", "White", "أبيض", Color.rgb(235, 235, 235), "en"),
                color("color_brown", "Brown", "بني", Color.rgb(141, 85, 36), "en"),
                color("color_purple", "Purple", "بنفسجي", Color.rgb(142, 68, 173), "en")
        };
    }

    private static LearnItem color(String id, String title, String subtitle, int color, String locale) {
        return new LearnItem(id, title, subtitle, title, locale, LearnItem.TYPE_COLOR, color);
    }

    private static LearnItem[] buildAnimalsArabic() {
        return new LearnItem[] {
                animal("animal_lion", "أسد", "Lion", "الأسد", "ar", LearnItem.TYPE_LION, Color.rgb(238, 165, 53)),
                animal("animal_elephant", "فيل", "Elephant", "الفيل", "ar", LearnItem.TYPE_ELEPHANT, Color.rgb(120, 144, 156)),
                animal("animal_cat", "قطة", "Cat", "القطة", "ar", LearnItem.TYPE_CAT, Color.rgb(245, 166, 35)),
                animal("animal_dog", "كلب", "Dog", "الكلب", "ar", LearnItem.TYPE_DOG, Color.rgb(176, 124, 72)),
                animal("animal_horse", "حصان", "Horse", "الحصان", "ar", LearnItem.TYPE_HORSE, Color.rgb(121, 85, 72)),
                animal("animal_rabbit", "أرنب", "Rabbit", "الأرنب", "ar", LearnItem.TYPE_RABBIT, Color.rgb(214, 155, 196)),
                animal("animal_cow", "بقرة", "Cow", "البقرة", "ar", LearnItem.TYPE_COW, Color.rgb(100, 100, 100)),
                animal("animal_monkey", "قرد", "Monkey", "القرد", "ar", LearnItem.TYPE_MONKEY, Color.rgb(141, 93, 58)),
                animal("animal_giraffe", "زرافة", "Giraffe", "الزرافة", "ar", LearnItem.TYPE_GIRAFFE, Color.rgb(235, 177, 52)),
                animal("animal_tiger", "نمر", "Tiger", "النمر", "ar", LearnItem.TYPE_TIGER, Color.rgb(239, 125, 35)),
                animal("animal_bear", "دب", "Bear", "الدب", "ar", LearnItem.TYPE_BEAR, Color.rgb(111, 78, 55)),
                animal("animal_fish", "سمكة", "Fish", "السمكة", "ar", LearnItem.TYPE_FISH, Color.rgb(42, 158, 205))
        };
    }

    private static LearnItem[] buildAnimalsEnglish() {
        return new LearnItem[] {
                animal("animal_lion", "Lion", "أسد", "Lion", "en", LearnItem.TYPE_LION, Color.rgb(238, 165, 53)),
                animal("animal_elephant", "Elephant", "فيل", "Elephant", "en", LearnItem.TYPE_ELEPHANT, Color.rgb(120, 144, 156)),
                animal("animal_cat", "Cat", "قطة", "Cat", "en", LearnItem.TYPE_CAT, Color.rgb(245, 166, 35)),
                animal("animal_dog", "Dog", "كلب", "Dog", "en", LearnItem.TYPE_DOG, Color.rgb(176, 124, 72)),
                animal("animal_horse", "Horse", "حصان", "Horse", "en", LearnItem.TYPE_HORSE, Color.rgb(121, 85, 72)),
                animal("animal_rabbit", "Rabbit", "أرنب", "Rabbit", "en", LearnItem.TYPE_RABBIT, Color.rgb(214, 155, 196)),
                animal("animal_cow", "Cow", "بقرة", "Cow", "en", LearnItem.TYPE_COW, Color.rgb(100, 100, 100)),
                animal("animal_monkey", "Monkey", "قرد", "Monkey", "en", LearnItem.TYPE_MONKEY, Color.rgb(141, 93, 58)),
                animal("animal_giraffe", "Giraffe", "زرافة", "Giraffe", "en", LearnItem.TYPE_GIRAFFE, Color.rgb(235, 177, 52)),
                animal("animal_tiger", "Tiger", "نمر", "Tiger", "en", LearnItem.TYPE_TIGER, Color.rgb(239, 125, 35)),
                animal("animal_bear", "Bear", "دب", "Bear", "en", LearnItem.TYPE_BEAR, Color.rgb(111, 78, 55)),
                animal("animal_fish", "Fish", "سمكة", "Fish", "en", LearnItem.TYPE_FISH, Color.rgb(42, 158, 205))
        };
    }

    private static LearnItem animal(String id, String title, String subtitle, String speak, String locale, int type, int color) {
        return new LearnItem(id, title, subtitle, speak, locale, type, color);
    }

    private static int colorForIndex(int index) {
        int[] colors = {PURPLE, BLUE, GREEN, ORANGE, PINK, GOLD};
        return colors[index % colors.length];
    }
}
