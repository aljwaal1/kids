package com.explapp.kidslegacy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;

import java.util.Locale;

public final class WordPictureView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final LearnItem item;
    private final String word;

    public WordPictureView(Context context, LearnItem item, String displayWord) {
        super(context);
        this.item = item;
        this.word = displayWord == null ? "" : displayWord;
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = getWidth();
        float h = getHeight();
        float cx = w / 2f;
        float cy = h / 2f;
        float s = Math.min(w, h);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(lighten(item.accentColor, 0.82f));
        paint.setShadowLayer(10f, 0f, 4f, Color.argb(45, 0, 0, 0));
        canvas.drawRoundRect(new RectF(w * .05f, h * .04f, w * .95f, h * .96f), s * .10f, s * .10f, paint);
        paint.clearShadowLayer();

        if (item.illustrationType == LearnItem.TYPE_NUMBER) {
            drawNumber(canvas, cx, cy, s);
            return;
        }
        if (item.illustrationType == LearnItem.TYPE_COLOR) {
            drawColor(canvas, cx, cy, s);
            return;
        }

        String key = normalize(word);
        if (isAnimal(key)) {
            drawAnimal(canvas, cx, cy, s, key);
        } else if (contains(key, "سيارة", "car", "van")) {
            drawCar(canvas, cx, cy, s);
        } else if (contains(key, "طائرة", "plane")) {
            drawPlane(canvas, cx, cy, s);
        } else if (contains(key, "تفاحة", "apple", "رمان", "pomegranate", "orange", "ليمون", "lemon")) {
            drawFruit(canvas, cx, cy, s, key);
        } else if (contains(key, "موز", "banana")) {
            drawBanana(canvas, cx, cy, s);
        } else if (contains(key, "شمس", "sun")) {
            drawSun(canvas, cx, cy, s);
        } else if (contains(key, "هلال", "moon")) {
            drawMoon(canvas, cx, cy, s);
        } else if (contains(key, "وردة", "flower")) {
            drawFlower(canvas, cx, cy, s);
        } else if (contains(key, "يد", "hand")) {
            drawHand(canvas, cx, cy, s);
        } else if (contains(key, "ظرف", "envelope")) {
            drawEnvelope(canvas, cx, cy, s);
        } else if (contains(key, "كرة", "ball")) {
            drawBall(canvas, cx, cy, s);
        } else if (contains(key, "مظلة", "umbrella")) {
            drawUmbrella(canvas, cx, cy, s);
        } else if (contains(key, "طائرة ورقية", "kite")) {
            drawKite(canvas, cx, cy, s);
        } else if (contains(key, "عصير", "juice")) {
            drawJuice(canvas, cx, cy, s);
        } else if (contains(key, "آيس", "ice cream")) {
            drawIceCream(canvas, cx, cy, s);
        } else if (contains(key, "ماء", "water")) {
            drawWater(canvas, cx, cy, s);
        } else if (contains(key, "ذرة", "corn")) {
            drawCorn(canvas, cx, cy, s);
        } else if (contains(key, "تاج", "queen", "crown")) {
            drawCrown(canvas, cx, cy, s);
        } else if (contains(key, "عش", "nest")) {
            drawNest(canvas, cx, cy, s);
        } else {
            drawSymbol(canvas, cx, cy, s);
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.US);
    }

    private boolean contains(String value, String... keys) {
        for (String key : keys) if (value.contains(key.toLowerCase(Locale.US))) return true;
        return false;
    }

    private boolean isAnimal(String key) {
        return contains(key, "أسد", "lion", "بطة", "duck", "ثعلب", "fox", "جمل", "camel", "حصان", "horse", "خروف", "sheep", "دجاجة", "chicken", "زرافة", "giraffe", "سمكة", "fish", "صقر", "falcon", "ضفدع", "frog", "عصفور", "bird", "غزال", "deer", "فيل", "elephant", "قطة", "cat", "كلب", "dog", "نمر", "tiger", "أرنب", "rabbit", "قرد", "monkey", "دب", "bear", "بقرة", "cow", "panda", "zebra");
    }

    private void drawNumber(Canvas c, float cx, float cy, float s) {
        paint.setColor(item.accentColor);
        c.drawCircle(cx, cy, s * .32f, paint);
        drawCentered(c, item.title, cx, cy, s * .42f, Color.WHITE);
    }

    private void drawColor(Canvas c, float cx, float cy, float s) {
        paint.setColor(item.accentColor);
        paint.setShadowLayer(10f, 0f, 4f, Color.argb(75, 0, 0, 0));
        c.drawCircle(cx, cy, s * .31f, paint);
        paint.clearShadowLayer();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Math.max(3f, s * .025f));
        paint.setColor(Color.argb(90, 0, 0, 0));
        c.drawCircle(cx, cy, s * .31f, paint);
        paint.setStyle(Paint.Style.FILL);
    }

    private void drawAnimal(Canvas c, float cx, float cy, float s, String key) {
        int base = contains(key, "lion", "أسد") ? Color.rgb(245, 158, 11)
                : contains(key, "elephant", "فيل") ? Color.rgb(148, 163, 184)
                : contains(key, "frog", "ضفدع") ? Color.rgb(34, 197, 94)
                : contains(key, "tiger", "نمر", "fox", "ثعلب") ? Color.rgb(249, 115, 22)
                : contains(key, "rabbit", "أرنب") ? Color.rgb(244, 163, 196)
                : contains(key, "fish", "سمكة") ? Color.rgb(34, 211, 238)
                : contains(key, "bear", "دب", "monkey", "قرد", "horse", "حصان", "camel", "جمل", "dog", "كلب") ? Color.rgb(180, 112, 62)
                : Color.rgb(250, 190, 70);

        if (contains(key, "fish", "سمكة")) {
            paint.setColor(base);
            c.drawOval(new RectF(cx - s * .32f, cy - s * .19f, cx + s * .28f, cy + s * .19f), paint);
            Path tail = new Path();
            tail.moveTo(cx - s * .31f, cy);
            tail.lineTo(cx - s * .48f, cy - s * .22f);
            tail.lineTo(cx - s * .48f, cy + s * .22f);
            tail.close();
            c.drawPath(tail, paint);
            paint.setColor(Color.WHITE); c.drawCircle(cx + s * .12f, cy - s * .04f, s * .035f, paint);
            paint.setColor(Color.rgb(30, 41, 59)); c.drawCircle(cx + s * .13f, cy - s * .04f, s * .016f, paint);
            return;
        }

        if (contains(key, "rabbit", "أرنب")) {
            paint.setColor(base);
            c.drawOval(new RectF(cx - s * .20f, cy - s * .47f, cx - s * .04f, cy - s * .05f), paint);
            c.drawOval(new RectF(cx + s * .04f, cy - s * .47f, cx + s * .20f, cy - s * .05f), paint);
        }
        if (contains(key, "elephant", "فيل")) {
            paint.setColor(base);
            c.drawCircle(cx - s * .24f, cy, s * .17f, paint);
            c.drawCircle(cx + s * .24f, cy, s * .17f, paint);
        }

        paint.setColor(base);
        c.drawCircle(cx, cy, s * .29f, paint);
        if (!contains(key, "elephant", "فيل", "frog", "ضفدع")) {
            Path ears = new Path();
            ears.moveTo(cx - s * .25f, cy - s * .14f);
            ears.lineTo(cx - s * .13f, cy - s * .38f);
            ears.lineTo(cx - s * .02f, cy - s * .14f);
            ears.moveTo(cx + s * .25f, cy - s * .14f);
            ears.lineTo(cx + s * .13f, cy - s * .38f);
            ears.lineTo(cx + s * .02f, cy - s * .14f);
            c.drawPath(ears, paint);
        }
        drawEyes(c, cx, cy - s * .05f, s * .085f, s * .018f);
        paint.setColor(Color.rgb(80, 48, 38)); c.drawCircle(cx, cy + s * .05f, s * .032f, paint);
        paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(Math.max(4f, s * .025f)); paint.setStrokeCap(Paint.Cap.ROUND);
        c.drawArc(new RectF(cx - s * .10f, cy + s * .04f, cx + s * .10f, cy + s * .16f), 15, 150, false, paint);
        paint.setStyle(Paint.Style.FILL);
        if (contains(key, "elephant", "فيل")) {
            paint.setColor(base); c.drawRoundRect(new RectF(cx - s * .045f, cy + s * .05f, cx + s * .045f, cy + s * .35f), s * .04f, s * .04f, paint);
        }
    }

    private void drawCar(Canvas c, float cx, float cy, float s) {
        paint.setColor(item.accentColor);
        c.drawRoundRect(new RectF(cx - s * .38f, cy - s * .05f, cx + s * .38f, cy + s * .22f), s * .07f, s * .07f, paint);
        Path roof = new Path();
        roof.moveTo(cx - s * .24f, cy - s * .05f);
        roof.lineTo(cx - s * .10f, cy - s * .27f);
        roof.lineTo(cx + s * .20f, cy - s * .27f);
        roof.lineTo(cx + s * .31f, cy - s * .05f);
        roof.close();
        paint.setColor(Color.rgb(147, 197, 253)); c.drawPath(roof, paint);
        paint.setColor(Color.rgb(30, 41, 59));
        c.drawCircle(cx - s * .24f, cy + s * .22f, s * .085f, paint);
        c.drawCircle(cx + s * .24f, cy + s * .22f, s * .085f, paint);
    }

    private void drawPlane(Canvas c, float cx, float cy, float s) {
        paint.setColor(item.accentColor);
        Path p = new Path();
        p.moveTo(cx - s * .46f, cy + s * .05f); p.lineTo(cx + s * .44f, cy - s * .20f); p.lineTo(cx + s * .48f, cy - s * .08f);
        p.lineTo(cx + s * .10f, cy + s * .08f); p.lineTo(cx + s * .22f, cy + s * .34f); p.lineTo(cx + s * .08f, cy + s * .38f);
        p.lineTo(cx - s * .10f, cy + s * .16f); p.lineTo(cx - s * .40f, cy + s * .25f); p.close();
        c.drawPath(p, paint);
    }

    private void drawFruit(Canvas c, float cx, float cy, float s, String key) {
        int color = contains(key, "lemon", "ليمون") ? Color.rgb(250, 204, 21)
                : contains(key, "orange") ? Color.rgb(251, 146, 60)
                : contains(key, "pomegranate", "رمان") ? Color.rgb(220, 38, 38)
                : Color.rgb(239, 68, 68);
        paint.setColor(color);
        if (contains(key, "lemon", "ليمون")) c.drawOval(new RectF(cx - s * .34f, cy - s * .21f, cx + s * .34f, cy + s * .21f), paint);
        else c.drawCircle(cx, cy + s * .03f, s * .29f, paint);
        paint.setColor(Color.rgb(22, 163, 74));
        Path leaf = new Path(); leaf.moveTo(cx, cy - s * .24f); leaf.quadTo(cx + s * .26f, cy - s * .46f, cx + s * .32f, cy - s * .22f); leaf.quadTo(cx + s * .15f, cy - s * .14f, cx, cy - s * .24f); c.drawPath(leaf, paint);
    }

    private void drawBanana(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.rgb(250, 204, 21));
        Path p = new Path();
        p.moveTo(cx - s * .35f, cy - s * .25f); p.quadTo(cx - s * .16f, cy + s * .40f, cx + s * .36f, cy + s * .15f);
        p.quadTo(cx + s * .06f, cy + s * .47f, cx - s * .26f, cy + s * .24f); p.quadTo(cx - s * .47f, cy + s * .08f, cx - s * .35f, cy - s * .25f); p.close(); c.drawPath(p, paint);
    }

    private void drawSun(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.rgb(250, 204, 21)); c.drawCircle(cx, cy, s * .25f, paint);
        paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(Math.max(6f, s * .035f)); paint.setStrokeCap(Paint.Cap.ROUND); paint.setColor(Color.rgb(245, 158, 11));
        for (int i = 0; i < 8; i++) { double a = Math.PI * 2 * i / 8; c.drawLine(cx + (float)Math.cos(a) * s * .34f, cy + (float)Math.sin(a) * s * .34f, cx + (float)Math.cos(a) * s * .45f, cy + (float)Math.sin(a) * s * .45f, paint); }
        paint.setStyle(Paint.Style.FILL);
    }

    private void drawMoon(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.rgb(250, 204, 21)); c.drawCircle(cx, cy, s * .32f, paint);
        paint.setColor(lighten(item.accentColor, .90f)); c.drawCircle(cx + s * .15f, cy - s * .11f, s * .31f, paint);
    }

    private void drawFlower(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.rgb(236, 72, 153));
        for (int i = 0; i < 6; i++) { double a = Math.PI * 2 * i / 6; c.drawCircle(cx + (float)Math.cos(a) * s * .18f, cy + (float)Math.sin(a) * s * .18f, s * .14f, paint); }
        paint.setColor(Color.rgb(250, 204, 21)); c.drawCircle(cx, cy, s * .13f, paint);
        paint.setColor(Color.rgb(22, 163, 74)); c.drawRoundRect(new RectF(cx - s * .025f, cy + s * .24f, cx + s * .025f, cy + s * .47f), s * .02f, s * .02f, paint);
    }

    private void drawHand(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.rgb(246, 196, 156));
        c.drawRoundRect(new RectF(cx - s * .20f, cy - s * .02f, cx + s * .24f, cy + s * .35f), s * .10f, s * .10f, paint);
        for (int i = 0; i < 4; i++) c.drawRoundRect(new RectF(cx - s * .19f + i * s * .11f, cy - s * .36f - i * s * .02f, cx - s * .10f + i * s * .11f, cy + s * .02f), s * .04f, s * .04f, paint);
        c.drawOval(new RectF(cx - s * .34f, cy + s * .02f, cx - s * .05f, cy + s * .19f), paint);
    }

    private void drawEnvelope(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.WHITE); c.drawRoundRect(new RectF(cx - s * .38f, cy - s * .23f, cx + s * .38f, cy + s * .24f), s * .05f, s * .05f, paint);
        paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(Math.max(5f, s * .025f)); paint.setColor(item.accentColor);
        c.drawRoundRect(new RectF(cx - s * .38f, cy - s * .23f, cx + s * .38f, cy + s * .24f), s * .05f, s * .05f, paint);
        c.drawLine(cx - s * .36f, cy - s * .20f, cx, cy + s * .05f, paint); c.drawLine(cx + s * .36f, cy - s * .20f, cx, cy + s * .05f, paint); paint.setStyle(Paint.Style.FILL);
    }

    private void drawBall(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.WHITE); c.drawCircle(cx, cy, s * .31f, paint);
        paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(Math.max(5f, s * .025f)); paint.setColor(item.accentColor); c.drawCircle(cx, cy, s * .31f, paint); paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(30, 41, 59)); Path p = new Path(); for (int i=0;i<5;i++){double a=-Math.PI/2+i*Math.PI*2/5;float x=cx+(float)Math.cos(a)*s*.12f;float y=cy+(float)Math.sin(a)*s*.12f;if(i==0)p.moveTo(x,y);else p.lineTo(x,y);}p.close();c.drawPath(p,paint);
    }

    private void drawUmbrella(Canvas c, float cx, float cy, float s) {
        paint.setColor(item.accentColor); Path p = new Path(); p.moveTo(cx - s * .38f, cy); p.quadTo(cx, cy - s * .48f, cx + s * .38f, cy); p.close(); c.drawPath(p, paint);
        paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(Math.max(6f,s*.03f)); paint.setStrokeCap(Paint.Cap.ROUND); paint.setColor(Color.rgb(30,41,59)); c.drawLine(cx,cy,cx,cy+s*.36f,paint); c.drawArc(new RectF(cx-s*.13f,cy+s*.25f,cx+s*.13f,cy+s*.46f),0,150,false,paint); paint.setStyle(Paint.Style.FILL);
    }

    private void drawKite(Canvas c, float cx, float cy, float s) {
        paint.setColor(item.accentColor); Path p=new Path();p.moveTo(cx,cy-s*.40f);p.lineTo(cx+s*.31f,cy);p.lineTo(cx,cy+s*.34f);p.lineTo(cx-s*.31f,cy);p.close();c.drawPath(p,paint);
        paint.setStyle(Paint.Style.STROKE);paint.setStrokeWidth(Math.max(4f,s*.02f));paint.setColor(Color.WHITE);c.drawLine(cx,cy-s*.39f,cx,cy+s*.33f,paint);c.drawLine(cx-s*.30f,cy,cx+s*.30f,cy,paint);paint.setColor(Color.rgb(30,41,59));c.drawLine(cx,cy+s*.34f,cx+s*.12f,cy+s*.48f,paint);paint.setStyle(Paint.Style.FILL);
    }

    private void drawJuice(Canvas c, float cx, float cy, float s) {
        paint.setColor(Color.rgb(251,146,60)); Path p=new Path();p.moveTo(cx-s*.25f,cy-s*.31f);p.lineTo(cx+s*.25f,cy-s*.31f);p.lineTo(cx+s*.19f,cy+s*.34f);p.lineTo(cx-s*.19f,cy+s*.34f);p.close();c.drawPath(p,paint);
        paint.setStyle(Paint.Style.STROKE);paint.setStrokeWidth(Math.max(5f,s*.025f));paint.setColor(Color.rgb(239,68,68));c.drawLine(cx+s*.08f,cy-s*.28f,cx+s*.27f,cy-s*.48f,paint);paint.setStyle(Paint.Style.FILL);
    }

    private void drawIceCream(Canvas c,float cx,float cy,float s){paint.setColor(Color.rgb(217,119,6));Path p=new Path();p.moveTo(cx-s*.20f,cy);p.lineTo(cx+s*.20f,cy);p.lineTo(cx,cy+s*.42f);p.close();c.drawPath(p,paint);paint.setColor(Color.rgb(249,168,212));c.drawCircle(cx-s*.10f,cy-s*.10f,s*.18f,paint);paint.setColor(Color.rgb(253,230,138));c.drawCircle(cx+s*.11f,cy-s*.10f,s*.18f,paint);}

    private void drawWater(Canvas c,float cx,float cy,float s){paint.setColor(Color.rgb(56,189,248));Path p=new Path();p.moveTo(cx,cy-s*.42f);p.cubicTo(cx-s*.34f,cy-s*.04f,cx-s*.30f,cy+s*.35f,cx,cy+s*.40f);p.cubicTo(cx+s*.30f,cy+s*.35f,cx+s*.34f,cy-s*.04f,cx,cy-s*.42f);p.close();c.drawPath(p,paint);paint.setColor(Color.argb(170,255,255,255));c.drawCircle(cx-s*.10f,cy+s*.02f,s*.045f,paint);}

    private void drawCorn(Canvas c,float cx,float cy,float s){paint.setColor(Color.rgb(250,204,21));c.drawOval(new RectF(cx-s*.18f,cy-s*.38f,cx+s*.18f,cy+s*.36f),paint);paint.setStyle(Paint.Style.STROKE);paint.setStrokeWidth(Math.max(2f,s*.012f));paint.setColor(Color.rgb(217,119,6));for(int i=-2;i<=2;i++)c.drawLine(cx+i*s*.065f,cy-s*.34f,cx+i*s*.065f,cy+s*.31f,paint);for(int i=-3;i<=3;i++)c.drawLine(cx-s*.16f,cy+i*s*.09f,cx+s*.16f,cy+i*s*.09f,paint);paint.setStyle(Paint.Style.FILL);paint.setColor(Color.rgb(34,197,94));Path l=new Path();l.moveTo(cx-s*.17f,cy+s*.30f);l.quadTo(cx-s*.45f,cy,cx-s*.23f,cy-s*.26f);l.lineTo(cx-s*.08f,cy+s*.34f);l.close();c.drawPath(l,paint);}

    private void drawCrown(Canvas c,float cx,float cy,float s){paint.setColor(Color.rgb(250,204,21));Path p=new Path();p.moveTo(cx-s*.36f,cy+s*.28f);p.lineTo(cx-s*.42f,cy-s*.26f);p.lineTo(cx-s*.17f,cy);p.lineTo(cx,cy-s*.38f);p.lineTo(cx+s*.17f,cy);p.lineTo(cx+s*.42f,cy-s*.26f);p.lineTo(cx+s*.36f,cy+s*.28f);p.close();c.drawPath(p,paint);paint.setColor(Color.rgb(239,68,68));c.drawCircle(cx,cy+s*.10f,s*.055f,paint);}

    private void drawNest(Canvas c,float cx,float cy,float s){paint.setColor(Color.rgb(146,64,14));Path p=new Path();p.moveTo(cx-s*.36f,cy);p.quadTo(cx,cy+s*.48f,cx+s*.36f,cy);p.close();c.drawPath(p,paint);paint.setColor(Color.WHITE);c.drawOval(new RectF(cx-s*.18f,cy-s*.20f,cx-s*.02f,cy+s*.10f),paint);paint.setColor(Color.rgb(219,234,254));c.drawOval(new RectF(cx+s*.02f,cy-s*.20f,cx+s*.18f,cy+s*.10f),paint);}

    private void drawSymbol(Canvas c,float cx,float cy,float s){paint.setColor(item.accentColor);c.drawCircle(cx,cy,s*.31f,paint);String symbol=word.length()>0?word.substring(0,1):item.title;drawCentered(c,symbol,cx,cy,s*.32f,Color.WHITE);}

    private void drawEyes(Canvas c,float cx,float cy,float gap,float r){paint.setColor(Color.WHITE);c.drawCircle(cx-gap,cy,r*1.8f,paint);c.drawCircle(cx+gap,cy,r*1.8f,paint);paint.setColor(Color.rgb(30,41,59));c.drawCircle(cx-gap,cy,r,paint);c.drawCircle(cx+gap,cy,r,paint);}

    private void drawCentered(Canvas c,String text,float cx,float cy,float size,int color){paint.setColor(color);paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));paint.setTextAlign(Paint.Align.CENTER);paint.setTextSize(size);Paint.FontMetrics fm=paint.getFontMetrics();c.drawText(text,cx,cy-(fm.ascent+fm.descent)/2f,paint);}

    private int lighten(int color,float amount){int r=Color.red(color),g=Color.green(color),b=Color.blue(color);return Color.rgb(Math.min(255,(int)(r+(255-r)*amount)),Math.min(255,(int)(g+(255-g)*amount)),Math.min(255,(int)(b+(255-b)*amount)));}
}
