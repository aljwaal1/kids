package com.explapp.kidslegacy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;

public final class IllustrationView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private LearnItem item;

    public IllustrationView(Context context, LearnItem item) {
        super(context);
        this.item = item;
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void setItem(LearnItem item) {
        this.item = item;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (item == null) return;

        float w = getWidth();
        float h = getHeight();
        float cx = w / 2f;
        float cy = h / 2f;
        float size = Math.min(w, h);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(lighten(item.accentColor, 0.82f));
        paint.setShadowLayer(12f, 0f, 5f, Color.argb(45, 0, 0, 0));
        canvas.drawRoundRect(new RectF(w * 0.12f, h * 0.08f, w * 0.88f, h * 0.92f), size * 0.18f, size * 0.18f, paint);
        paint.clearShadowLayer();

        if (item.illustrationType == LearnItem.TYPE_LETTER || item.illustrationType == LearnItem.TYPE_NUMBER) {
            drawSymbol(canvas, item.title, cx, cy, size);
            return;
        }
        if (item.illustrationType == LearnItem.TYPE_COLOR) {
            drawColorDot(canvas, cx, cy, size);
            return;
        }

        switch (item.illustrationType) {
            case LearnItem.TYPE_LION: drawLion(canvas, cx, cy, size); break;
            case LearnItem.TYPE_ELEPHANT: drawElephant(canvas, cx, cy, size); break;
            case LearnItem.TYPE_CAT: drawCat(canvas, cx, cy, size, false); break;
            case LearnItem.TYPE_DOG: drawDog(canvas, cx, cy, size); break;
            case LearnItem.TYPE_HORSE: drawHorse(canvas, cx, cy, size); break;
            case LearnItem.TYPE_RABBIT: drawRabbit(canvas, cx, cy, size); break;
            case LearnItem.TYPE_COW: drawCow(canvas, cx, cy, size); break;
            case LearnItem.TYPE_MONKEY: drawMonkey(canvas, cx, cy, size); break;
            case LearnItem.TYPE_GIRAFFE: drawGiraffe(canvas, cx, cy, size); break;
            case LearnItem.TYPE_TIGER: drawCat(canvas, cx, cy, size, true); break;
            case LearnItem.TYPE_BEAR: drawBear(canvas, cx, cy, size); break;
            case LearnItem.TYPE_FISH: drawFish(canvas, cx, cy, size); break;
            default: drawSymbol(canvas, item.title.substring(0, 1), cx, cy, size); break;
        }
    }

    private void drawSymbol(Canvas c, String symbol, float cx, float cy, float s) {
        paint.setColor(item.accentColor);
        c.drawCircle(cx, cy, s * 0.30f, paint);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(s * 0.35f);
        Paint.FontMetrics fm = paint.getFontMetrics();
        c.drawText(symbol, cx, cy - (fm.ascent + fm.descent) / 2f, paint);
    }

    private void drawColorDot(Canvas c, float cx, float cy, float s) {
        paint.setColor(item.accentColor);
        paint.setShadowLayer(10f, 0f, 4f, Color.argb(70, 0, 0, 0));
        c.drawCircle(cx, cy, s * 0.29f, paint);
        paint.clearShadowLayer();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Math.max(2f, s * 0.025f));
        paint.setColor(Color.argb(80, 0, 0, 0));
        c.drawCircle(cx, cy, s * 0.29f, paint);
        paint.setStyle(Paint.Style.FILL);
    }

    private void drawEyes(Canvas c, float cx, float cy, float gap, float r) {
        paint.setColor(Color.WHITE);
        c.drawCircle(cx - gap, cy, r * 1.8f, paint);
        c.drawCircle(cx + gap, cy, r * 1.8f, paint);
        paint.setColor(Color.rgb(45, 45, 45));
        c.drawCircle(cx - gap, cy, r, paint);
        c.drawCircle(cx + gap, cy, r, paint);
    }

    private void drawSmile(Canvas c, float cx, float cy, float s) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Math.max(3f, s * 0.025f));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.rgb(90, 55, 45));
        c.drawArc(new RectF(cx - s * 0.09f, cy - s * 0.01f, cx + s * 0.09f, cy + s * 0.12f), 15, 150, false, paint);
        paint.setStyle(Paint.Style.FILL);
    }

    private void drawLion(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.rgb(225, 142, 40));
        c.drawCircle(cx, cy, s * 0.31f, paint);
        paint.setColor(Color.rgb(255, 205, 94));
        c.drawCircle(cx, cy, s * 0.23f, paint);
        drawEyes(c, cx, cy - s * 0.04f, s * 0.075f, s * 0.018f);
        paint.setColor(Color.rgb(110, 65, 35));
        c.drawCircle(cx, cy + s * 0.04f, s * 0.028f, paint);
        drawSmile(c, cx, cy + s * 0.04f, s);
    }

    private void drawElephant(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.rgb(150, 165, 175));
        c.drawCircle(cx - s * 0.20f, cy, s * 0.15f, paint);
        c.drawCircle(cx + s * 0.20f, cy, s * 0.15f, paint);
        paint.setColor(Color.rgb(175, 188, 196));
        c.drawCircle(cx, cy, s * 0.24f, paint);
        c.drawRoundRect(new RectF(cx - s * 0.05f, cy + s * 0.05f, cx + s * 0.05f, cy + s * 0.34f), s * 0.04f, s * 0.04f, paint);
        drawEyes(c, cx, cy - s * 0.06f, s * 0.075f, s * 0.016f);
    }

    private void drawCat(Canvas c, float cx, float cy, float s, boolean tiger) {
        paint.setColor(tiger ? Color.rgb(242, 142, 43) : Color.rgb(246, 178, 79));
        Path ears = new Path();
        ears.moveTo(cx - s * 0.22f, cy - s * 0.13f);
        ears.lineTo(cx - s * 0.12f, cy - s * 0.35f);
        ears.lineTo(cx - s * 0.02f, cy - s * 0.13f);
        ears.moveTo(cx + s * 0.22f, cy - s * 0.13f);
        ears.lineTo(cx + s * 0.12f, cy - s * 0.35f);
        ears.lineTo(cx + s * 0.02f, cy - s * 0.13f);
        ears.close();
        c.drawPath(ears, paint);
        c.drawCircle(cx, cy, s * 0.24f, paint);
        drawEyes(c, cx, cy - s * 0.04f, s * 0.075f, s * 0.016f);
        paint.setColor(Color.rgb(95, 60, 50));
        c.drawCircle(cx, cy + s * 0.04f, s * 0.025f, paint);
        drawSmile(c, cx, cy + s * 0.04f, s);
        if (tiger) {
            paint.setStrokeWidth(s * 0.025f);
            paint.setColor(Color.rgb(70, 55, 45));
            for (int i = -1; i <= 1; i++) {
                c.drawLine(cx + i * s * 0.07f, cy - s * 0.22f, cx + i * s * 0.05f, cy - s * 0.13f, paint);
            }
        }
    }

    private void drawDog(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.rgb(155, 105, 62));
        c.drawOval(new RectF(cx - s * 0.30f, cy - s * 0.20f, cx - s * 0.13f, cy + s * 0.18f), paint);
        c.drawOval(new RectF(cx + s * 0.13f, cy - s * 0.20f, cx + s * 0.30f, cy + s * 0.18f), paint);
        paint.setColor(Color.rgb(205, 154, 99));
        c.drawCircle(cx, cy, s * 0.24f, paint);
        drawEyes(c, cx, cy - s * 0.05f, s * 0.075f, s * 0.016f);
        paint.setColor(Color.rgb(70, 50, 42));
        c.drawCircle(cx, cy + s * 0.04f, s * 0.035f, paint);
        drawSmile(c, cx, cy + s * 0.04f, s);
    }

    private void drawHorse(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.rgb(105, 72, 52));
        c.drawOval(new RectF(cx - s * 0.22f, cy - s * 0.29f, cx + s * 0.22f, cy + s * 0.28f), paint);
        paint.setColor(Color.rgb(145, 98, 68));
        Path ear = new Path();
        ear.moveTo(cx - s * 0.15f, cy - s * 0.23f);
        ear.lineTo(cx - s * 0.20f, cy - s * 0.38f);
        ear.lineTo(cx - s * 0.07f, cy - s * 0.25f);
        ear.moveTo(cx + s * 0.15f, cy - s * 0.23f);
        ear.lineTo(cx + s * 0.20f, cy - s * 0.38f);
        ear.lineTo(cx + s * 0.07f, cy - s * 0.25f);
        c.drawPath(ear, paint);
        drawEyes(c, cx, cy - s * 0.08f, s * 0.07f, s * 0.015f);
        paint.setColor(Color.rgb(210, 160, 120));
        c.drawOval(new RectF(cx - s * 0.13f, cy + s * 0.05f, cx + s * 0.13f, cy + s * 0.23f), paint);
    }

    private void drawRabbit(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.rgb(232, 213, 225));
        c.drawOval(new RectF(cx - s * 0.18f, cy - s * 0.48f, cx - s * 0.03f, cy - s * 0.05f), paint);
        c.drawOval(new RectF(cx + s * 0.03f, cy - s * 0.48f, cx + s * 0.18f, cy - s * 0.05f), paint);
        c.drawCircle(cx, cy + s * 0.03f, s * 0.24f, paint);
        drawEyes(c, cx, cy - s * 0.02f, s * 0.075f, s * 0.016f);
        paint.setColor(Color.rgb(230, 125, 160));
        c.drawCircle(cx, cy + s * 0.07f, s * 0.027f, paint);
        drawSmile(c, cx, cy + s * 0.07f, s);
    }

    private void drawCow(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.WHITE);
        c.drawCircle(cx, cy, s * 0.25f, paint);
        paint.setColor(Color.rgb(70, 70, 70));
        c.drawCircle(cx - s * 0.10f, cy - s * 0.08f, s * 0.065f, paint);
        c.drawCircle(cx + s * 0.13f, cy - s * 0.13f, s * 0.055f, paint);
        drawEyes(c, cx, cy - s * 0.02f, s * 0.075f, s * 0.014f);
        paint.setColor(Color.rgb(245, 174, 184));
        c.drawOval(new RectF(cx - s * 0.14f, cy + s * 0.05f, cx + s * 0.14f, cy + s * 0.20f), paint);
    }

    private void drawMonkey(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.rgb(120, 78, 48));
        c.drawCircle(cx - s * 0.22f, cy, s * 0.12f, paint);
        c.drawCircle(cx + s * 0.22f, cy, s * 0.12f, paint);
        c.drawCircle(cx, cy, s * 0.25f, paint);
        paint.setColor(Color.rgb(219, 172, 124));
        c.drawOval(new RectF(cx - s * 0.16f, cy - s * 0.04f, cx + s * 0.16f, cy + s * 0.22f), paint);
        drawEyes(c, cx, cy - s * 0.08f, s * 0.075f, s * 0.015f);
        drawSmile(c, cx, cy + s * 0.06f, s);
    }

    private void drawGiraffe(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.rgb(244, 202, 86));
        c.drawRoundRect(new RectF(cx - s * 0.09f, cy - s * 0.10f, cx + s * 0.09f, cy + s * 0.34f), s * 0.06f, s * 0.06f, paint);
        c.drawOval(new RectF(cx - s * 0.22f, cy - s * 0.31f, cx + s * 0.22f, cy + s * 0.04f), paint);
        paint.setColor(Color.rgb(150, 95, 45));
        c.drawCircle(cx - s * 0.07f, cy + s * 0.12f, s * 0.035f, paint);
        c.drawCircle(cx + s * 0.06f, cy + s * 0.23f, s * 0.035f, paint);
        c.drawCircle(cx + s * 0.10f, cy - s * 0.18f, s * 0.035f, paint);
        drawEyes(c, cx, cy - s * 0.14f, s * 0.07f, s * 0.014f);
    }

    private void drawBear(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.rgb(105, 73, 52));
        c.drawCircle(cx - s * 0.18f, cy - s * 0.19f, s * 0.10f, paint);
        c.drawCircle(cx + s * 0.18f, cy - s * 0.19f, s * 0.10f, paint);
        c.drawCircle(cx, cy, s * 0.25f, paint);
        paint.setColor(Color.rgb(187, 143, 101));
        c.drawOval(new RectF(cx - s * 0.13f, cy + s * 0.00f, cx + s * 0.13f, cy + s * 0.18f), paint);
        drawEyes(c, cx, cy - s * 0.07f, s * 0.075f, s * 0.015f);
        paint.setColor(Color.rgb(55, 42, 35));
        c.drawCircle(cx, cy + s * 0.05f, s * 0.03f, paint);
    }

    private void drawFish(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.rgb(54, 171, 214));
        c.drawOval(new RectF(cx - s * 0.25f, cy - s * 0.16f, cx + s * 0.22f, cy + s * 0.16f), paint);
        Path tail = new Path();
        tail.moveTo(cx - s * 0.22f, cy);
        tail.lineTo(cx - s * 0.40f, cy - s * 0.18f);
        tail.lineTo(cx - s * 0.40f, cy + s * 0.18f);
        tail.close();
        c.drawPath(tail, paint);
        paint.setColor(Color.WHITE);
        c.drawCircle(cx + s * 0.11f, cy - s * 0.04f, s * 0.035f, paint);
        paint.setColor(Color.rgb(40, 40, 40));
        c.drawCircle(cx + s * 0.12f, cy - s * 0.04f, s * 0.017f, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(s * 0.018f);
        paint.setColor(Color.WHITE);
        c.drawArc(new RectF(cx - s * 0.02f, cy - s * 0.10f, cx + s * 0.15f, cy + s * 0.10f), -40, 90, false, paint);
        paint.setStyle(Paint.Style.FILL);
    }

    private int lighten(int color, float amount) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        r = (int) (r + (255 - r) * amount);
        g = (int) (g + (255 - g) * amount);
        b = (int) (b + (255 - b) * amount);
        return Color.rgb(Math.min(255, r), Math.min(255, g), Math.min(255, b));
    }
}
