import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_tts/flutter_tts.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
  runApp(const KidsApp());
}

final FlutterTts tts = FlutterTts();

Future<void> speak(String text, {String lang = 'ar'}) async {
  await tts.setLanguage(lang == 'en' ? 'en-US' : 'ar');
  await tts.setSpeechRate(0.42);
  await tts.setPitch(1.05);
  await tts.speak(text);
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
        colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xff6c63ff)),
        scaffoldBackgroundColor: const Color(0xfffbf7ef),
      ),
      home: const HomePage(),
    );
  }
}

class LearnItem {
  final String title;
  final String subtitle;
  final String emoji;
  final String say;
  final String lang;
  const LearnItem(this.title, this.subtitle, this.emoji, this.say, this.lang);
}

const arabicLetters = <LearnItem>[
  LearnItem('أ', 'أسد', '🦁', 'أ، أسد', 'ar'), LearnItem('ب', 'بطة', '🦆', 'ب، بطة', 'ar'),
  LearnItem('ت', 'تفاحة', '🍎', 'ت، تفاحة', 'ar'), LearnItem('ث', 'ثعلب', '🦊', 'ث، ثعلب', 'ar'),
  LearnItem('ج', 'جمل', '🐪', 'ج، جمل', 'ar'), LearnItem('ح', 'حصان', '🐴', 'ح، حصان', 'ar'),
  LearnItem('خ', 'خروف', '🐑', 'خ، خروف', 'ar'), LearnItem('د', 'دجاجة', '🐔', 'د، دجاجة', 'ar'),
  LearnItem('ذ', 'ذرة', '🌽', 'ذ، ذرة', 'ar'), LearnItem('ر', 'رمان', '🍎', 'ر، رمان', 'ar'),
  LearnItem('ز', 'زرافة', '🦒', 'ز، زرافة', 'ar'), LearnItem('س', 'سمكة', '🐟', 'س، سمكة', 'ar'),
  LearnItem('ش', 'شمس', '☀️', 'ش، شمس', 'ar'), LearnItem('ص', 'صقر', '🦅', 'ص، صقر', 'ar'),
  LearnItem('ض', 'ضفدع', '🐸', 'ض، ضفدع', 'ar'), LearnItem('ط', 'طائرة', '✈️', 'ط، طائرة', 'ar'),
  LearnItem('ظ', 'ظرف', '✉️', 'ظ، ظرف', 'ar'), LearnItem('ع', 'عصفور', '🐦', 'ع، عصفور', 'ar'),
  LearnItem('غ', 'غزال', '🦌', 'غ، غزال', 'ar'), LearnItem('ف', 'فيل', '🐘', 'ف، فيل', 'ar'),
  LearnItem('ق', 'قطة', '🐱', 'ق، قطة', 'ar'), LearnItem('ك', 'كلب', '🐶', 'ك، كلب', 'ar'),
  LearnItem('ل', 'ليمون', '🍋', 'ل، ليمون', 'ar'), LearnItem('م', 'موز', '🍌', 'م، موز', 'ar'),
  LearnItem('ن', 'نمر', '🐯', 'ن، نمر', 'ar'), LearnItem('هـ', 'هلال', '🌙', 'هـ، هلال', 'ar'),
  LearnItem('و', 'وردة', '🌹', 'و، وردة', 'ar'), LearnItem('ي', 'يد', '✋', 'ي، يد', 'ar'),
];

const englishLetters = <LearnItem>[
  LearnItem('A', 'Apple', '🍎', 'A, Apple', 'en'), LearnItem('B', 'Ball', '⚽', 'B, Ball', 'en'),
  LearnItem('C', 'Cat', '🐱', 'C, Cat', 'en'), LearnItem('D', 'Dog', '🐶', 'D, Dog', 'en'),
  LearnItem('E', 'Elephant', '🐘', 'E, Elephant', 'en'), LearnItem('F', 'Fish', '🐟', 'F, Fish', 'en'),
  LearnItem('G', 'Giraffe', '🦒', 'G, Giraffe', 'en'), LearnItem('H', 'Horse', '🐴', 'H, Horse', 'en'),
  LearnItem('I', 'Ice cream', '🍦', 'I, Ice cream', 'en'), LearnItem('J', 'Juice', '🧃', 'J, Juice', 'en'),
  LearnItem('K', 'Kite', '🪁', 'K, Kite', 'en'), LearnItem('L', 'Lion', '🦁', 'L, Lion', 'en'),
  LearnItem('M', 'Monkey', '🐒', 'M, Monkey', 'en'), LearnItem('N', 'Nest', '🪺', 'N, Nest', 'en'),
  LearnItem('O', 'Orange', '🍊', 'O, Orange', 'en'), LearnItem('P', 'Panda', '🐼', 'P, Panda', 'en'),
  LearnItem('Q', 'Queen', '👑', 'Q, Queen', 'en'), LearnItem('R', 'Rabbit', '🐰', 'R, Rabbit', 'en'),
  LearnItem('S', 'Sun', '☀️', 'S, Sun', 'en'), LearnItem('T', 'Tiger', '🐯', 'T, Tiger', 'en'),
  LearnItem('U', 'Umbrella', '☂️', 'U, Umbrella', 'en'), LearnItem('V', 'Van', '🚐', 'V, Van', 'en'),
  LearnItem('W', 'Water', '💧', 'W, Water', 'en'), LearnItem('X', 'Xylophone', '🎼', 'X, Xylophone', 'en'),
  LearnItem('Y', 'Yo-yo', '🪀', 'Y, Yo-yo', 'en'), LearnItem('Z', 'Zebra', '🦓', 'Z, Zebra', 'en'),
];

const arabicNumbers = <LearnItem>[
  LearnItem('1', 'واحد', '⭐', 'واحد', 'ar'), LearnItem('2', 'اثنان', '⭐⭐', 'اثنان', 'ar'),
  LearnItem('3', 'ثلاثة', '⭐⭐⭐', 'ثلاثة', 'ar'), LearnItem('4', 'أربعة', '⭐⭐⭐⭐', 'أربعة', 'ar'),
  LearnItem('5', 'خمسة', '⭐⭐⭐⭐⭐', 'خمسة', 'ar'), LearnItem('6', 'ستة', '🍎🍎🍎🍎🍎🍎', 'ستة', 'ar'),
  LearnItem('7', 'سبعة', '🍎🍎🍎🍎🍎🍎🍎', 'سبعة', 'ar'), LearnItem('8', 'ثمانية', '🍎🍎🍎🍎🍎🍎🍎🍎', 'ثمانية', 'ar'),
  LearnItem('9', 'تسعة', '🍎🍎🍎🍎🍎🍎🍎🍎🍎', 'تسعة', 'ar'), LearnItem('10', 'عشرة', '🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎', 'عشرة', 'ar'),
];

const englishNumbers = <LearnItem>[
  LearnItem('1', 'One', '⭐', 'One', 'en'), LearnItem('2', 'Two', '⭐⭐', 'Two', 'en'),
  LearnItem('3', 'Three', '⭐⭐⭐', 'Three', 'en'), LearnItem('4', 'Four', '⭐⭐⭐⭐', 'Four', 'en'),
  LearnItem('5', 'Five', '⭐⭐⭐⭐⭐', 'Five', 'en'), LearnItem('6', 'Six', '🍎🍎🍎🍎🍎🍎', 'Six', 'en'),
  LearnItem('7', 'Seven', '🍎🍎🍎🍎🍎🍎🍎', 'Seven', 'en'), LearnItem('8', 'Eight', '🍎🍎🍎🍎🍎🍎🍎🍎', 'Eight', 'en'),
  LearnItem('9', 'Nine', '🍎🍎🍎🍎🍎🍎🍎🍎🍎', 'Nine', 'en'), LearnItem('10', 'Ten', '🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎', 'Ten', 'en'),
];

const colorsAr = <LearnItem>[
  LearnItem('أحمر', '', '🔴', 'أحمر', 'ar'), LearnItem('أزرق', '', '🔵', 'أزرق', 'ar'),
  LearnItem('أخضر', '', '🟢', 'أخضر', 'ar'), LearnItem('أصفر', '', '🟡', 'أصفر', 'ar'),
  LearnItem('أسود', '', '⚫', 'أسود', 'ar'), LearnItem('أبيض', '', '⚪', 'أبيض', 'ar'),
  LearnItem('بني', '', '🟤', 'بني', 'ar'), LearnItem('بنفسجي', '', '🟣', 'بنفسجي', 'ar'),
];

const colorsEn = <LearnItem>[
  LearnItem('Red', '', '🔴', 'Red', 'en'), LearnItem('Blue', '', '🔵', 'Blue', 'en'),
  LearnItem('Green', '', '🟢', 'Green', 'en'), LearnItem('Yellow', '', '🟡', 'Yellow', 'en'),
  LearnItem('Black', '', '⚫', 'Black', 'en'), LearnItem('White', '', '⚪', 'White', 'en'),
  LearnItem('Brown', '', '🟤', 'Brown', 'en'), LearnItem('Purple', '', '🟣', 'Purple', 'en'),
];

const animals = <LearnItem>[
  LearnItem('أسد', 'Lion', '🦁', 'أسد. Lion', 'ar'), LearnItem('فيل', 'Elephant', '🐘', 'فيل. Elephant', 'ar'),
  LearnItem('قطة', 'Cat', '🐱', 'قطة. Cat', 'ar'), LearnItem('كلب', 'Dog', '🐶', 'كلب. Dog', 'ar'),
  LearnItem('حصان', 'Horse', '🐴', 'حصان. Horse', 'ar'), LearnItem('أرنب', 'Rabbit', '🐰', 'أرنب. Rabbit', 'ar'),
  LearnItem('بقرة', 'Cow', '🐮', 'بقرة. Cow', 'ar'), LearnItem('قرد', 'Monkey', '🐒', 'قرد. Monkey', 'ar'),
  LearnItem('زرافة', 'Giraffe', '🦒', 'زرافة. Giraffe', 'ar'), LearnItem('نمر', 'Tiger', '🐯', 'نمر. Tiger', 'ar'),
  LearnItem('دب', 'Bear', '🐻', 'دب. Bear', 'ar'), LearnItem('سمكة', 'Fish', '🐟', 'سمكة. Fish', 'ar'),
];

class HomePage extends StatelessWidget {
  const HomePage({super.key});
  void open(BuildContext context, Widget page) => Navigator.push(context, MaterialPageRoute(builder: (_) => page));

  @override
  Widget build(BuildContext context) {
    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        appBar: AppBar(title: const Text('طفلي الأول'), centerTitle: true),
        body: Padding(
          padding: const EdgeInsets.all(16),
          child: ListView(
            children: [
              const Text('تعلم والعب', textAlign: TextAlign.center, style: TextStyle(fontSize: 34, fontWeight: FontWeight.w900)),
              const SizedBox(height: 18),
              HomeButton('العربية', '🇸🇦', () => open(context, const ArabicPage())),
              HomeButton('English', '🇬🇧', () => open(context, const EnglishPage())),
              HomeButton('الحيوانات عربي + English', '🐶', () => open(context, const LearnGrid(title: 'الحيوانات', items: animals, rtl: true))),
              HomeButton('الألعاب', '🎮', () => open(context, const GamesPage())),
              HomeButton('إنجازاتي', '🏆', () => open(context, const AchievementsPage())),
            ],
          ),
        ),
      ),
    );
  }
}

class HomeButton extends StatelessWidget {
  final String text;
  final String icon;
  final VoidCallback onTap;
  const HomeButton(this.text, this.icon, this.onTap, {super.key});

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 2,
      margin: const EdgeInsets.only(bottom: 14),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(24)),
      child: InkWell(
        borderRadius: BorderRadius.circular(24),
        onTap: onTap,
        child: Padding(
          padding: const EdgeInsets.all(22),
          child: Row(children: [Text(icon, style: const TextStyle(fontSize: 42)), const SizedBox(width: 18), Expanded(child: Text(text, style: const TextStyle(fontSize: 26, fontWeight: FontWeight.bold)))]),
        ),
      ),
    );
  }
}

class ArabicPage extends StatelessWidget {
  const ArabicPage({super.key});
  void open(BuildContext context, Widget page) => Navigator.push(context, MaterialPageRoute(builder: (_) => page));

  @override
  Widget build(BuildContext context) {
    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        appBar: AppBar(title: const Text('العربية'), centerTitle: true),
        body: Padding(
          padding: const EdgeInsets.all(16),
          child: ListView(children: [
            HomeButton('الحروف العربية', '🔤', () => open(context, const LearnGrid(title: 'الحروف العربية', items: arabicLetters, rtl: true))),
            HomeButton('الأرقام 1 - 10', '🔢', () => open(context, const LearnGrid(title: 'الأرقام', items: arabicNumbers, rtl: true))),
            HomeButton('الألوان', '🎨', () => open(context, const LearnGrid(title: 'الألوان', items: colorsAr, rtl: true))),
            HomeButton('الحيوانات عربي + English', '🐾', () => open(context, const LearnGrid(title: 'الحيوانات', items: animals, rtl: true))),
          ]),
        ),
      ),
    );
  }
}

class EnglishPage extends StatelessWidget {
  const EnglishPage({super.key});
  void open(BuildContext context, Widget page) => Navigator.push(context, MaterialPageRoute(builder: (_) => page));

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('English'), centerTitle: true),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: ListView(children: [
          HomeButton('Capital Letters', '🔠', () => open(context, const LearnGrid(title: 'Capital Letters', items: englishLetters))),
          HomeButton('Numbers 1 - 10', '🔢', () => open(context, const LearnGrid(title: 'Numbers', items: englishNumbers))),
          HomeButton('Colors', '🎨', () => open(context, const LearnGrid(title: 'Colors', items: colorsEn))),
          HomeButton('Animals عربي + English', '🐾', () => open(context, const LearnGrid(title: 'Animals', items: animals, rtl: true))),
        ]),
      ),
    );
  }
}

class LearnGrid extends StatelessWidget {
  final String title;
  final List<LearnItem> items;
  final bool rtl;
  const LearnGrid({super.key, required this.title, required this.items, this.rtl = false});

  @override
  Widget build(BuildContext context) {
    return Directionality(
      textDirection: rtl ? TextDirection.rtl : TextDirection.ltr,
      child: Scaffold(
        appBar: AppBar(title: Text(title), centerTitle: true),
        body: GridView.builder(
          padding: const EdgeInsets.all(12),
          itemCount: items.length,
          gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(crossAxisCount: 2, mainAxisSpacing: 12, crossAxisSpacing: 12, childAspectRatio: 0.9),
          itemBuilder: (context, index) => ItemCard(items[index]),
        ),
      ),
    );
  }
}

class ItemCard extends StatelessWidget {
  final LearnItem item;
  const ItemCard(this.item, {super.key});

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 2,
      color: Colors.white,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(26)),
      child: InkWell(
        borderRadius: BorderRadius.circular(26),
        onTap: () => speak(item.say, lang: item.lang),
        child: Padding(
          padding: const EdgeInsets.all(12),
          child: Column(mainAxisAlignment: MainAxisAlignment.center, children: [
            Text(item.emoji, textAlign: TextAlign.center, style: const TextStyle(fontSize: 46)),
            const SizedBox(height: 8),
            Text(item.title, textAlign: TextAlign.center, style: const TextStyle(fontSize: 32, fontWeight: FontWeight.w900)),
            if (item.subtitle.isNotEmpty) ...[const SizedBox(height: 6), Text(item.subtitle, textAlign: TextAlign.center, style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold, color: Color(0xff555555)))],
          ]),
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

  @override
  void initState() {
    super.initState();
    loadStars();
  }

  Future<void> loadStars() async {
    final prefs = await SharedPreferences.getInstance();
    setState(() => stars = prefs.getInt('stars') ?? 0);
  }

  Future<void> answer(bool correct) async {
    if (correct) {
      final prefs = await SharedPreferences.getInstance();
      stars++;
      await prefs.setInt('stars', stars);
      setState(() => msg = 'أحسنت ⭐');
      speak('أحسنت', lang: 'ar');
    } else {
      setState(() => msg = 'حاول مرة أخرى');
      speak('حاول مرة أخرى', lang: 'ar');
    }
  }

  @override
  Widget build(BuildContext context) {
    final options = const [
      LearnItem('أسد', 'Lion', '🦁', 'أسد', 'ar'),
      LearnItem('قطة', 'Cat', '🐱', 'قطة', 'ar'),
      LearnItem('فيل', 'Elephant', '🐘', 'فيل', 'ar'),
      LearnItem('كلب', 'Dog', '🐶', 'كلب', 'ar'),
    ];
    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        appBar: AppBar(title: Text('الألعاب  ⭐ $stars'), centerTitle: true),
        body: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(children: [
            Text(msg, textAlign: TextAlign.center, style: const TextStyle(fontSize: 28, fontWeight: FontWeight.w900)),
            const SizedBox(height: 20),
            Expanded(
              child: GridView.builder(
                itemCount: options.length,
                gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(crossAxisCount: 2, mainAxisSpacing: 12, crossAxisSpacing: 12),
                itemBuilder: (context, i) {
                  final item = options[i];
                  return Card(
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(24)),
                    child: InkWell(borderRadius: BorderRadius.circular(24), onTap: () => answer(item.title == 'أسد'), child: Center(child: Text(item.emoji, style: const TextStyle(fontSize: 70)))),
                  );
                },
              ),
            ),
          ]),
        ),
      ),
    );
  }
}

class AchievementsPage extends StatefulWidget {
  const AchievementsPage({super.key});
  @override
  State<AchievementsPage> createState() => _AchievementsPageState();
}

class _AchievementsPageState extends State<AchievementsPage> {
  int stars = 0;
  @override
  void initState() {
    super.initState();
    loadStars();
  }

  Future<void> loadStars() async {
    final prefs = await SharedPreferences.getInstance();
    setState(() => stars = prefs.getInt('stars') ?? 0);
  }

  @override
  Widget build(BuildContext context) {
    String cup = 'ابدأ اللعب واجمع النجوم';
    if (stars >= 50) cup = '🥇 كأس ذهبي';
    else if (stars >= 25) cup = '🥈 كأس فضي';
    else if (stars >= 10) cup = '🥉 كأس برونزي';

    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        appBar: AppBar(title: const Text('إنجازاتي'), centerTitle: true),
        body: Center(
          child: Card(
            margin: const EdgeInsets.all(20),
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(30)),
            child: Padding(
              padding: const EdgeInsets.all(26),
              child: Column(mainAxisSize: MainAxisSize.min, children: [
                const Text('🏆', style: TextStyle(fontSize: 90)),
                Text('النجوم: $stars', style: const TextStyle(fontSize: 34, fontWeight: FontWeight.w900)),
                const SizedBox(height: 18),
                Text(cup, textAlign: TextAlign.center, style: const TextStyle(fontSize: 28, fontWeight: FontWeight.bold)),
              ]),
            ),
          ),
        ),
      ),
    );
  }
}
