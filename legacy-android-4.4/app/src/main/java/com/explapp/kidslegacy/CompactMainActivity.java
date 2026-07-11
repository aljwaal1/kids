package com.explapp.kidslegacy;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.InputType;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public final class CompactMainActivity extends Activity implements TextToSpeech.OnInitListener {
    private static final int HOME = 0;
    private static final int ARABIC = 1;
    private static final int ENGLISH = 2;
    private static final int LESSON = 3;
    private static final int GAMES = 4;
    private static final int QUIZ = 5;
    private static final int ACHIEVEMENTS = 6;
    private static final int PARENT_GATE = 7;
    private static final int PARENT_GROUPS = 8;
    private static final int RECORDINGS = 9;
    private static final int REQUEST_RECORD_AUDIO = 8105;

    private static final int PURPLE = Color.rgb(109, 40, 217);
    private static final int BLUE = Color.rgb(2, 132, 199);
    private static final int GREEN = Color.rgb(5, 150, 105);
    private static final int ORANGE = Color.rgb(234, 88, 12);
    private static final int PINK = Color.rgb(219, 39, 119);
    private static final int GOLD = Color.rgb(217, 119, 6);
    private static final int RED = Color.rgb(225, 29, 72);
    private static final int TEXT = Color.rgb(23, 32, 51);
    private static final int MUTED = Color.rgb(71, 85, 105);
    private static final int BG = Color.rgb(238, 242, 255);

    private final ArrayList<PageState> backStack = new ArrayList<PageState>();
    private final Random random = new Random();
    private final Handler handler = new Handler();
    private final ArrayList<LearnItem> quizOptions = new ArrayList<LearnItem>();

    private PageState current;
    private SharedPreferences prefs;
    private TextToSpeech tts;
    private boolean ttsReady;
    private ToneGenerator tones;
    private MediaPlayer player;
    private MediaRecorder recorder;
    private String activeRecordingId = "";
    private LearnItem pendingRecordingItem;

    private QuizConfig activeQuiz;
    private LearnItem quizTarget;
    private String lastTargetId = "";
    private String quizMessage = "";
    private int currentScore;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(67, 56, 202));
            getWindow().setNavigationBarColor(Color.rgb(30, 27, 75));
        }
        prefs = getSharedPreferences("kids_progress", Context.MODE_PRIVATE);
        tones = new ToneGenerator(AudioManager.STREAM_MUSIC, 52);
        tts = new TextToSpeech(this, this);
        current = PageState.simple(HOME);
        render();
    }

    @Override
    public void onInit(int status) {
        ttsReady = status == TextToSpeech.SUCCESS;
        if (ttsReady) {
            tts.setSpeechRate(.80f);
            tts.setPitch(1.04f);
        }
    }

    private void navigate(PageState next) {
        stopRecordingIfNeeded();
        if (current != null) backStack.add(current.copy());
        current = next;
        render();
    }

    @Override
    public void onBackPressed() {
        if (recorder != null) {
            stopRecording(true);
            render();
            return;
        }
        if (backStack.isEmpty()) {
            super.onBackPressed();
            return;
        }
        current = backStack.remove(backStack.size() - 1);
        render();
    }

    private void render() {
        stopPlayback();
        switch (current.type) {
            case HOME: showHome(); break;
            case ARABIC: showLanguage(true); break;
            case ENGLISH: showLanguage(false); break;
            case LESSON: showLesson(); break;
            case GAMES: showGames(); break;
            case QUIZ: showQuiz(); break;
            case ACHIEVEMENTS: showAchievements(); break;
            case PARENT_GATE: showParentGate(); break;
            case PARENT_GROUPS: showParentGroups(); break;
            case RECORDINGS: showRecordingItem(); break;
            default: showHome(); break;
        }
    }

    private void showHome() {
        LinearLayout page = createPage("طفلي الأول", true);
        LinearLayout body = content(page);

        LinearLayout hero = vertical();
        hero.setGravity(Gravity.CENTER);
        hero.setPadding(dp(10), dp(8), dp(10), dp(8));
        hero.setBackground(gradient(PURPLE, BLUE, 24));
        hero.addView(label("تعلّم والعب", 31, Color.WHITE, Gravity.CENTER, true));
        hero.addView(label("كل شاشة كاملة دون تمرير للأسفل", 15, Color.WHITE, Gravity.CENTER, true));
        body.addView(hero, fixed(dp(82), dp(5)));

        View[] tiles = new View[] {
                menuTile("ع", "العربية", "حروف وأرقام وألوان", PURPLE, new View.OnClickListener() {
                    @Override public void onClick(View v) { tap(); navigate(PageState.simple(ARABIC)); }
                }),
                menuTile("A", "English", "Letters and numbers", BLUE, new View.OnClickListener() {
                    @Override public void onClick(View v) { tap(); navigate(PageState.simple(ENGLISH)); }
                }),
                menuTile("ح", "الحيوانات", "صور عربي وإنجليزي", ORANGE, new View.OnClickListener() {
                    @Override public void onClick(View v) { tap(); navigate(PageState.lesson("الحيوانات", LearningData.ANIMALS_ARABIC, true)); }
                }),
                menuTile("?", "الألعاب", "أسئلة ونجوم", GREEN, new View.OnClickListener() {
                    @Override public void onClick(View v) { tap(); navigate(PageState.simple(GAMES)); }
                }),
                menuTile("★", "إنجازاتي", "النجوم والميداليات", GOLD, new View.OnClickListener() {
                    @Override public void onClick(View v) { tap(); navigate(PageState.simple(ACHIEVEMENTS)); }
                }),
                menuTile("ص", "قسم الوالدين", "تسجيل صوت الأسرة", PINK, new View.OnClickListener() {
                    @Override public void onClick(View v) { tap(); navigate(PageState.simple(PARENT_GATE)); }
                })
        };
        addTileGrid(body, tiles, 2);
        setContentView(page);
    }

    private void showLanguage(final boolean arabic) {
        LinearLayout page = createPage(arabic ? "العربية" : "English", arabic);
        LinearLayout body = content(page);
        body.addView(label(arabic ? "اختر الدرس" : "Choose a lesson", 25, TEXT, Gravity.CENTER, true), fixed(dp(48), dp(2)));
        View[] tiles = arabic ? new View[] {
                lessonTile("أ", "الحروف العربية", "28 حرفًا مع صورة", PURPLE, LearningData.ARABIC_LETTERS, true),
                lessonTile("١", "الأرقام 1 - 10", "رقم واسم وصورة", BLUE, LearningData.ARABIC_NUMBERS, true),
                lessonTile("●", "الألوان", "ألوان قوية وواضحة", PINK, LearningData.ARABIC_COLORS, true),
                lessonTile("ح", "الحيوانات", "صور مبسطة", ORANGE, LearningData.ANIMALS_ARABIC, true)
        } : new View[] {
                lessonTile("A", "Capital Letters", "26 letters with pictures", BLUE, LearningData.ENGLISH_LETTERS, false),
                lessonTile("1", "Numbers 1 - 10", "Number, name and picture", GREEN, LearningData.ENGLISH_NUMBERS, false),
                lessonTile("●", "Colors", "Strong clear colors", PINK, LearningData.ENGLISH_COLORS, false),
                lessonTile("C", "Animals", "Friendly illustrations", ORANGE, LearningData.ANIMALS_ENGLISH, false)
        };
        addTileGrid(body, tiles, 2);
        setContentView(page);
    }

    private View lessonTile(String icon, final String title, String subtitle, int color, final LearnItem[] items, final boolean rtl) {
        return menuTile(icon, title, subtitle, color, new View.OnClickListener() {
            @Override public void onClick(View v) { tap(); navigate(PageState.lesson(title, items, rtl)); }
        });
    }

    private void showLesson() {
        final LearnItem item = current.items[current.index];
        LinearLayout page = createPage(current.title, current.rtl);
        LinearLayout body = content(page);

        LinearLayout card = vertical();
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(10), dp(8), dp(10), dp(8));
        card.setBackground(rounded(Color.WHITE, 25, item.accentColor, 3));
        body.addView(card, weighted(1f, dp(5)));

        View picture = pictureFor(item);
        card.addView(picture, weighted(1f, dp(2)));
        card.addView(label(item.title, item.title.length() <= 2 ? 59 : 36, item.accentColor, Gravity.CENTER, true), fixed(dp(68), 0));
        card.addView(label(displayWord(item), 25, TEXT, Gravity.CENTER, true), fixed(dp(40), 0));

        Button listen = button(current.rtl ? "استمع" : "Listen", item.accentColor, 19);
        listen.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { playItem(item); }
        });
        card.addView(listen, fixed(dp(50), dp(5)));

        LinearLayout nav = horizontal();
        nav.setGravity(Gravity.CENTER);
        Button previous = button(current.rtl ? "السابق  ←" : "←  Previous", BLUE, 16);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { moveItem(-1); }
        });
        Button next = button(current.rtl ? "التالي  →" : "Next  →", RED, 16);
        next.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { moveItem(1); }
        });
        nav.addView(previous, weightedHorizontal(1f, dp(4)));
        TextView counter = label((current.index + 1) + " / " + current.items.length, 16, PURPLE, Gravity.CENTER, true);
        counter.setBackground(rounded(Color.WHITE, 16, Color.rgb(199, 210, 254), 2));
        nav.addView(counter, new LinearLayout.LayoutParams(dp(82), dp(52)));
        nav.addView(next, weightedHorizontal(1f, dp(4)));
        body.addView(nav, fixed(dp(62), dp(4)));
        setContentView(page);
    }

    private void moveItem(int direction) {
        tap();
        current.index = (current.index + direction + current.items.length) % current.items.length;
        render();
        handler.postDelayed(new Runnable() {
            @Override public void run() { if (current.type == LESSON) playItem(current.items[current.index]); }
        }, 160);
    }

    private void showGames() {
        LinearLayout page = createPage("الألعاب التعليمية", true);
        LinearLayout body = content(page);
        body.addView(label("اختر اللعبة", 25, TEXT, Gravity.CENTER, true), fixed(dp(48), dp(2)));
        View[] tiles = new View[] {
                quizTile("أ", "الحروف العربية", new QuizConfig("لعبة الحروف العربية", "أين حرف", LearningData.ARABIC_LETTERS, true, PURPLE)),
                quizTile("١", "الأرقام العربية", new QuizConfig("لعبة الأرقام العربية", "أين الرقم", LearningData.ARABIC_NUMBERS, true, BLUE)),
                quizTile("A", "English Letters", new QuizConfig("English Letters Game", "Where is", LearningData.ENGLISH_LETTERS, false, GREEN)),
                quizTile("1", "English Numbers", new QuizConfig("English Numbers Game", "Where is", LearningData.ENGLISH_NUMBERS, false, ORANGE)),
                quizTile("ح", "الحيوانات", new QuizConfig("لعبة الحيوانات", "أين", LearningData.ANIMALS_ARABIC, true, PINK))
        };
        addTileGrid(body, tiles, 2);
        setContentView(page);
    }

    private View quizTile(String icon, String title, final QuizConfig config) {
        return menuTile(icon, title, "أربع إجابات في شاشة واحدة", config.color, new View.OnClickListener() {
            @Override public void onClick(View v) { startQuiz(config); }
        });
    }

    private void startQuiz(QuizConfig config) {
        tap();
        activeQuiz = config;
        currentScore = 0;
        lastTargetId = "";
        quizMessage = config.rtl ? "اختر الإجابة الصحيحة" : "Choose the correct answer";
        prepareQuestion();
        navigate(PageState.quiz(config));
    }

    private void prepareQuestion() {
        LearnItem target = activeQuiz.items[random.nextInt(activeQuiz.items.length)];
        int guard = 0;
        while (target.id.equals(lastTargetId) && activeQuiz.items.length > 1 && guard < 20) {
            target = activeQuiz.items[random.nextInt(activeQuiz.items.length)];
            guard++;
        }
        lastTargetId = target.id;
        quizTarget = target;
        ArrayList<LearnItem> others = new ArrayList<LearnItem>();
        for (LearnItem item : activeQuiz.items) if (!item.id.equals(target.id)) others.add(item);
        Collections.shuffle(others, random);
        quizOptions.clear();
        quizOptions.add(target);
        for (int i = 0; i < 3 && i < others.size(); i++) quizOptions.add(others.get(i));
        Collections.shuffle(quizOptions, random);
    }

    private void showQuiz() {
        final QuizConfig config = activeQuiz;
        LinearLayout page = createPage(config.title, config.rtl);
        LinearLayout body = content(page);

        LinearLayout question = vertical();
        question.setGravity(Gravity.CENTER);
        question.setPadding(dp(7), dp(4), dp(7), dp(4));
        question.setBackground(rounded(Color.WHITE, 22, lighten(config.color, .60f), 2));
        question.addView(label(config.prompt, 20, TEXT, Gravity.CENTER, true), fixed(dp(29), 0));

        TextView target = label(quizTarget.title, quizTarget.title.length() <= 2 ? 66 : 45, RED, Gravity.CENTER, true);
        question.addView(target, fixed(dp(76), 0));

        TextView message = label(quizMessage, 16, config.color, Gravity.CENTER, true);
        question.addView(message, fixed(dp(25), 0));
        question.addView(label("★ " + prefs.getInt("stars", 0) + "   ✓ " + currentScore + "   أعلى " + prefs.getInt("best_score", 0), 14, MUTED, Gravity.CENTER, true), fixed(dp(24), 0));
        body.addView(question, fixed(dp(164), dp(4)));

        LinearLayout answerGrid = vertical();
        for (int rowIndex = 0; rowIndex < 2; rowIndex++) {
            LinearLayout row = horizontal();
            row.setGravity(Gravity.CENTER);
            for (int column = 0; column < 2; column++) {
                int index = rowIndex * 2 + column;
                row.addView(answerCard(quizOptions.get(index), config), weightedHorizontal(1f, dp(4)));
            }
            answerGrid.addView(row, weighted(1f, dp(3)));
        }
        body.addView(answerGrid, weighted(1f, dp(3)));
        setContentView(page);
    }

    private View answerCard(final LearnItem item, final QuizConfig config) {
        LinearLayout card = vertical();
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(5), dp(5), dp(5), dp(5));
        card.setBackground(rounded(Color.WHITE, 21, item.accentColor, 3));

        if (item.illustrationType == LearnItem.TYPE_LETTER || item.illustrationType == LearnItem.TYPE_NUMBER) {
            TextView answer = label(item.title, 55, item.accentColor, Gravity.CENTER, true);
            card.addView(answer, weighted(1f, 0));
        } else {
            card.addView(pictureFor(item), weighted(1f, 0));
            card.addView(label(item.title, 24, TEXT, Gravity.CENTER, true), fixed(dp(37), 0));
        }
        card.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { answerQuiz(item, config); }
        });
        return card;
    }

    private void answerQuiz(LearnItem answer, final QuizConfig config) {
        if (answer.id.equals(quizTarget.id)) {
            int stars = prefs.getInt("stars", 0) + 1;
            int correct = prefs.getInt("correct_answers", 0) + 1;
            currentScore++;
            int best = Math.max(prefs.getInt("best_score", 0), currentScore);
            prefs.edit().putInt("stars", stars).putInt("correct_answers", correct).putInt("best_score", best).apply();
            quizMessage = config.rtl ? "أحسنت يا بطل!" : "Great job!";
            successTone();
            render();
            handler.postDelayed(new Runnable() {
                @Override public void run() {
                    if (current.type == QUIZ) {
                        quizMessage = config.rtl ? "اختر الإجابة الصحيحة" : "Choose the correct answer";
                        prepareQuestion();
                        render();
                    }
                }
            }, 650);
        } else {
            currentScore = 0;
            quizMessage = config.rtl ? "حاول مرة أخرى" : "Try again";
            wrongTone();
            render();
        }
    }

    private void showAchievements() {
        LinearLayout page = createPage("إنجازاتي", true);
        LinearLayout body = content(page);
        int stars = prefs.getInt("stars", 0);
        int correct = prefs.getInt("correct_answers", 0);
        int best = prefs.getInt("best_score", 0);

        LinearLayout panel = vertical();
        panel.setGravity(Gravity.CENTER);
        panel.setPadding(dp(18), dp(12), dp(18), dp(12));
        panel.setBackground(gradient(GOLD, ORANGE, 28));
        panel.addView(label(stars >= 100 ? "بطل التعلم" : stars >= 50 ? "الميدالية الذهبية" : stars >= 25 ? "الميدالية الفضية" : stars >= 10 ? "الميدالية البرونزية" : "ابدأ واجمع النجوم", 26, Color.WHITE, Gravity.CENTER, true), fixed(dp(55), 0));
        panel.addView(statRow("★", "النجوم", stars, Color.WHITE), fixed(dp(68), dp(4)));
        panel.addView(statRow("✓", "الإجابات الصحيحة", correct, Color.WHITE), fixed(dp(68), dp(4)));
        panel.addView(statRow("أ", "أعلى نتيجة", best, Color.WHITE), fixed(dp(68), dp(4)));

        Button reset = button("تصفير الإنجازات", Color.rgb(153, 27, 27), 17);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                new AlertDialog.Builder(CompactMainActivity.this)
                        .setTitle("تصفير الإنجازات")
                        .setMessage("هل تريد حذف النجوم والنتائج؟")
                        .setNegativeButton("إلغاء", null)
                        .setPositiveButton("تصفير", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) { prefs.edit().clear().apply(); render(); }
                        }).show();
            }
        });
        panel.addView(reset, fixed(dp(53), dp(8)));
        body.addView(panel, weighted(1f, dp(8)));
        setContentView(page);
    }

    private void showParentGate() {
        LinearLayout page = createPage("قسم الوالدين", true);
        LinearLayout body = content(page);
        body.setGravity(Gravity.CENTER);

        LinearLayout panel = vertical();
        panel.setGravity(Gravity.CENTER);
        panel.setPadding(dp(20), dp(18), dp(20), dp(18));
        panel.setBackground(rounded(Color.WHITE, 28, PINK, 3));
        panel.addView(label("تسجيل صوت الأسرة", 29, TEXT, Gravity.CENTER, true), fixed(dp(56), 0));
        panel.addView(label("الرمز الافتراضي 1234", 17, MUTED, Gravity.CENTER, true), fixed(dp(40), 0));
        final EditText pin = new EditText(this);
        pin.setSingleLine(true);
        pin.setGravity(Gravity.CENTER);
        pin.setTextSize(25);
        pin.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        pin.setHint("رمز الدخول");
        pin.setBackground(rounded(BG, 18, Color.rgb(199, 210, 254), 2));
        panel.addView(pin, fixed(dp(58), dp(8)));
        Button enter = button("دخول", PINK, 19);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if ("1234".equals(pin.getText().toString().trim())) navigate(PageState.simple(PARENT_GROUPS));
                else { wrongTone(); Toast.makeText(CompactMainActivity.this, "الرمز غير صحيح", Toast.LENGTH_SHORT).show(); }
            }
        });
        panel.addView(enter, fixed(dp(55), dp(8)));
        body.addView(panel, fixed(dp(310), dp(10)));
        setContentView(page);
    }

    private void showParentGroups() {
        LinearLayout page = createPage("تسجيل الأصوات", true);
        LinearLayout body = content(page);
        body.addView(label("اختر المجموعة", 24, TEXT, Gravity.CENTER, true), fixed(dp(46), dp(2)));
        View[] tiles = new View[] {
                recordingTile("أ", "الحروف العربية", LearningData.ARABIC_LETTERS, true, PURPLE),
                recordingTile("١", "الأرقام العربية", LearningData.ARABIC_NUMBERS, true, BLUE),
                recordingTile("A", "English Letters", LearningData.ENGLISH_LETTERS, false, GREEN),
                recordingTile("1", "English Numbers", LearningData.ENGLISH_NUMBERS, false, ORANGE),
                recordingTile("ح", "الحيوانات", LearningData.ANIMALS_ARABIC, true, PINK)
        };
        addTileGrid(body, tiles, 2);
        setContentView(page);
    }

    private View recordingTile(String icon, final String title, final LearnItem[] items, final boolean rtl, int color) {
        return menuTile(icon, title, "تسجيل لكل بطاقة", color, new View.OnClickListener() {
            @Override public void onClick(View v) { navigate(PageState.recordings(title, items, rtl)); }
        });
    }

    private void showRecordingItem() {
        final LearnItem item = current.items[current.index];
        final File file = recordingFile(item.id);
        final boolean exists = file.exists() && file.length() > 0;
        final boolean active = item.id.equals(activeRecordingId) && recorder != null;

        LinearLayout page = createPage(current.title, current.rtl);
        LinearLayout body = content(page);
        LinearLayout card = vertical();
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(9), dp(7), dp(9), dp(7));
        card.setBackground(rounded(Color.WHITE, 25, item.accentColor, 3));
        card.addView(pictureFor(item), weighted(1f, dp(2)));
        card.addView(label(item.title, item.title.length() <= 2 ? 50 : 31, item.accentColor, Gravity.CENTER, true), fixed(dp(60), 0));
        card.addView(label(displayWord(item), 21, TEXT, Gravity.CENTER, true), fixed(dp(35), 0));
        card.addView(label(active ? "جاري التسجيل..." : exists ? "تم حفظ تسجيل" : "لا يوجد تسجيل", 15, active ? RED : exists ? GREEN : MUTED, Gravity.CENTER, true), fixed(dp(28), 0));

        LinearLayout controls = horizontal();
        Button record = button(active ? "إيقاف وحفظ" : exists ? "إعادة التسجيل" : "تسجيل", active ? RED : item.accentColor, 14);
        record.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { if (active) { stopRecording(true); render(); } else beginRecording(item); }
        });
        controls.addView(record, weightedHorizontal(1f, dp(3)));
        Button play = button("تشغيل", BLUE, 14);
        play.setEnabled(exists && !active);
        play.setAlpha(play.isEnabled() ? 1f : .42f);
        play.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { playFile(file); } });
        controls.addView(play, weightedHorizontal(1f, dp(3)));
        Button delete = button("حذف", Color.rgb(153, 27, 27), 14);
        delete.setEnabled(exists && !active);
        delete.setAlpha(delete.isEnabled() ? 1f : .42f);
        delete.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { if (file.exists()) file.delete(); render(); } });
        controls.addView(delete, weightedHorizontal(1f, dp(3)));
        card.addView(controls, fixed(dp(53), dp(5)));
        body.addView(card, weighted(1f, dp(5)));

        LinearLayout nav = horizontal();
        Button previous = button("السابق ←", BLUE, 15);
        previous.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { moveRecording(-1); } });
        Button next = button("التالي →", RED, 15);
        next.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { moveRecording(1); } });
        nav.addView(previous, weightedHorizontal(1f, dp(4)));
        nav.addView(label((current.index + 1) + " / " + current.items.length, 16, PURPLE, Gravity.CENTER, true), new LinearLayout.LayoutParams(dp(82), dp(50)));
        nav.addView(next, weightedHorizontal(1f, dp(4)));
        body.addView(nav, fixed(dp(60), dp(4)));
        setContentView(page);
    }

    private void moveRecording(int direction) {
        stopRecordingIfNeeded();
        current.index = (current.index + direction + current.items.length) % current.items.length;
        render();
    }

    private View pictureFor(LearnItem item) {
        if (item.illustrationType >= LearnItem.TYPE_LION) return new IllustrationView(this, item);
        return new WordPictureView(this, item, displayWord(item));
    }

    private String displayWord(LearnItem item) {
        if ("ar_letter_11".equals(item.id)) return "سيارة";
        return item.subtitle == null ? "" : item.subtitle;
    }

    private String speakText(LearnItem item) {
        if ("ar_letter_11".equals(item.id)) return "حرف س. سيارة";
        return item.speakText;
    }

    private void playItem(LearnItem item) {
        File custom = recordingFile(item.id);
        if (custom.exists() && custom.length() > 0) { playFile(custom); return; }
        if (!ttsReady) { tap(); return; }
        Locale locale = "en".equals(item.locale) ? Locale.ENGLISH : new Locale("ar");
        int result = tts.setLanguage(locale);
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Toast.makeText(this, "استخدم تسجيل الوالدين لهذه اللغة", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
        tts.speak(speakText(item), TextToSpeech.QUEUE_FLUSH, params);
        tap();
    }

    private void beginRecording(LearnItem item) {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            pendingRecordingItem = item;
            requestPermissions(new String[] { Manifest.permission.RECORD_AUDIO }, REQUEST_RECORD_AUDIO);
            return;
        }
        startRecording(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO && pendingRecordingItem != null) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LearnItem item = pendingRecordingItem;
                pendingRecordingItem = null;
                startRecording(item);
            } else {
                pendingRecordingItem = null;
                Toast.makeText(this, "يجب السماح باستخدام الميكروفون", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startRecording(LearnItem item) {
        stopPlayback();
        stopRecordingIfNeeded();
        File file = recordingFile(item.id);
        if (file.exists()) file.delete();
        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(file.getAbsolutePath());
            recorder.prepare();
            recorder.start();
            activeRecordingId = item.id;
            render();
        } catch (Exception e) {
            if (recorder != null) try { recorder.release(); } catch (Exception ignored) {}
            recorder = null;
            activeRecordingId = "";
            file.delete();
            Toast.makeText(this, "تعذر بدء التسجيل", Toast.LENGTH_LONG).show();
        }
    }

    private boolean stopRecording(boolean notify) {
        if (recorder == null) return false;
        File file = recordingFile(activeRecordingId);
        boolean saved = false;
        try {
            recorder.stop();
            saved = file.exists() && file.length() > 0;
        } catch (RuntimeException e) {
            file.delete();
        } finally {
            try { recorder.release(); } catch (Exception ignored) {}
            recorder = null;
            activeRecordingId = "";
        }
        if (notify) Toast.makeText(this, saved ? "تم حفظ التسجيل" : "لم يتم حفظ التسجيل", Toast.LENGTH_SHORT).show();
        return saved;
    }

    private void stopRecordingIfNeeded() { if (recorder != null) stopRecording(false); }

    private File recordingFile(String id) {
        File dir = new File(getFilesDir(), "parent_recordings");
        if (!dir.exists()) dir.mkdirs();
        String safe;
        try { safe = Base64.encodeToString(id.getBytes("UTF-8"), Base64.URL_SAFE | Base64.NO_WRAP).replace("=", ""); }
        catch (UnsupportedEncodingException e) { safe = String.valueOf(id.hashCode()); }
        return new File(dir, safe + ".3gp");
    }

    private void playFile(File file) {
        if (file == null || !file.exists()) return;
        stopPlayback();
        try {
            player = new MediaPlayer();
            player.setDataSource(file.getAbsolutePath());
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override public void onCompletion(MediaPlayer mp) { mp.release(); if (player == mp) player = null; }
            });
        } catch (Exception e) { stopPlayback(); }
    }

    private void stopPlayback() {
        if (ttsReady) tts.stop();
        if (player != null) {
            try { player.stop(); } catch (Exception ignored) {}
            try { player.release(); } catch (Exception ignored) {}
            player = null;
        }
    }

    private LinearLayout createPage(String title, boolean rtl) {
        LinearLayout page = vertical();
        page.setBackground(gradient(BG, Color.rgb(255, 247, 237), 0));
        page.setLayoutDirection(rtl ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        LinearLayout bar = horizontal();
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(dp(8), dp(7), dp(8), dp(7));
        bar.setBackground(gradient(PURPLE, BLUE, 0));
        if (!backStack.isEmpty()) {
            Button back = button(rtl ? "رجوع" : "Back", Color.argb(55, 255, 255, 255), 14);
            back.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { onBackPressed(); } });
            bar.addView(back, new LinearLayout.LayoutParams(dp(76), dp(43)));
        }
        bar.addView(label(title, 22, Color.WHITE, Gravity.CENTER, true), weightedHorizontal(1f, dp(2)));
        if (!backStack.isEmpty()) bar.addView(new View(this), new LinearLayout.LayoutParams(dp(76), dp(43)));
        page.addView(bar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(58)));
        return page;
    }

    private LinearLayout content(LinearLayout page) {
        LinearLayout body = vertical();
        body.setPadding(dp(8), dp(7), dp(8), dp(7));
        page.addView(body, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
        return body;
    }

    private View menuTile(String icon, String title, String subtitle, int color, View.OnClickListener listener) {
        LinearLayout tile = vertical();
        tile.setGravity(Gravity.CENTER);
        tile.setPadding(dp(7), dp(7), dp(7), dp(7));
        tile.setBackground(gradient(color, darken(color, .18f), 22));
        TextView iconView = label(icon, icon.length() > 2 ? 19 : 35, Color.WHITE, Gravity.CENTER, true);
        iconView.setBackground(rounded(Color.argb(45, 255, 255, 255), 18, Color.argb(110, 255, 255, 255), 2));
        tile.addView(iconView, fixed(dp(58), dp(2)));
        TextView titleView = label(title, 18, Color.WHITE, Gravity.CENTER, true);
        titleView.setMaxLines(2);
        tile.addView(titleView, weighted(.58f, dp(1)));
        TextView subView = label(subtitle, 12, Color.WHITE, Gravity.CENTER, false);
        subView.setMaxLines(2);
        tile.addView(subView, weighted(.42f, dp(1)));
        tile.setOnClickListener(listener);
        return tile;
    }

    private void addTileGrid(LinearLayout body, View[] tiles, int columns) {
        LinearLayout grid = vertical();
        int rows = (tiles.length + columns - 1) / columns;
        int index = 0;
        for (int r = 0; r < rows; r++) {
            LinearLayout row = horizontal();
            for (int c = 0; c < columns; c++) {
                if (index < tiles.length) row.addView(tiles[index++], weightedHorizontal(1f, dp(4)));
                else row.addView(new View(this), weightedHorizontal(1f, dp(4)));
            }
            grid.addView(row, weighted(1f, dp(4)));
        }
        body.addView(grid, weighted(1f, dp(2)));
    }

    private View statRow(String icon, String title, int value, int color) {
        LinearLayout row = horizontal();
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(10), dp(7), dp(10), dp(7));
        row.setBackground(rounded(Color.argb(38, 255, 255, 255), 17, Color.argb(95, 255, 255, 255), 2));
        row.addView(label(icon, 25, color, Gravity.CENTER, true), new LinearLayout.LayoutParams(dp(46), dp(46)));
        row.addView(label(title, 18, color, Gravity.CENTER_VERTICAL, true), weightedHorizontal(1f, dp(3)));
        row.addView(label(String.valueOf(value), 25, color, Gravity.CENTER, true), new LinearLayout.LayoutParams(dp(64), dp(46)));
        return row;
    }

    private TextView label(String value, int size, int color, int gravity, boolean bold) {
        TextView text = new TextView(this);
        text.setText(value);
        text.setTextSize(size);
        text.setTextColor(color);
        text.setGravity(gravity | Gravity.CENTER_VERTICAL);
        text.setTypeface(Typeface.create("sans", bold ? Typeface.BOLD : Typeface.NORMAL));
        text.setLineSpacing(0f, 1.02f);
        return text;
    }

    private Button button(String value, int color, int size) {
        Button button = new Button(this);
        button.setText(value);
        button.setAllCaps(false);
        button.setTextSize(size);
        button.setTextColor(Color.WHITE);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setPadding(dp(4), dp(2), dp(4), dp(2));
        button.setBackground(rounded(color, 16, color, 1));
        return button;
    }

    private LinearLayout vertical() { LinearLayout l = new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); return l; }
    private LinearLayout horizontal() { LinearLayout l = new LinearLayout(this); l.setOrientation(LinearLayout.HORIZONTAL); return l; }

    private LinearLayout.LayoutParams fixed(int height, int margin) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        p.setMargins(margin, margin, margin, margin);
        return p;
    }

    private LinearLayout.LayoutParams weighted(float weight, int margin) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, weight);
        p.setMargins(margin, margin, margin, margin);
        return p;
    }

    private LinearLayout.LayoutParams weightedHorizontal(float weight, int margin) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, weight);
        p.setMargins(margin, margin, margin, margin);
        return p;
    }

    private GradientDrawable rounded(int fill, int radius, int stroke, int strokeWidth) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(fill);
        d.setCornerRadius(dp(radius));
        d.setStroke(dp(strokeWidth), stroke);
        return d;
    }

    private GradientDrawable gradient(int start, int end, int radius) {
        GradientDrawable d = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[] { start, end });
        d.setCornerRadius(dp(radius));
        return d;
    }

    private int lighten(int color, float amount) {
        return Color.rgb(Math.min(255, (int)(Color.red(color) + (255 - Color.red(color)) * amount)), Math.min(255, (int)(Color.green(color) + (255 - Color.green(color)) * amount)), Math.min(255, (int)(Color.blue(color) + (255 - Color.blue(color)) * amount)));
    }

    private int darken(int color, float amount) {
        return Color.rgb(Math.max(0, (int)(Color.red(color) * (1f - amount))), Math.max(0, (int)(Color.green(color) * (1f - amount))), Math.max(0, (int)(Color.blue(color) * (1f - amount))));
    }

    private int dp(int value) { return Math.round(value * getResources().getDisplayMetrics().density); }
    private void tap() { if (tones != null) tones.startTone(ToneGenerator.TONE_PROP_BEEP, 45); }
    private void successTone() { if (tones != null) tones.startTone(ToneGenerator.TONE_PROP_ACK, 150); }
    private void wrongTone() { if (tones != null) tones.startTone(ToneGenerator.TONE_PROP_NACK, 150); }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        stopRecordingIfNeeded();
        stopPlayback();
        if (tts != null) { tts.shutdown(); tts = null; }
        if (tones != null) { tones.release(); tones = null; }
        super.onDestroy();
    }

    private static final class QuizConfig {
        final String title;
        final String prompt;
        final LearnItem[] items;
        final boolean rtl;
        final int color;
        QuizConfig(String title, String prompt, LearnItem[] items, boolean rtl, int color) {
            this.title = title; this.prompt = prompt; this.items = items; this.rtl = rtl; this.color = color;
        }
    }

    private static final class PageState {
        final int type;
        final String title;
        final LearnItem[] items;
        final boolean rtl;
        final QuizConfig quiz;
        int index;

        PageState(int type, String title, LearnItem[] items, boolean rtl, QuizConfig quiz, int index) {
            this.type = type; this.title = title; this.items = items; this.rtl = rtl; this.quiz = quiz; this.index = index;
        }
        static PageState simple(int type) { return new PageState(type, "", null, true, null, 0); }
        static PageState lesson(String title, LearnItem[] items, boolean rtl) { return new PageState(LESSON, title, items, rtl, null, 0); }
        static PageState quiz(QuizConfig quiz) { return new PageState(QUIZ, quiz.title, quiz.items, quiz.rtl, quiz, 0); }
        static PageState recordings(String title, LearnItem[] items, boolean rtl) { return new PageState(RECORDINGS, title, items, rtl, null, 0); }
        PageState copy() { return new PageState(type, title, items, rtl, quiz, index); }
    }
}
