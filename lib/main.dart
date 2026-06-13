import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter_tts/flutter_tts.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() {
  runApp(const KidsApp());
}

class KidsApp extends StatelessWidget {
  const KidsApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'طفلي الأول',
      theme: ThemeData(
        fontFamily: 'Roboto',
        scaffoldBackgroundColor: const Color(0xFFFFF7ED),
        colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFFFF9F7A)),
        useMaterial3: true,
      ),
      home: const HomePage(),
    );
  }
}

class TtsService {
  static final FlutterTts tts = FlutterTts();

  static Future<void> say(String text, {bool english = false}) async {
    await tts.stop();
    await tts.setSpeechRate(0.42);
    await tts.setPitch(1.05);
    await tts.setLanguage(english ? 'en-US' : 'ar');
    await tts.speak(text);
  }

  static Future<void> sayBoth(String arabic, String english) async {
    await say(arabic);
    await Future.delayed(const Duration(milliseconds: 1200));
    await say(english, english: true);
  }
}

class ProgressStore {
  static const String key = 'stars';

  static Future<int> getStars() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getInt(key) ?? 0;
  }

  static Future<int> addStar() async {
    final prefs = await SharedPreferences.getInstance();
    final next = (prefs.getInt(key) ?? 0) + 1;
    await prefs.setInt(key, next);
    return next;
  }

  static Future<void> resetStars() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setInt(key, 0);
  }
}

class LearnItem {
  final String emoji;
  final String ar;
  final String en;

  const LearnItem(this.emoji, this.ar, this.en);
}

const List<LearnItem> animals = [
  LearnItem('🦁', 'أسد', 'Lion'),
  LearnItem('🐘', 'فيل', 'Elephant'),
  LearnItem('🐱', 'قطة', 'Cat'),
  LearnItem('🐶', 'كلب', 'Dog'),
  LearnItem('🐴', 'حصان', 'Horse'),
  LearnItem('🐰', 'أرنب', 'Rabbit'),
  LearnItem('🐮', 'بقرة', 'Cow'),
  LearnItem('🐔', 'دجاجة', 'Chicken'),
  LearnItem('🐵', 'قرد', 'Monkey'),
  LearnItem('🦒', 'زرافة', 'Giraffe'),
  LearnItem('🐯', 'نمر', 'Tiger'),
  LearnItem('🐻', 'دب', 'Bear'),
  LearnItem('🦆', 'بطة', 'Duck'),
  LearnItem('🐑', 'خروف', 'Sheep'),
  LearnItem('🐟', 'سمكة', 'Fish'),
  LearnItem('🐢', 'سلحفاة', 'Turtle'),
  LearnItem('🐦', 'عصفور', 'Bird'),
  LearnItem('🐪', 'جمل', 'Camel'),
  LearnItem('🐐', 'ماعز', 'Goat'),
  LearnItem('🦓', 'حمار وحشي', 'Zebra'),
];

const List<LearnItem> colors = [
  LearnItem('🔴', 'أحمر', 'Red'),
  LearnItem('🔵', 'أزرق', 'Blue'),
  LearnItem('🟢', 'أخضر', 'Green'),
  LearnItem('🟡', 'أصفر', 'Yellow'),
  LearnItem('⚫', 'أسود', 'Black'),
  LearnItem('⚪', 'أبيض', 'White'),
  LearnItem('🟤', 'بني', 'Brown'),
  LearnItem('🟣', 'بنفسجي', 'Purple'),
  LearnItem('🟠', 'برتقالي', 'Orange'),
  LearnItem('🌸', 'وردي', 'Pink'),
];

const List<LearnItem> arabicLetters = [
  LearnItem('🦁', 'أ - أسد', 'أ'),
  LearnItem('🦆', 'ب - بطة', 'ب'),
  LearnItem('🍎', 'ت - تفاحة', 'ت'),
  LearnItem('🐍', 'ث - ثعبان', 'ث'),
  LearnItem('🐪', 'ج - جمل', 'ج'),
  LearnItem('🐴', 'ح - حصان', 'ح'),
  LearnItem('🥒', 'خ - خيار', 'خ'),
  LearnItem('🐔', 'د - دجاجة', 'د'),
  LearnItem('🌽', 'ذ - ذرة', 'ذ'),
  LearnItem('🍊', 'ر - رمان', 'ر'),
  LearnItem('🌸', 'ز - زهرة', 'ز'),
  LearnItem('🐟', 'س - سمكة', 'س'),
  LearnItem('☀️', 'ش - شمس', 'ش'),
  LearnItem('🦅', 'ص - صقر', 'ص'),
  LearnItem('🐸', 'ض - ضفدع', 'ض'),
  LearnItem('🛩️', 'ط - طائرة', 'ط'),
  LearnItem('📩', 'ظ - ظرف', 'ظ'),
  LearnItem('👁️', 'ع - عين', 'ع'),
  LearnItem('🌳', 'غ - غزال', 'غ'),
  LearnItem('🐘', 'ف - فيل', 'ف'),
  LearnItem('🐱', 'ق - قطة', 'ق'),
  LearnItem('📘', 'ك - كتاب', 'ك'),
  LearnItem('🍋', 'ل - ليمون', 'ل'),
  LearnItem('🍌', 'م - موز', 'م'),
  LearnItem('🐝', 'ن - نحلة', 'ن'),
  LearnItem('🎁', 'ه - هدية', 'ه'),
  LearnItem('🌹', 'و - وردة', 'و'),
  LearnItem('✋', 'ي - يد', 'ي'),
];

const List<LearnItem> englishLetters = [
  LearnItem('🍎', 'A', 'Apple'),
  LearnItem('⚽', 'B', 'Ball'),
  LearnItem('🐱', 'C', 'Cat'),
  LearnItem('🐶', 'D', 'Dog'),
  LearnItem('🐘', 'E', 'Elephant'),
  LearnItem('🐟', 'F', 'Fish'),
  LearnItem('🐐', 'G', 'Goat'),
  LearnItem('🏠', 'H', 'House'),
  LearnItem('🍦', 'I', 'Ice cream'),
  LearnItem('🧃', 'J', 'Juice'),
  LearnItem('🔑', 'K', 'Key'),
  LearnItem('🦁', 'L', 'Lion'),
  LearnItem('🐵', 'M', 'Monkey'),
  LearnItem('📒', 'N', 'Notebook'),
  LearnItem('🍊', 'O', 'Orange'),
  LearnItem('✏️', 'P', 'Pencil'),
  LearnItem('👑', 'Q', 'Queen'),
  LearnItem('🐰', 'R', 'Rabbit'),
  LearnItem('☀️', 'S', 'Sun'),
  LearnItem('🐯', 'T', 'Tiger'),
  LearnItem('☂️', 'U', 'Umbrella'),
  LearnItem('🎻', 'V', 'Violin'),
  LearnItem('⌚', 'W', 'Watch'),
  LearnItem('❌', 'X', 'X-ray'),
  LearnItem('🪀', 'Y', 'Yo-yo'),
  LearnItem('🦓', 'Z', 'Zebra'),
];

const List<String> arabicNumbers = [
  'واحد',
  'اثنان',
  'ثلاثة',
  'أربعة',
  'خمسة',
  'ستة',
  'سبعة',
  'ثمانية',
  'تسعة',
  'عشرة',
];

const List<String> englishNumbers = [
  'One',
  'Two',
  'Three',
  'Four',
  'Five',
  'Six',
  'Seven',
  'Eight',
  'Nine',
  'Ten',
];

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    final cards = [
      MenuEntry('🇸🇦', 'العربية', MenuPage(title: 'العربية', cards: arabicMenu())),
      MenuEntry('🇬🇧', 'English', MenuPage(title: 'English', cards: englishMenu())),
      const MenuEntry('🎮', 'الألعاب', GamesPage()),
      const MenuEntry('🏆', 'إنجازاتي', AchievementsPage()),
    ];

    return Scaffold(
      appBar: AppBar(
        title: const Text('طفلي الأول'),
        centerTitle: true,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: GridView.count(
          crossAxisCount: 2,
          mainAxisSpacing: 14,
          crossAxisSpacing: 14,
          children: cards.map((entry) => MenuCard(entry: entry)).toList(),
        ),
      ),
    );
  }
}

List<MenuEntry> arabicMenu() {
  return const [
    MenuEntry('🔤', 'الحروف العربية', LearnGrid(title: 'الحروف العربية', items: arabicLetters, mode: 'arabicLetters')),
    MenuEntry('🔢', 'الأرقام 1 - 10', NumbersPage(arabic: true)),
    MenuEntry('🎨', 'الألوان', LearnGrid(title: 'الألوان', items: colors, mode: 'colorsAr')),
    MenuEntry('🐶', 'الحيوانات عربي + English', LearnGrid(title: 'الحيوانات', items: animals, mode: 'animals')),
  ];
}

List<MenuEntry> englishMenu() {
  return const [
    MenuEntry('🔠', 'Capital Letters', LearnGrid(title: 'Capital Letters', items: englishLetters, mode: 'englishLetters')),
    MenuEntry('🔢', 'Numbers 1 - 10', NumbersPage(arabic: false)),
    MenuEntry('🎨', 'Colors', LearnGrid(title: 'Colors', items: colors, mode: 'colorsEn')),
    MenuEntry('🐶', 'Animals عربي + English', LearnGrid(title: 'Animals', items: animals, mode: 'animals')),
  ];
}

class MenuEntry {
  final String icon;
  final String title;
  final Widget page;

  const MenuEntry(this.icon, this.title, this.page);
}

class MenuCard extends StatelessWidget {
  final MenuEntry entry;

  const MenuCard({super.key, required this.entry});

  @override
  Widget build(BuildContext context) {
    return InkWell(
      borderRadius: BorderRadius.circular(28),
      onTap: () {
        Navigator.push(context, MaterialPageRoute(builder: (_) => entry.page));
      },
      child: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(28),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withValues(alpha: 0.08),
              blurRadius: 14,
              offset: const Offset(0, 8),
            ),
          ],
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(entry.icon, style: const TextStyle(fontSize: 54)),
            const SizedBox(height: 10),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 8),
              child: Text(
                entry.title,
                textAlign: TextAlign.center,
                style: const TextStyle(fontSize: 22, fontWeight: FontWeight.w900),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class MenuPage extends StatelessWidget {
  final String title;
  final List<MenuEntry> cards;

  const MenuPage({super.key, required this.title, required this.cards});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(title), centerTitle: true),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: GridView.count(
          crossAxisCount: 2,
          mainAxisSpacing: 14,
          crossAxisSpacing: 14,
          children: cards.map((entry) => MenuCard(entry: entry)).toList(),
        ),
      ),
    );
  }
}

class LearnGrid extends StatelessWidget {
  final String title;
  final String mode;
  final List<LearnItem> items;

  const LearnGrid({
    super.key,
    required this.title,
    required this.items,
    required this.mode,
  });

  Future<void> speak(LearnItem item) async {
    if (mode == 'animals') {
      await TtsService.sayBoth(item.ar, item.en);
    } else if (mode == 'colorsAr') {
      await TtsService.say(item.ar);
    } else if (mode == 'colorsEn') {
      await TtsService.say(item.en, english: true);
    } else if (mode == 'englishLetters') {
      await TtsService.say(item.ar, english: true);
      await Future.delayed(const Duration(milliseconds: 900));
      await TtsService.say(item.en, english: true);
    } else {
      await TtsService.say(item.ar);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(title), centerTitle: true),
      body: Padding(
        padding: const EdgeInsets.all(12),
        child: GridView.builder(
          itemCount: items.length,
          gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 2,
            mainAxisSpacing: 12,
            crossAxisSpacing: 12,
            childAspectRatio: 0.82,
          ),
          itemBuilder: (_, index) => ItemCard(
            item: items[index],
            mode: mode,
            onTap: () => speak(items[index]),
          ),
        ),
      ),
    );
  }
}

class ItemCard extends StatelessWidget {
  final LearnItem item;
  final String mode;
  final VoidCallback onTap;

  const ItemCard({
    super.key,
    required this.item,
    required this.mode,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    String mainText = item.ar;
    String subText = '';

    if (mode == 'animals') {
      mainText = item.ar;
      subText = item.en;
    } else if (mode == 'colorsEn') {
      mainText = item.en;
    } else if (mode == 'englishLetters') {
      mainText = item.ar;
      subText = item.en;
    }

    return InkWell(
      borderRadius: BorderRadius.circular(26),
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(26),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withValues(alpha: 0.08),
              blurRadius: 12,
              offset: const Offset(0, 7),
            ),
          ],
        ),
        padding: const EdgeInsets.all(12),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(item.emoji, style: const TextStyle(fontSize: 58)),
            const SizedBox(height: 10),
            Text(
              mainText,
              textAlign: TextAlign.center,
              style: const TextStyle(fontSize: 24, fontWeight: FontWeight.w900),
            ),
            if (subText.isNotEmpty) ...[
              const SizedBox(height: 6),
              Text(
                subText,
                textAlign: TextAlign.center,
                style: const TextStyle(fontSize: 20, fontWeight: FontWeight.w700),
              ),
            ],
            const SizedBox(height: 8),
            const Icon(Icons.volume_up_rounded),
          ],
        ),
      ),
    );
  }
}

class NumbersPage extends StatelessWidget {
  final bool arabic;

  const NumbersPage({super.key, required this.arabic});

  Future<void> speakNumber(int number) async {
    final text = arabic ? arabicNumbers[number - 1] : englishNumbers[number - 1];
    await TtsService.say(text, english: !arabic);
  }

  @override
  Widget build(BuildContext context) {
    final title = arabic ? 'الأرقام' : 'Numbers';

    return Scaffold(
      appBar: AppBar(title: Text(title), centerTitle: true),
      body: Padding(
        padding: const EdgeInsets.all(12),
        child: GridView.builder(
          itemCount: 10,
          gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 2,
            mainAxisSpacing: 12,
            crossAxisSpacing: 12,
            childAspectRatio: 0.9,
          ),
          itemBuilder: (_, index) {
            final number = index + 1;
            final word = arabic ? arabicNumbers[index] : englishNumbers[index];

            return InkWell(
              borderRadius: BorderRadius.circular(26),
              onTap: () => speakNumber(number),
              child: Container(
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(26),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withValues(alpha: 0.08),
                      blurRadius: 12,
                      offset: const Offset(0, 7),
                    ),
                  ],
                ),
                padding: const EdgeInsets.all(14),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      '$number',
                      style: const TextStyle(fontSize: 58, fontWeight: FontWeight.w900),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      word,
                      textAlign: TextAlign.center,
                      style: const TextStyle(fontSize: 24, fontWeight: FontWeight.w800),
                    ),
                    const SizedBox(height: 10),
                    Text(
                      List.filled(number, '🍎').join(' '),
                      textAlign: TextAlign.center,
                      maxLines: 2,
                      style: const TextStyle(fontSize: 20),
                    ),
                    const SizedBox(height: 8),
                    const Icon(Icons.volume_up_rounded),
                  ],
                ),
              ),
            );
          },
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
  final Random random = Random();
  late LearnItem answer;
  late List<LearnItem> options;
  String msg = 'اختر الحيوان الصحيح';
  int stars = 0;

  @override
  void initState() {
    super.initState();
    _loadStars();
    next();
  }

  Future<void> _loadStars() async {
    final current = await ProgressStore.getStars();
    if (mounted) {
      setState(() => stars = current);
    }
  }

  void next() {
    final shuffled = [...animals]..shuffle(random);
    answer = shuffled.first;
    options = shuffled.take(4).toList()..shuffle(random);
  }

  Future<void> pick(LearnItem item) async {
    if (item.ar == answer.ar) {
      final updatedStars = await ProgressStore.addStar();
      await TtsService.say('أحسنت');
      if (mounted) {
        setState(() {
          stars = updatedStars;
          msg = '🎉 أحسنت! اختر التالي';
          next();
        });
      }
    } else {
      await TtsService.say('حاول مرة أخرى');
      if (mounted) {
        setState(() => msg = '🙂 حاول مرة أخرى');
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('الألعاب  ⭐ $stars'),
        centerTitle: true,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            Text(
              msg,
              textAlign: TextAlign.center,
              style: const TextStyle(fontSize: 24, fontWeight: FontWeight.w900),
            ),
            const SizedBox(height: 12),
            Text(
              'أين ${answer.ar}؟',
              style: const TextStyle(fontSize: 30, fontWeight: FontWeight.w900),
            ),
            const SizedBox(height: 18),
            Expanded(
              child: GridView.count(
                crossAxisCount: 2,
                mainAxisSpacing: 12,
                crossAxisSpacing: 12,
                children: options.map((item) {
                  return InkWell(
                    borderRadius: BorderRadius.circular(26),
                    onTap: () => pick(item),
                    child: Container(
                      decoration: BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(26),
                        boxShadow: [
                          BoxShadow(
                            color: Colors.black.withValues(alpha: 0.08),
                            blurRadius: 12,
                            offset: const Offset(0, 7),
                          ),
                        ],
                      ),
                      child: Center(
                        child: Text(item.emoji, style: const TextStyle(fontSize: 72)),
                      ),
                    ),
                  );
                }).toList(),
              ),
            ),
          ],
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
    _loadStars();
  }

  Future<void> _loadStars() async {
    final current = await ProgressStore.getStars();
    if (mounted) {
      setState(() => stars = current);
    }
  }

  String get cup {
    if (stars >= 50) return '🥇 كأس ذهبي';
    if (stars >= 25) return '🥈 كأس فضي';
    if (stars >= 10) return '🥉 كأس برونزي';
    return '⭐ اجمع 10 نجوم لتحصل على كأس';
  }

  Future<void> reset() async {
    await ProgressStore.resetStars();
    if (mounted) {
      setState(() => stars = 0);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('إنجازاتي'),
        centerTitle: true,
      ),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(22),
          child: Container(
            width: double.infinity,
            padding: const EdgeInsets.all(24),
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(30),
              boxShadow: [
                BoxShadow(
                  color: Colors.black.withValues(alpha: 0.08),
                  blurRadius: 14,
                  offset: const Offset(0, 8),
                ),
              ],
            ),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                const Text('🏆', style: TextStyle(fontSize: 80)),
                const SizedBox(height: 14),
                Text(
                  'النجوم: $stars ⭐',
                  style: const TextStyle(fontSize: 30, fontWeight: FontWeight.w900),
                ),
                const SizedBox(height: 16),
                Text(
                  cup,
                  textAlign: TextAlign.center,
                  style: const TextStyle(fontSize: 26, fontWeight: FontWeight.w800),
                ),
                const SizedBox(height: 24),
                ElevatedButton.icon(
                  onPressed: reset,
                  icon: const Icon(Icons.refresh_rounded),
                  label: const Text('تصفير النجوم'),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
