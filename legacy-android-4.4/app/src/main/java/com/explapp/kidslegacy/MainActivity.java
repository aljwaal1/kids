package com.explapp.kidslegacy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private final String[][] categories = {
        {"الحروف العربية", "أ  ب  ت  ث  ج  ح  خ  د  ذ  ر  ز  س  ش  ص  ض  ط  ظ  ع  غ  ف  ق  ك  ل  م  ن  هـ  و  ي"},
        {"English Letters", "A  B  C  D  E  F  G  H  I  J  K  L  M  N  O  P  Q  R  S  T  U  V  W  X  Y  Z"},
        {"الأرقام", "1  2  3  4  5  6  7  8  9  10"},
        {"الألوان", "🔴 أحمر   🔵 أزرق   🟢 أخضر   🟡 أصفر   ⚫ أسود   ⚪ أبيض"},
        {"الحيوانات", "🦁 أسد   🐘 فيل   🐱 قطة   🐶 كلب   🐴 حصان   🐰 أرنب"}
    };

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        showHome();
    }

    private TextView text(String value, int size) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(Color.rgb(35,35,35));
        t.setGravity(Gravity.CENTER);
        t.setPadding(20,20,20,20);
        return t;
    }

    private void showHome() {
        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24,30,24,30);
        root.setBackgroundColor(Color.rgb(255,248,234));
        root.addView(text("طفلي الأول", 30));
        root.addView(text("نسخة خفيفة متوافقة مع Android 4.4", 16));
        for (int i = 0; i < categories.length; i++) {
            final int index = i;
            Button b = new Button(this);
            b.setText(categories[i][0]);
            b.setTextSize(20);
            b.setAllCaps(false);
            b.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { showCategory(index); }
            });
            root.addView(b, new LinearLayout.LayoutParams(-1, 150));
        }
        scroll.addView(root);
        setContentView(scroll);
    }

    private void showCategory(int index) {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(30,40,30,40);
        root.setBackgroundColor(Color.rgb(255,248,234));
        root.addView(text(categories[index][0], 28));
        root.addView(text(categories[index][1], 25), new LinearLayout.LayoutParams(-1, 0, 1));
        Button back = new Button(this);
        back.setText("رجوع");
        back.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showHome(); }
        });
        root.addView(back, new LinearLayout.LayoutParams(-1, 130));
        setContentView(root);
    }
}
