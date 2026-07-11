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
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
    private static final int SCREEN_HOME = 0;
    private static final int SCREEN_ARABIC = 1;
    private static final int SCREEN_ENGLISH = 2;
    private static final int SCREEN_LEARN = 3;
    private static final int SCREEN_GAMES = 4;
    private static final int SCREEN_QUIZ = 5;
    private static final int SCREEN_ACHIEVEMENTS = 6;
    private static final int SCREEN_PARENT_GATE = 7;
    private static final int SCREEN_PARENT_GROUPS = 8;
    private static final int SCREEN_RECORDINGS = 9;

    private static final int REQUEST_RECORD_AUDIO = 8105;

    private static final int PURPLE = Color.rgb(108, 99, 255);
    private static final int BLUE = Color.rgb(65, 137, 230);
    private static final int GREEN = Color.rgb(49, 167, 117);
    private static final int ORANGE = Color.rgb(245, 133, 55);
    private static final int PINK = Color.rgb(224, 91, 145);
    private static final int GOLD = Color.rgb(230, 173, 35);
    private static final int TEXT_DARK = Color.rgb(42, 45, 55);
    private static final int MUTED = Color.rgb(95, 100, 112);
    private static final int CREAM = Color.rgb(255, 248, 234);

    private final ArrayList<ScreenState> backStack = new ArrayList<ScreenState>();
    private final Random random = new Random();
    private final Handler handler = new Handler();

    private ScreenState currentScreen;
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
    private final ArrayList<LearnItem> quizOptions = new ArrayList<LearnItem>();
    private String quizMessage = "";
    private String lastTargetId = "";
    private int currentScore;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(88, 78, 220));
        }
        prefs = getSharedPreferences("kids_progress", Context.MODE_PRIVATE);
        tones = new ToneGenerator(AudioManager.STREAM_MUSIC, 58);
        tts = new TextToSpeech(this, this);
        currentScreen = ScreenState.home();
        render();
    }

    @Override
    public void onInit(int status) {
        ttsReady = status == TextToSpeech.SUCCESS;
        if (ttsReady) {
            tts.setSpeechRate(0.82f);
            tts.setPitch(1.05f);
        }
    }

    private void navigate(ScreenState next) {
        stopRecordingIfNeeded();
        if (currentScreen != null) backStack.add(currentScreen);
        currentScreen = next;
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
        currentScreen = backStack.remove(backStack.size() - 1);
        render();
    }

    private void render() {
        stopPlayback();
        switch (currentScreen.type) {
            case SCREEN_HOME: showHome(); break;
            case SCREEN_ARABIC: showArabicMenu(); break;
            case SCREEN_ENGLISH: showEnglishMenu(); break;
            case SCREEN_LEARN: showLearningGrid(currentScreen); break;
            case SCREEN_GAMES: showGamesMenu(); break;
            case SCREEN_QUIZ: showQuiz(currentScreen.quiz); break;
            case SCREEN_ACHIEVEMENTS: showAchievements(); break;
            case SCREEN_PARENT_GATE: showParentGate(); break;
            case SCREEN_PARENT_GROUPS: showParentGroups(); break;
            case SCREEN_RECORDINGS: showRecordingList(currentScreen); break;
            default: showHome(); break;
        }
    }

    private void showHome() {
        LinearLayout page = createPage("طفلي الأول", false);
        LinearLayout body = addScrollBody(page);

        TextView title = label("تعلّم والعب", 36, TEXT_DARK, Gravity.CENTER, true);
        title.setPadding(dp(12), dp(18), dp(12), dp(3));
        body.addView(title);
        TextView subtitle = label("رحلة تعليمية ممتعة تعمل دون إنترنت", 18, MUTED, Gravity.CENTER, false);
        subtitle.setPadding(dp(12), 0, dp(12), dp(18));
        body.addView(subtitle);

        LinearLayout progress = horizontal();
        progress.setGravity(Gravity.CENTER);
        progress.setPadding(dp(12), dp(14), dp(12), dp(14));
        progress.setBackground(rounded(Color.WHITE, 24, Color.rgb(237, 231, 255)));
        addWithMargins(body, progress, dp(14), dp(4), dp(14), dp(18), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progress.addView(statChip("★", "النجوم", prefs.getInt("stars", 0), GOLD), weighted());
        progress.addView(statChip("✓", "الصحيحة", prefs.getInt("correct_answers", 0), GREEN), weighted());
        progress.addView(statChip("🏆", "الأعلى", prefs.getInt("best_score", 0), PURPLE), weighted());

        body.addView(menuCard("ع", "العربية", "حروف، أرقام، ألوان وحيوانات", PURPLE, new View.OnClickListener() {
            @Override public void onClick(View v) { click(); navigate(ScreenState.simple(SCREEN_ARABIC)); }
        }));
        body.addView(menuCard("A", "English", "Letters, numbers, colors and animals", BLUE, new View.OnClickListener() {
            @Override public void onClick(View v) { click(); navigate(ScreenState.simple(SCREEN_ENGLISH)); }
        }));
        body.addView(menuCard("ح", "الحيوانات", "تعرف إلى الحيوانات بالعربي والإنجليزي", ORANGE, new View.OnClickListener() {
            @Override public void onClick(View v) {
                click();
                navigate(ScreenState.learn("الحيوانات", LearningData.ANIMALS_ARABIC, true));
            }
        }));
        body.addView(menuCard("?", "الألعاب التعليمية", "أسئلة عشوائية ونجوم ومكافآت", GREEN, new View.OnClickListener() {
            @Override public void onClick(View v) { click(); navigate(ScreenState.simple(SCREEN_GAMES)); }
        }));
        body.addView(menuCard("★", "إنجازاتي", "شاهد النجوم والميداليات وأعلى نتيجة", GOLD, new View.OnClickListener() {
            @Override public void onClick(View v) { click(); navigate(ScreenState.simple(SCREEN_ACHIEVEMENTS)); }
        }));
        body.addView(menuCard("صوت", "قسم الوالدين", "سجل صوت الأب أو الأم لكل بطاقة", PINK, new View.OnClickListener() {
            @Override public void onClick(View v) { click(); navigate(ScreenState.simple(SCREEN_PARENT_GATE)); }
        }));

        finishPage(page);
    }

    private void showArabicMenu() {
        LinearLayout page = createPage("العربية", true);
        LinearLayout body = addScrollBody(page);
        addSectionIntro(body, "اختر ما تريد تعلّمه", "اضغط على أي بطاقة لسماعها");
        body.addView(menuCard("أ", "الحروف العربية", "28 حرفًا مع كلمة توضيحية", PURPLE, new View.OnClickListener() {
            @Override public void onClick(View v) { navigate(ScreenState.learn("الحروف العربية", LearningData.ARABIC_LETTERS, true)); }
        }));
        body.addView(menuCard("١", "الأرقام 1 - 10", "العدد والاسم بطريقة سهلة", BLUE, new View.OnClickListener() {
            @Override public void onClick(View v) { navigate(ScreenState.learn("الأرقام العربية", LearningData.ARABIC_NUMBERS, true)); }
        }));
        body.addView(menuCard("●", "الألوان", "ثمانية ألوان أساسية", PINK, new View.OnClickListener() {
            @Override public void onClick(View v) { navigate(ScreenState.learn("الألوان", LearningData.ARABIC_COLORS, true)); }
        }));
        body.addView(menuCard("ح", "الحيوانات", "رسومات مبسطة وأسماء ثنائية اللغة", ORANGE, new View.OnClickListener() {
            @Override public void onClick(View v) { navigate(ScreenState.learn("الحيوانات", LearningData.ANIMALS_ARABIC, true)); }
        }));
        finishPage(page);
    }

    private void showEnglishMenu() {
        LinearLayout page = createPage("English", false);
        LinearLayout body = addScrollBody(page);
        addSectionIntro(body, "Choose a lesson", "Tap a card to hear it");
        body.addView(menuCard("A", "Capital Letters", "26 letters with easy words", BLUE, new View.OnClickListener() {
            @Override public void onClick(View v) { navigate(ScreenState.learn("Capital Letters", LearningData.ENGLISH_LETTERS, false)); }
        }));
        body.addView(menuCard("1", "Numbers 1 - 10", "Learn the number and its name", GREEN, new View.OnClickListener() {
            @Override public void onClick(View v) { navigate(ScreenState.learn("English Numbers", LearningData.ENGLISH_NUMBERS, false)); }
        }));
        body.addView(menuCard("●", "Colors", "Eight basic colors", PINK, new View.OnClickListener() {
            @Override public void onClick(View v) { navigate(ScreenState.learn("Colors", LearningData.ENGLISH_COLORS, false)); }
        }));
        body.addView(menuCard("C", "Animals", "Friendly drawings and bilingual names", ORANGE, new View.OnClickListener() {
            @Override public void onClick(View v) { navigate(ScreenState.learn("Animals", LearningData.ANIMALS_ENGLISH, false)); }
        }));
        finishPage(page);
    }

    private void showLearningGrid(final ScreenState screen) {
        LinearLayout page = createPage(screen.title, screen.rtl);
        LinearLayout body = addScrollBody(page);
        TextView hint = label(screen.rtl ? "اضغط على البطاقة للاستماع" : "Tap a card to listen", 17, MUTED, Gravity.CENTER, true);
        hint.setPadding(dp(8), dp(12), dp(8), dp(10));
        body.addView(hint);

        for (int i = 0; i < screen.items.length; i += 2) {
            LinearLayout row = horizontal();
            row.setGravity(Gravity.TOP);
            LearnItem left = screen.items[i];
            row.addView(learnCard(left, screen.rtl), cardParams());
            if (i + 1 < screen.items.length) {
                row.addView(learnCard(screen.items[i + 1], screen.rtl), cardParams());
            } else {
                View spacer = new View(this);
                row.addView(spacer, cardParams());
            }
            body.addView(row);
        }
        finishPage(page);
    }

    private View learnCard(final LearnItem item, boolean rtl) {
        LinearLayout card = vertical();
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(8), dp(8), dp(8), dp(8));
        card.setBackground(rounded(Color.WHITE, 24, lighten(item.accentColor, 0.82f)));

        IllustrationView art = new IllustrationView(this, item);
        card.addView(art, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(112)));
        TextView name = label(item.title, item.title.length() <= 2 ? 31 : 24, TEXT_DARK, Gravity.CENTER, true);
        name.setPadding(dp(4), dp(2), dp(4), 0);
        card.addView(name);
        if (item.subtitle != null && item.subtitle.length() > 0) {
            TextView sub = label(item.subtitle, 17, MUTED, Gravity.CENTER, true);
            sub.setPadding(dp(2), 0, dp(2), dp(2));
            card.addView(sub);
        }
        TextView hear = label(rtl ? "استمع" : "Listen", 13, item.accentColor, Gravity.CENTER, true);
        card.addView(hear);
        card.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                animatePress(v);
                playItem(item);
            }
        });
        return card;
    }

    private void showGamesMenu() {
        LinearLayout page = createPage("الألعاب التعليمية", true);
        LinearLayout body = addScrollBody(page);
        addSectionIntro(body, "اختر اللعبة", "كل إجابة صحيحة تمنح الطفل نجمة");
        addQuizMenu(body, new QuizConfig("لعبة الحروف العربية", "أين حرف", LearningData.ARABIC_LETTERS, true, PURPLE));
        addQuizMenu(body, new QuizConfig("لعبة الأرقام العربية", "أين الرقم", LearningData.ARABIC_NUMBERS, true, BLUE));
        addQuizMenu(body, new QuizConfig("English Letters Game", "Where is", LearningData.ENGLISH_LETTERS, false, GREEN));
        addQuizMenu(body, new QuizConfig("English Numbers Game", "Where is", LearningData.ENGLISH_NUMBERS, false, ORANGE));
        addQuizMenu(body, new QuizConfig("لعبة الحيوانات", "أين", LearningData.ANIMALS_ARABIC, true, PINK));
        finishPage(page);
    }

    private void addQuizMenu(LinearLayout body, final QuizConfig config) {
        body.addView(menuCard("?", config.title, config.rtl ? "أربعة اختيارات في كل سؤال" : "Four choices in every question", config.color, new View.OnClickListener() {
            @Override public void onClick(View v) {
                startQuiz(config);
            }
        }));
    }

    private void startQuiz(QuizConfig config) {
        activeQuiz = config;
        currentScore = 0;
        lastTargetId = "";
        quizMessage = config.rtl ? "اختر الإجابة الصحيحة" : "Choose the correct answer";
        prepareQuestion();
        navigate(ScreenState.quiz(config));
    }

    private void prepareQuestion() {
        if (activeQuiz == null || activeQuiz.items.length == 0) return;
        LearnItem target = activeQuiz.items[random.nextInt(activeQuiz.items.length)];
        int guard = 0;
        while (target.id.equals(lastTargetId) && activeQuiz.items.length > 1 && guard < 20) {
            target = activeQuiz.items[random.nextInt(activeQuiz.items.length)];
            guard++;
        }
        lastTargetId = target.id;
        quizTarget = target;
        ArrayList<LearnItem> others = new ArrayList<LearnItem>();
        for (LearnItem item : activeQuiz.items) {
            if (!item.id.equals(target.id)) others.add(item);
        }
        Collections.shuffle(others, random);
        quizOptions.clear();
        quizOptions.add(target);
        for (int i = 0; i < others.size() && i < 3; i++) quizOptions.add(others.get(i));
        Collections.shuffle(quizOptions, random);
    }

    private void showQuiz(final QuizConfig config) {
        if (activeQuiz != config || quizTarget == null) {
            activeQuiz = config;
            quizMessage = config.rtl ? "اختر الإجابة الصحيحة" : "Choose the correct answer";
            prepareQuestion();
        }
        LinearLayout page = createPage(config.title, config.rtl);
        LinearLayout body = addScrollBody(page);

        LinearLayout questionCard = vertical();
        questionCard.setPadding(dp(14), dp(16), dp(14), dp(16));
        questionCard.setBackground(rounded(Color.WHITE, 24, lighten(config.color, 0.78f)));
        addWithMargins(body, questionCard, dp(14), dp(12), dp(14), dp(14), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        String q = config.prompt + " " + quizTarget.title + (config.rtl ? "؟" : "?");
        questionCard.addView(label(q, 29, TEXT_DARK, Gravity.CENTER, true));
        TextView message = label(quizMessage, 18, config.color, Gravity.CENTER, true);
        message.setPadding(0, dp(7), 0, dp(4));
        questionCard.addView(message);
        questionCard.addView(label("★ " + prefs.getInt("stars", 0) + "    ✓ " + currentScore + "    🏆 " + prefs.getInt("best_score", 0), 18, MUTED, Gravity.CENTER, true));

        for (int i = 0; i < quizOptions.size(); i += 2) {
            LinearLayout row = horizontal();
            final LearnItem first = quizOptions.get(i);
            row.addView(quizCard(first, config), cardParams());
            if (i + 1 < quizOptions.size()) {
                final LearnItem second = quizOptions.get(i + 1);
                row.addView(quizCard(second, config), cardParams());
            } else {
                row.addView(new View(this), cardParams());
            }
            body.addView(row);
        }
        finishPage(page);
    }

    private View quizCard(final LearnItem item, final QuizConfig config) {
        LinearLayout card = vertical();
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(8), dp(8), dp(8), dp(8));
        card.setBackground(rounded(Color.WHITE, 24, lighten(item.accentColor, 0.82f)));
        card.addView(new IllustrationView(this, item), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(108)));
        card.addView(label(item.title, item.title.length() <= 2 ? 30 : 23, TEXT_DARK, Gravity.CENTER, true));
        if (item.subtitle != null && item.subtitle.length() > 0 && item.illustrationType >= LearnItem.TYPE_LION) {
            card.addView(label(item.subtitle, 16, MUTED, Gravity.CENTER, true));
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
                    if (currentScreen != null && currentScreen.type == SCREEN_QUIZ) {
                        quizMessage = config.rtl ? "اختر الإجابة الصحيحة" : "Choose the correct answer";
                        prepareQuestion();
                        render();
                    }
                }
            }, 750);
        } else {
            currentScore = 0;
            quizMessage = config.rtl ? "حاول مرة أخرى" : "Try again";
            wrongTone();
            render();
        }
    }

    private void showAchievements() {
        LinearLayout page = createPage("إنجازاتي", true);
        LinearLayout body = addScrollBody(page);
        int stars = prefs.getInt("stars", 0);
        int best = prefs.getInt("best_score", 0);
        int correct = prefs.getInt("correct_answers", 0);

        LinearLayout award = vertical();
        award.setGravity(Gravity.CENTER);
        award.setPadding(dp(18), dp(24), dp(18), dp(24));
        award.setBackground(rounded(Color.WHITE, 28, Color.rgb(244, 232, 180)));
        addWithMargins(body, award, dp(16), dp(18), dp(16), dp(16), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView medalIcon = label(stars >= 100 ? "♛" : stars >= 50 ? "★" : stars >= 10 ? "●" : "☆", 76, GOLD, Gravity.CENTER, true);
        award.addView(medalIcon);
        award.addView(label(medalName(stars), 28, TEXT_DARK, Gravity.CENTER, true));
        TextView next = label(nextReward(stars), 17, MUTED, Gravity.CENTER, false);
        next.setPadding(dp(8), dp(7), dp(8), dp(16));
        award.addView(next);
        award.addView(achievementRow("★", "النجوم", stars, GOLD));
        award.addView(achievementRow("✓", "الإجابات الصحيحة", correct, GREEN));
        award.addView(achievementRow("🔥", "أعلى نتيجة متتالية", best, ORANGE));

        Button reset = actionButton("تصفير الإنجازات", Color.rgb(180, 72, 72));
        reset.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("تصفير الإنجازات")
                        .setMessage("هل تريد حذف النجوم والنتائج المحفوظة؟")
                        .setNegativeButton("إلغاء", null)
                        .setPositiveButton("تصفير", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                prefs.edit().clear().apply();
                                click();
                                render();
                            }
                        }).show();
            }
        });
        addWithMargins(body, reset, dp(24), dp(8), dp(24), dp(24), ViewGroup.LayoutParams.MATCH_PARENT, dp(52));
        finishPage(page);
    }

    private String medalName(int stars) {
        if (stars >= 100) return "بطل التعلم";
        if (stars >= 50) return "الميدالية الذهبية";
        if (stars >= 25) return "الميدالية الفضية";
        if (stars >= 10) return "الميدالية البرونزية";
        return "ابدأ رحلتك واجمع النجوم";
    }

    private String nextReward(int stars) {
        if (stars >= 100) return "وصلت إلى أعلى مستوى، أحسنت!";
        int target = stars < 10 ? 10 : stars < 25 ? 25 : stars < 50 ? 50 : 100;
        return "باقي " + (target - stars) + " نجمة للجائزة التالية";
    }

    private void showParentGate() {
        LinearLayout page = createPage("قسم الوالدين", true);
        LinearLayout body = addScrollBody(page);
        addSectionIntro(body, "تسجيل صوت الأسرة", "سجّل نطق الأب أو الأم، وسيستخدمه التطبيق بدل الصوت الآلي");

        final EditText pin = new EditText(this);
        pin.setHint("رمز الدخول");
        pin.setTextSize(24);
        pin.setGravity(Gravity.CENTER);
        pin.setSingleLine(true);
        pin.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        pin.setBackground(rounded(Color.WHITE, 18, Color.rgb(225, 218, 245)));
        pin.setPadding(dp(14), dp(10), dp(14), dp(10));
        addWithMargins(body, pin, dp(28), dp(10), dp(28), dp(14), ViewGroup.LayoutParams.MATCH_PARENT, dp(58));

        Button enter = actionButton("دخول - الرمز الافتراضي 1234", PURPLE);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if ("1234".equals(pin.getText().toString().trim())) {
                    click();
                    navigate(ScreenState.simple(SCREEN_PARENT_GROUPS));
                } else {
                    wrongTone();
                    Toast.makeText(MainActivity.this, "الرمز غير صحيح", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addWithMargins(body, enter, dp(24), 0, dp(24), dp(24), ViewGroup.LayoutParams.MATCH_PARENT, dp(56));
        finishPage(page);
    }

    private void showParentGroups() {
        LinearLayout page = createPage("تسجيل الأصوات", true);
        LinearLayout body = addScrollBody(page);
        addSectionIntro(body, "اختر المجموعة", "يمكن التسجيل والتشغيل وإعادة التسجيل والحذف لكل بطاقة");
        body.addView(recordingMenu("أ", "الحروف العربية", LearningData.ARABIC_LETTERS, true, PURPLE));
        body.addView(recordingMenu("١", "الأرقام العربية", LearningData.ARABIC_NUMBERS, true, BLUE));
        body.addView(recordingMenu("A", "English Letters", LearningData.ENGLISH_LETTERS, false, GREEN));
        body.addView(recordingMenu("1", "English Numbers", LearningData.ENGLISH_NUMBERS, false, ORANGE));
        body.addView(recordingMenu("ح", "الحيوانات", LearningData.ANIMALS_ARABIC, true, PINK));
        finishPage(page);
    }

    private View recordingMenu(String icon, final String title, final LearnItem[] items, final boolean rtl, int color) {
        return menuCard(icon, title, "تسجيل منفصل لكل بطاقة", color, new View.OnClickListener() {
            @Override public void onClick(View v) {
                navigate(ScreenState.recordings(title, items, rtl));
            }
        });
    }

    private void showRecordingList(final ScreenState screen) {
        LinearLayout page = createPage(screen.title, screen.rtl);
        LinearLayout body = addScrollBody(page);
        TextView tip = label(screen.rtl ? "اضغط تسجيل، انطق الكلمة، ثم اضغط إيقاف وحفظ" : "Tap Record, speak, then tap Stop and save", 16, MUTED, Gravity.CENTER, true);
        tip.setPadding(dp(14), dp(12), dp(14), dp(12));
        body.addView(tip);

        for (int i = 0; i < screen.items.length; i++) {
            final LearnItem item = screen.items[i];
            final File file = recordingFile(item.id);
            final boolean exists = file.exists() && file.length() > 0;
            final boolean active = item.id.equals(activeRecordingId) && recorder != null;

            LinearLayout card = vertical();
            card.setPadding(dp(12), dp(12), dp(12), dp(12));
            card.setBackground(rounded(Color.WHITE, 20, lighten(item.accentColor, 0.83f)));
            addWithMargins(body, card, dp(12), dp(5), dp(12), dp(7), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            LinearLayout header = horizontal();
            header.setGravity(Gravity.CENTER_VERTICAL);
            TextView badge = label(item.title, 24, Color.WHITE, Gravity.CENTER, true);
            badge.setBackground(rounded(item.accentColor, 18, item.accentColor));
            header.addView(badge, new LinearLayout.LayoutParams(dp(56), dp(56)));
            LinearLayout names = vertical();
            names.setPadding(dp(12), 0, dp(12), 0);
            names.addView(label(item.title, 22, TEXT_DARK, screen.rtl ? Gravity.RIGHT : Gravity.LEFT, true));
            if (item.subtitle != null && item.subtitle.length() > 0) names.addView(label(item.subtitle, 16, MUTED, screen.rtl ? Gravity.RIGHT : Gravity.LEFT, false));
            names.addView(label(active ? "جاري التسجيل..." : exists ? "تم تسجيل صوت" : "لا يوجد تسجيل", 14, active ? Color.RED : exists ? GREEN : MUTED, screen.rtl ? Gravity.RIGHT : Gravity.LEFT, true));
            header.addView(names, weighted());
            card.addView(header);

            LinearLayout buttons = horizontal();
            buttons.setPadding(0, dp(10), 0, 0);
            Button record = smallButton(active ? "إيقاف وحفظ" : exists ? "إعادة التسجيل" : "تسجيل", active ? Color.rgb(205, 66, 66) : item.accentColor);
            record.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (activeRecordingId.equals(item.id) && recorder != null) {
                        stopRecording(true);
                        render();
                    } else {
                        beginRecording(item);
                    }
                }
            });
            buttons.addView(record, weightedWithMargins(dp(3)));

            Button listen = smallButton("تشغيل", BLUE);
            listen.setEnabled(exists && !active);
            listen.setAlpha(listen.isEnabled() ? 1f : 0.45f);
            listen.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { playFile(file); }
            });
            buttons.addView(listen, weightedWithMargins(dp(3)));

            Button delete = smallButton("حذف", Color.rgb(145, 100, 100));
            delete.setEnabled(exists && !active);
            delete.setAlpha(delete.isEnabled() ? 1f : 0.45f);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (file.exists()) file.delete();
                    click();
                    render();
                }
            });
            buttons.addView(delete, weightedWithMargins(dp(3)));
            card.addView(buttons);
        }
        finishPage(page);
    }

    private void beginRecording(LearnItem item) {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            pendingRecordingItem = item;
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
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
                Toast.makeText(this, "يجب السماح باستخدام الميكروفون للتسجيل", Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, "بدأ التسجيل", Toast.LENGTH_SHORT).show();
            render();
        } catch (Exception e) {
            if (recorder != null) {
                try { recorder.release(); } catch (Exception ignored) {}
            }
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

    private void stopRecordingIfNeeded() {
        if (recorder != null) stopRecording(false);
    }

    private File recordingFile(String id) {
        File dir = new File(getFilesDir(), "parent_recordings");
        if (!dir.exists()) dir.mkdirs();
        String safe;
        try {
            safe = Base64.encodeToString(id.getBytes("UTF-8"), Base64.URL_SAFE | Base64.NO_WRAP).replace("=", "");
        } catch (UnsupportedEncodingException e) {
            safe = String.valueOf(id.hashCode());
        }
        return new File(dir, safe + ".3gp");
    }

    private void playItem(LearnItem item) {
        File custom = recordingFile(item.id);
        if (custom.exists() && custom.length() > 0) {
            playFile(custom);
            return;
        }
        if (!ttsReady) {
            click();
            Toast.makeText(this, "الصوت الآلي غير متاح، ويمكن تسجيل صوت من قسم الوالدين", Toast.LENGTH_SHORT).show();
            return;
        }
        Locale locale = "en".equals(item.locale) ? Locale.ENGLISH : new Locale("ar");
        int result = tts.setLanguage(locale);
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            click();
            Toast.makeText(this, "هذه اللغة غير متاحة في محرك النطق. استخدم تسجيل الوالدين.", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
        tts.speak(item.speakText, TextToSpeech.QUEUE_FLUSH, params);
        click();
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
                @Override public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    if (player == mp) player = null;
                }
            });
        } catch (Exception e) {
            stopPlayback();
            Toast.makeText(this, "تعذر تشغيل التسجيل", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlayback() {
        if (ttsReady) tts.stop();
        if (player != null) {
            try { player.stop(); } catch (Exception ignored) {}
            try { player.release(); } catch (Exception ignored) {}
            player = null;
        }
    }

    private void click() {
        if (tones != null) tones.startTone(ToneGenerator.TONE_PROP_BEEP, 55);
    }

    private void successTone() {
        if (tones != null) tones.startTone(ToneGenerator.TONE_PROP_ACK, 170);
    }

    private void wrongTone() {
        if (tones != null) tones.startTone(ToneGenerator.TONE_PROP_NACK, 170);
    }

    private LinearLayout createPage(String title, boolean rtl) {
        LinearLayout page = vertical();
        page.setBackground(gradient(CREAM, Color.rgb(243, 239, 255)));
        page.setLayoutDirection(rtl ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);

        LinearLayout bar = horizontal();
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(dp(10), dp(10), dp(10), dp(10));
        bar.setBackground(gradient(PURPLE, Color.rgb(83, 126, 223)));
        if (!backStack.isEmpty()) {
            Button back = new Button(this);
            back.setText(rtl ? "رجوع" : "Back");
            back.setTextSize(15);
            back.setTextColor(Color.WHITE);
            back.setAllCaps(false);
            back.setBackground(rounded(Color.argb(45, 255, 255, 255), 15, Color.argb(90, 255, 255, 255)));
            back.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { onBackPressed(); }
            });
            bar.addView(back, new LinearLayout.LayoutParams(dp(78), dp(44)));
        }
        TextView heading = label(title, 23, Color.WHITE, Gravity.CENTER, true);
        bar.addView(heading, weighted());
        if (!backStack.isEmpty()) {
            View balance = new View(this);
            bar.addView(balance, new LinearLayout.LayoutParams(dp(78), dp(44)));
        }
        page.addView(bar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(64)));
        return page;
    }

    private LinearLayout addScrollBody(LinearLayout page) {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        LinearLayout body = vertical();
        body.setPadding(dp(8), dp(4), dp(8), dp(8));
        scroll.addView(body, new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        page.addView(scroll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
        return body;
    }

    private void finishPage(LinearLayout page) {
        setContentView(page);
        AlphaAnimation fade = new AlphaAnimation(0.15f, 1f);
        fade.setDuration(220);
        page.startAnimation(fade);
    }

    private void addSectionIntro(LinearLayout body, String title, String subtitle) {
        TextView t = label(title, 30, TEXT_DARK, Gravity.CENTER, true);
        t.setPadding(dp(10), dp(20), dp(10), dp(4));
        body.addView(t);
        TextView s = label(subtitle, 17, MUTED, Gravity.CENTER, false);
        s.setPadding(dp(10), 0, dp(10), dp(16));
        body.addView(s);
    }

    private View menuCard(String icon, String title, String subtitle, int color, View.OnClickListener listener) {
        LinearLayout card = horizontal();
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(dp(12), dp(12), dp(12), dp(12));
        card.setBackground(rounded(Color.WHITE, 24, lighten(color, 0.80f)));
        card.setOnClickListener(listener);
        addWithMarginsToSelf(card, dp(10), dp(6), dp(10), dp(7));

        TextView iconView = label(icon, icon.length() > 2 ? 14 : 29, Color.WHITE, Gravity.CENTER, true);
        iconView.setBackground(rounded(color, 22, color));
        card.addView(iconView, new LinearLayout.LayoutParams(dp(72), dp(72)));

        LinearLayout text = vertical();
        text.setPadding(dp(14), 0, dp(10), 0);
        text.addView(label(title, 22, TEXT_DARK, Gravity.LEFT, true));
        TextView sub = label(subtitle, 15, MUTED, Gravity.LEFT, false);
        sub.setPadding(0, dp(3), 0, 0);
        text.addView(sub);
        card.addView(text, weighted());
        TextView arrow = label("›", 36, color, Gravity.CENTER, true);
        card.addView(arrow, new LinearLayout.LayoutParams(dp(36), dp(60)));
        return card;
    }

    private View statChip(String icon, String title, int value, int color) {
        LinearLayout chip = vertical();
        chip.setGravity(Gravity.CENTER);
        TextView i = label(icon, 25, color, Gravity.CENTER, true);
        chip.addView(i);
        chip.addView(label(String.valueOf(value), 21, TEXT_DARK, Gravity.CENTER, true));
        chip.addView(label(title, 12, MUTED, Gravity.CENTER, false));
        return chip;
    }

    private View achievementRow(String icon, String title, int value, int color) {
        LinearLayout row = horizontal();
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(10), dp(10), dp(10), dp(10));
        row.setBackground(rounded(lighten(color, 0.86f), 16, lighten(color, 0.72f)));
        TextView i = label(icon, 26, color, Gravity.CENTER, true);
        row.addView(i, new LinearLayout.LayoutParams(dp(46), dp(46)));
        row.addView(label(title, 18, TEXT_DARK, Gravity.RIGHT, true), weighted());
        row.addView(label(String.valueOf(value), 24, color, Gravity.CENTER, true), new LinearLayout.LayoutParams(dp(70), dp(46)));
        addWithMarginsToSelf(row, 0, dp(5), 0, dp(5));
        return row;
    }

    private Button actionButton(String text, int color) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(18);
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setBackground(rounded(color, 18, color));
        return button;
    }

    private Button smallButton(String text, int color) {
        Button button = actionButton(text, color);
        button.setTextSize(13);
        button.setPadding(dp(3), dp(2), dp(3), dp(2));
        return button;
    }

    private TextView label(String value, int size, int color, int gravity, boolean bold) {
        TextView text = new TextView(this);
        text.setText(value);
        text.setTextSize(size);
        text.setTextColor(color);
        text.setGravity(gravity | Gravity.CENTER_VERTICAL);
        text.setTypeface(Typeface.create("sans", bold ? Typeface.BOLD : Typeface.NORMAL));
        text.setLineSpacing(0f, 1.08f);
        return text;
    }

    private LinearLayout vertical() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        return layout;
    }

    private LinearLayout horizontal() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        return layout;
    }

    private LinearLayout.LayoutParams weighted() {
        return new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
    }

    private LinearLayout.LayoutParams weightedWithMargins(int margin) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, dp(46), 1f);
        params.setMargins(margin, 0, margin, 0);
        return params;
    }

    private LinearLayout.LayoutParams cardParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, dp(212), 1f);
        params.setMargins(dp(6), dp(6), dp(6), dp(6));
        return params;
    }

    private GradientDrawable rounded(int fill, int radiusDp, int stroke) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(fill);
        drawable.setCornerRadius(dp(radiusDp));
        drawable.setStroke(dp(1), stroke);
        return drawable;
    }

    private GradientDrawable gradient(int start, int end) {
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{start, end});
        drawable.setCornerRadius(0f);
        return drawable;
    }

    private int lighten(int color, float amount) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.rgb(
                Math.min(255, (int) (r + (255 - r) * amount)),
                Math.min(255, (int) (g + (255 - g) * amount)),
                Math.min(255, (int) (b + (255 - b) * amount))
        );
    }

    private void animatePress(View view) {
        AlphaAnimation animation = new AlphaAnimation(1f, 0.55f);
        animation.setDuration(120);
        animation.setRepeatMode(AlphaAnimation.REVERSE);
        animation.setRepeatCount(1);
        view.startAnimation(animation);
    }

    private void addWithMargins(LinearLayout parent, View child, int left, int top, int right, int bottom, int width, int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(left, top, right, bottom);
        parent.addView(child, params);
    }

    private void addWithMarginsToSelf(View view, int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(left, top, right, bottom);
        view.setLayoutParams(params);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        stopRecordingIfNeeded();
        stopPlayback();
        if (tts != null) {
            tts.shutdown();
            tts = null;
        }
        if (tones != null) {
            tones.release();
            tones = null;
        }
        super.onDestroy();
    }

    private static final class QuizConfig {
        final String title;
        final String prompt;
        final LearnItem[] items;
        final boolean rtl;
        final int color;

        QuizConfig(String title, String prompt, LearnItem[] items, boolean rtl, int color) {
            this.title = title;
            this.prompt = prompt;
            this.items = items;
            this.rtl = rtl;
            this.color = color;
        }
    }

    private static final class ScreenState {
        final int type;
        final String title;
        final LearnItem[] items;
        final boolean rtl;
        final QuizConfig quiz;

        private ScreenState(int type, String title, LearnItem[] items, boolean rtl, QuizConfig quiz) {
            this.type = type;
            this.title = title;
            this.items = items;
            this.rtl = rtl;
            this.quiz = quiz;
        }

        static ScreenState home() { return new ScreenState(SCREEN_HOME, "", null, true, null); }
        static ScreenState simple(int type) { return new ScreenState(type, "", null, true, null); }
        static ScreenState learn(String title, LearnItem[] items, boolean rtl) { return new ScreenState(SCREEN_LEARN, title, items, rtl, null); }
        static ScreenState quiz(QuizConfig quiz) { return new ScreenState(SCREEN_QUIZ, quiz.title, quiz.items, quiz.rtl, quiz); }
        static ScreenState recordings(String title, LearnItem[] items, boolean rtl) { return new ScreenState(SCREEN_RECORDINGS, title, items, rtl, null); }
    }
}
