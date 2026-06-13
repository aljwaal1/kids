import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
  runApp(const KidsApp());
}

class KidsApp extends StatelessWidget {
  const KidsApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'طفلي الأول',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xff6C63FF)),
        scaffoldBackgroundColor: const Color(0xffFFF8EA),
        appBarTheme: const AppBarTheme(
          centerTitle: true,
          backgroundColor: Color(0xffFFF8EA),
          foregroundColor: Color(0xff222222),
        ),
      ),
      home: const HomePage(),
    );
  }
}

void tapSound() {
  SystemSound.play(SystemSoundType.click);
  HapticFeedback.selectionClick();
}

class LearnItem {
  final String title;
  final String subtitle;
  final String emoji;
  const LearnItem(this.title, this.subtitle, this.emoji);
}

const arabicLetters = [
  LearnItem('أ', 'أسد', '🦁'), LearnItem('ب', 'بطة', '🦆'), LearnItem('ت', 'تفاحة', '🍎'),
  LearnItem('ث', 'ثعلب', '🦊'), LearnItem('ج', 'جمل', '🐪'), LearnItem('ح', 'حصان', '🐴'),
  LearnItem('خ', 'خروف', '🐑'), LearnItem('د', 'دجاجة', '🐔'), LearnItem('ذ', 'ذرة', '🌽'),
  LearnItem('ر', 'رمان', '🍎'), LearnItem('ز', 'زرافة', '🦒'), LearnItem('س', 'سمكة', '🐟'),
  LearnItem('ش', 'شمس', '☀️'), LearnItem('ص', 'صقر', '🦅'), LearnItem('ض', 'ضفدع', '🐸'),
  LearnItem('ط', 'طائرة', '✈️'), LearnItem('ظ', 'ظرف', '✉️'), LearnItem('ع', 'عصفور', '🐦'),
  LearnItem('غ', 'غزال', '🦌'), LearnItem('ف', 'فيل', '🐘'), LearnItem('ق', 'قطة', '🐱'),
  LearnItem('ك', 'كلب', '🐶'), LearnItem('ل', 'ليمون', '🍋'), LearnItem('م', 'موز', '🍌'),
  LearnItem('ن', 'نمر', '🐯'), LearnItem('هـ', 'هلال', '🌙'), LearnItem('و', 'وردة', '🌹'),
  LearnItem('ي', 'يد', '✋'),
];

const englishLetters = [
  LearnItem('A', 'Apple', '🍎'), LearnItem('B', 'Ball', '⚽'), LearnItem('C', 'Cat', '🐱'),
  LearnItem('D', 'Dog', '🐶'), LearnItem('E', 'Elephant', '🐘'), LearnItem('F', 'Fish', '🐟'),
  LearnItem('G', 'Giraffe', '🦒'), LearnItem('H', 'Horse', '🐴'), LearnItem('I', 'Ice Cream', '🍦'),
  LearnItem('J', 'Juice', '🧃'), LearnItem('K', 'Kite', '🪁'), LearnItem('L', 'Lion', '🦁'),
  LearnItem('M', 'Monkey', '🐒'), LearnItem('N', 'Nest', '🪺'), LearnItem('O', 'Orange', '🍊'),
  LearnItem('P', 'Panda', '🐼'), LearnItem('Q', 'Queen', '👑'), LearnItem('R', 'Rabbit', '🐰'),
  LearnItem('S', 'Sun', '☀️'), LearnItem('T', 'Tiger', '🐯'), LearnItem('U', 'Umbrella', '☂️'),
  LearnItem('V', 'Van', '🚐'), LearnItem('W', 'Water', '💧'), LearnItem('X', 'Xylophone', '🎼'),
  LearnItem('Y', 'Yo-yo', '🪀'), LearnItem('Z', 'Zebra', '🦓'),
];

const arabicNumbers = [
  LearnItem('1', 'واحد', '⭐'), LearnItem('2', 'اثنان', '⭐⭐'), LearnItem('3', 'ثلاثة', '⭐⭐⭐'),
  LearnItem('4', 'أربعة', '⭐⭐⭐⭐'), LearnItem('5', 'خمسة', '⭐⭐⭐⭐⭐'), LearnItem('6', 'ستة', '🍎🍎🍎🍎🍎🍎'),
  LearnItem('7', 'سبعة', '🍎🍎🍎🍎🍎🍎🍎'), LearnItem('8', 'ثمانية', '🍎🍎🍎🍎🍎🍎🍎🍎'),
  LearnItem('9', 'تسعة', '🍎🍎🍎🍎🍎🍎🍎🍎🍎'), LearnItem('10', 'عشرة', '🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎'),
];

const englishNumbers = [
  LearnItem('1', 'One', '⭐'), LearnItem('2', 'Two', '⭐⭐'), LearnItem('3', 'Three', '⭐⭐⭐'),
  LearnItem('4', 'Four', '⭐⭐⭐⭐'), LearnItem('5', 'Five', '⭐⭐⭐⭐⭐'), LearnItem('6', 'Six', '🍎🍎🍎🍎🍎🍎'),
  LearnItem('7', 'Seven', '🍎🍎🍎🍎🍎🍎🍎'), LearnItem('8', 'Eight', '🍎🍎🍎🍎🍎🍎🍎🍎'),
  LearnItem('9', 'Nine', '🍎🍎🍎🍎🍎🍎🍎🍎🍎'), LearnItem('10', 'Ten', '🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎'),
];

const colorsAr = [
  LearnItem('أحمر', '', '🔴'), LearnItem('أزرق', '', '🔵'), LearnItem('أخضر', '', '🟢'), LearnItem('أصفر', '', '🟡'),
  LearnItem('أسود', '', '⚫'), LearnItem('أبيض', '', '⚪'), LearnItem('بني', '', '🟤'), LearnItem('بنفسجي', '', '🟣'),
];

const colorsEn = [
  LearnItem('Red', '', '🔴'), LearnItem('Blue', '', '🔵'), LearnItem('Green', '', '🟢'), LearnItem('Yellow', '', '🟡'),
  LearnItem('Black', '', '⚫'), LearnItem('White', '', '⚪'), LearnItem('Brown', '', '🟤'), LearnItem('Purple', '', '🟣'),
];

const animals = [
  LearnItem('أسد', 'Lion', '🦁'), LearnItem('فيل', 'Elephant', '🐘'), LearnItem('قطة', 'Cat', '🐱'),
  LearnItem('كلب', 'Dog', '🐶'), LearnItem('حصان', 'Horse', '🐴'), LearnItem('أرنب', 'Rabbit', '🐰'),
  LearnItem('بقرة', 'Cow', '🐮'), LearnItem('قرد', 'Monkey', '🐒'), LearnItem('زرافة', 'Giraffe', '🦒'),
  LearnItem('نمر', 'Tiger', '🐯'), LearnItem('دب', 'Bear', '🐻'), LearnItem('سمكة', 'Fish', '🐟'),
];

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  void open(BuildContext context, Widget page) {
    tapSound();
    Navigator.push(context, MaterialPageRoute(builder: (_) => page));
  }

  @override
  Widget build(BuildContext context) {
    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        appBar: AppBar(title: const Text('طفلي الأول')),
        body: SafeArea(
          child: ListView(
            padding: const EdgeInsets.all(16),
            children: [
              const Text('تعلم والعب', textAlign: TextAlign.center, style: TextStyle(fontSize: 36, fontWeight: FontWeight.w900)),
              const SizedBox(height: 14),
              HomeButton(icon: '🇸🇦', text: 'العربية', onTap: () => open(context, const ArabicPage())),
              HomeButton(icon: '🇬🇧', text: 'English', onTap: () => open(context, const EnglishPage())),
              HomeButton(icon: '🐶', text: 'الحيوانات عربي + English', onTap: () => open(context, const LearnGrid(title: 'الحيوانات', items: animals, rtl: true))),
              HomeButton(icon: '🎮', text: 'الألعاب', onTap: () => open(context, const GamesPage())),
            ],
          ),
        ),
      ),
    );
  }
}

class HomeButton extends StatelessWidget {
  final String icon;
  final String text;
  final VoidCallback onTap;
  const HomeButton({super.key, required this.icon, required this.text, required this.onTap});

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.only(bottom: 14),
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(24)),
      child: InkWell(
        borderRadius: BorderRadius.circular(24),
        onTap: onTap,
        child: Padding(
          padding: const EdgeInsets.all(22),
          child: Row(
            children: [
              Text(icon, style: const TextStyle(fontSize: 42)),
              const SizedBox(width: 18),
              Expanded(child: Text(text, style: const TextStyle(fontSize: 26, fontWeight: FontWeight.bold))),
            ],
          ),
        ),
      ),
    );
  }
}

class ArabicPage extends StatelessWidget {
  const ArabicPage({super.key});
  void open(BuildContext c, Widget p) => Navigator.push(c, MaterialPageRoute(builder: (_) => p));

  @override
  Widget build(BuildContext context) {
    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        appBar: AppBar(title: const Text('العربية')),
        body: ListView(
          padding: const EdgeInsets.all(16),
          children: [
            HomeButton(icon: '🔤', text: 'الحروف العربية', onTap: () => open(context, const LearnGrid(title: 'الحروف العربية', items: arabicLetters, rtl: true))),
            HomeButton(icon: '🔢', text: 'الأرقام 1 - 10', onTap: () => open(context, const LearnGrid(title: 'الأرقام', items: arabicNumbers, rtl: true))),
            HomeButton(icon: '🎨', text: 'الألوان', onTap: () => open(context, const LearnGrid(title: 'الألوان', items: colorsAr, rtl: true))),
            HomeButton(icon: '🐾', text: 'الحيوانات', onTap: () => open(context, const LearnGrid(title: 'الحيوانات', items: animals, rtl: true))),
          ],
        ),
      ),
    );
  }
}

class EnglishPage extends StatelessWidget {
  const EnglishPage({super.key});
  void open(BuildContext c, Widget p) => Navigator.push(c, MaterialPageRoute(builder: (_) => p));

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('English')),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          HomeButton(icon: '🔠', text: 'Capital Letters', onTap: () => open(context, const LearnGrid(title: 'Capital Letters', items: englishLetters, rtl: false))),
          HomeButton(icon: '🔢', text: 'Numbers 1 - 10', onTap: () => open(context, const LearnGrid(title: 'Numbers', items: englishNumbers, rtl: false))),
          HomeButton(icon: '🎨', text: 'Colors', onTap: () => open(context, const LearnGrid(title: 'Colors', items: colorsEn, rtl: false))),
          HomeButton(icon: '🐾', text: 'Animals', onTap: () => open(context, const LearnGrid(title: 'Animals', items: animals, rtl: false))),
        ],
      ),
    );
  }
}

class LearnGrid extends StatelessWidget {
  final String title;
  final List<LearnItem> items;
  final bool rtl;
  const LearnGrid({super.key, required this.title, required this.items, required this.rtl});

  @override
  Widget build(BuildContext context) {
    return Directionality(
      textDirection: rtl ? TextDirection.rtl : TextDirection.ltr,
      child: Scaffold(
        appBar: AppBar(title: Text(title)),
        body: GridView.builder(
          padding: const EdgeInsets.all(12),
          itemCount: items.length,
          gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 2,
            mainAxisSpacing: 12,
            crossAxisSpacing: 12,
            childAspectRatio: 0.88,
          ),
          itemBuilder: (_, i) => LearnCard(item: items[i]),
        ),
      ),
    );
  }
}

class LearnCard extends StatelessWidget {
  final LearnItem item;
  const LearnCard({super.key, required this.item});

  @override
  Widget build(BuildContext context) {
    return Card(
      color: Colors.white,
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(26)),
      child: InkWell(
        borderRadius: BorderRadius.circular(26),
        onTap: tapSound,
        child: Padding(
          padding: const EdgeInsets.all(12),
          child: FittedBox(
            fit: BoxFit.scaleDown,
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(item.emoji, style: const TextStyle(fontSize: 58)),
                const SizedBox(height: 10),
                Text(item.title, textAlign: TextAlign.center, style: const TextStyle(fontSize: 36, fontWeight: FontWeight.w900)),
                if (item.subtitle.isNotEmpty) ...[
                  const SizedBox(height: 6),
                  Text(item.subtitle, textAlign: TextAlign.center, style: const TextStyle(fontSize: 25, fontWeight: FontWeight.bold, color: Color(0xff555555))),
                ],
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class GamesPage extends StatefulWidget {
  const GamesPage({super.key});

  @override
  State<GamesPage> createState() => _GamesPageState();
}

class _GamesPageState extends State<GamesPage> {
  int stars = 0;
  String msg = 'اختر الأسد';

  void answer(bool ok) {
    tapSound();
    setState(() {
      if (ok) {
        stars++;
        msg = 'أحسنت ⭐';
      } else {
        msg = 'حاول مرة أخرى';
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final options = animals.take(4).toList();
    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        appBar: AppBar(title: Text('الألعاب  ⭐ $stars')),
        body: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            children: [
              Text(msg, textAlign: TextAlign.center, style: const TextStyle(fontSize: 30, fontWeight: FontWeight.w900)),
              const SizedBox(height: 18),
              Expanded(
                child: GridView.builder(
                  itemCount: options.length,
                  gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(crossAxisCount: 2, mainAxisSpacing: 12, crossAxisSpacing: 12),
                  itemBuilder: (_, i) {
                    final item = options[i];
                    return Card(
                      color: Colors.white,
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(26)),
                      child: InkWell(
                        borderRadius: BorderRadius.circular(26),
                        onTap: () => answer(item.title == 'أسد'),
                        child: Center(child: Text(item.emoji, style: const TextStyle(fontSize: 74))),
                      ),
                    );
                  },
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
