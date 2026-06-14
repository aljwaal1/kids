import 'dart:math';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
  runApp(const KidsApp());
}

const MethodChannel _channel = MethodChannel('kids_audio');

Future<void> play(String fileName) async {
  try {
    await _channel.invokeMethod('playAsset', {'file': 'assets/audio/$fileName'});
  } catch (_) {
    SystemSound.play(SystemSoundType.click);
  }
  HapticFeedback.selectionClick();
}

Future<int> loadSavedInt(String key) async {
  try {
    final value = await _channel.invokeMethod<int>('loadInt', {'key': key});
    return value ?? 0;
  } catch (_) {
    return 0;
  }
}

Future<void> saveIntValue(String key, int value) async {
  try {
    await _channel.invokeMethod('saveInt', {'key': key, 'value': value});
  } catch (_) {}
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

class LearnItem {
  final String title;
  final String sub;
  final String emoji;
  final String sound;

  const LearnItem(this.title, this.sub, this.emoji, this.sound);
}

const arLetters = [
  LearnItem('أ', 'أسد', '🦁', 'ar_letter.wav'),
  LearnItem('ب', 'بطة', '🦆', 'ar_letter.wav'),
  LearnItem('ت', 'تفاحة', '🍎', 'ar_letter.wav'),
  LearnItem('ث', 'ثعلب', '🦊', 'ar_letter.wav'),
  LearnItem('ج', 'جمل', '🐪', 'ar_letter.wav'),
  LearnItem('ح', 'حصان', '🐴', 'ar_letter.wav'),
  LearnItem('خ', 'خروف', '🐑', 'ar_letter.wav'),
  LearnItem('د', 'دجاجة', '🐔', 'ar_letter.wav'),
  LearnItem('ذ', 'ذرة', '🌽', 'ar_letter.wav'),
  LearnItem('ر', 'رمان', '🍎', 'ar_letter.wav'),
  LearnItem('ز', 'زرافة', '🦒', 'ar_letter.wav'),
  LearnItem('س', 'سمكة', '🐟', 'ar_letter.wav'),
  LearnItem('ش', 'شمس', '☀️', 'ar_letter.wav'),
  LearnItem('ص', 'صقر', '🦅', 'ar_letter.wav'),
  LearnItem('ض', 'ضفدع', '🐸', 'ar_letter.wav'),
  LearnItem('ط', 'طائرة', '✈️', 'ar_letter.wav'),
  LearnItem('ظ', 'ظرف', '✉️', 'ar_letter.wav'),
  LearnItem('ع', 'عصفور', '🐦', 'ar_letter.wav'),
  LearnItem('غ', 'غزال', '🦌', 'ar_letter.wav'),
  LearnItem('ف', 'فيل', '🐘', 'ar_letter.wav'),
  LearnItem('ق', 'قطة', '🐱', 'ar_letter.wav'),
  LearnItem('ك', 'كلب', '🐶', 'ar_letter.wav'),
  LearnItem('ل', 'ليمون', '🍋', 'ar_letter.wav'),
  LearnItem('م', 'موز', '🍌', 'ar_letter.wav'),
  LearnItem('ن', 'نمر', '🐯', 'ar_letter.wav'),
  LearnItem('هـ', 'هلال', '🌙', 'ar_letter.wav'),
  LearnItem('و', 'وردة', '🌹', 'ar_letter.wav'),
  LearnItem('ي', 'يد', '✋', 'ar_letter.wav'),
];

const enLetters = [
  LearnItem('A', 'Apple', '🍎', 'en_letter.wav'),
  LearnItem('B', 'Ball', '⚽', 'en_letter.wav'),
  LearnItem('C', 'Cat', '🐱', 'en_letter.wav'),
  LearnItem('D', 'Dog', '🐶', 'en_letter.wav'),
  LearnItem('E', 'Elephant', '🐘', 'en_letter.wav'),
  LearnItem('F', 'Fish', '🐟', 'en_letter.wav'),
  LearnItem('G', 'Giraffe', '🦒', 'en_letter.wav'),
  LearnItem('H', 'Horse', '🐴', 'en_letter.wav'),
  LearnItem('I', 'Ice Cream', '🍦', 'en_letter.wav'),
  LearnItem('J', 'Juice', '🧃', 'en_letter.wav'),
  LearnItem('K', 'Kite', '🪁', 'en_letter.wav'),
  LearnItem('L', 'Lion', '🦁', 'en_letter.wav'),
  LearnItem('M', 'Monkey', '🐒', 'en_letter.wav'),
  LearnItem('N', 'Nest', '🪺', 'en_letter.wav'),
  LearnItem('O', 'Orange', '🍊', 'en_letter.wav'),
  LearnItem('P', 'Panda', '🐼', 'en_letter.wav'),
  LearnItem('Q', 'Queen', '👑', 'en_letter.wav'),
  LearnItem('R', 'Rabbit', '🐰', 'en_letter.wav'),
  LearnItem('S', 'Sun', '☀️', 'en_letter.wav'),
  LearnItem('T', 'Tiger', '🐯', 'en_letter.wav'),
  LearnItem('U', 'Umbrella', '☂️', 'en_letter.wav'),
  LearnItem('V', 'Van', '🚐', 'en_letter.wav'),
  LearnItem('W', 'Water', '💧', 'en_letter.wav'),
  LearnItem('X', 'Xylophone', '🎼', 'en_letter.wav'),
  LearnItem('Y', 'Yo-yo', '🪀', 'en_letter.wav'),
  LearnItem('Z', 'Zebra', '🦓', 'en_letter.wav'),
];

const arNums = [
  LearnItem('1', 'واحد', '⭐', 'number.wav'),
  LearnItem('2', 'اثنان', '⭐⭐', 'number.wav'),
  LearnItem('3', 'ثلاثة', '⭐⭐⭐', 'number.wav'),
  LearnItem('4', 'أربعة', '⭐⭐⭐⭐', 'number.wav'),
  LearnItem('5', 'خمسة', '⭐⭐⭐⭐⭐', 'number.wav'),
  LearnItem('6', 'ستة', '🍎🍎🍎🍎🍎🍎', 'number.wav'),
  LearnItem('7', 'سبعة', '🍎🍎🍎🍎🍎🍎🍎', 'number.wav'),
  LearnItem('8', 'ثمانية', '🍎🍎🍎🍎🍎🍎🍎🍎', 'number.wav'),
  LearnItem('9', 'تسعة', '🍎🍎🍎🍎🍎🍎🍎🍎🍎', 'number.wav'),
  LearnItem('10', 'عشرة', '🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎', 'number.wav'),
];

const enNums = [
  LearnItem('1', 'One', '⭐', 'number.wav'),
  LearnItem('2', 'Two', '⭐⭐', 'number.wav'),
  LearnItem('3', 'Three', '⭐⭐⭐', 'number.wav'),
  LearnItem('4', 'Four', '⭐⭐⭐⭐', 'number.wav'),
  LearnItem('5', 'Five', '⭐⭐⭐⭐⭐', 'number.wav'),
  LearnItem('6', 'Six', '🍎🍎🍎🍎🍎🍎', 'number.wav'),
  LearnItem('7', 'Seven', '🍎🍎🍎🍎🍎🍎🍎', 'number.wav'),
  LearnItem('8', 'Eight', '🍎🍎🍎🍎🍎🍎🍎🍎', 'number.wav'),
  LearnItem('9', 'Nine', '🍎🍎🍎🍎🍎🍎🍎🍎🍎', 'number.wav'),
  LearnItem('10', 'Ten', '🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎', 'number.wav'),
];

const arColors = [
  LearnItem('أحمر', '', '🔴', 'color.wav'),
  LearnItem('أزرق', '', '🔵', 'color.wav'),
  LearnItem('أخضر', '', '🟢', 'color.wav'),
  LearnItem('أصفر', '', '🟡', 'color.wav'),
  LearnItem('أسود', '', '⚫', 'color.wav'),
  LearnItem('أبيض', '', '⚪', 'color.wav'),
  LearnItem('بني', '', '🟤', 'color.wav'),
  LearnItem('بنفسجي', '', '🟣', 'color.wav'),
];

const enColors = [
  LearnItem('Red', '', '🔴', 'color.wav'),
  LearnItem('Blue', '', '🔵', 'color.wav'),
  LearnItem('Green', '', '🟢', 'color.wav'),
  LearnItem('Yellow', '', '🟡', 'color.wav'),
  LearnItem('Black', '', '⚫', 'color.wav'),
  LearnItem('White', '', '⚪', 'color.wav'),
  LearnItem('Brown', '', '🟤', 'color.wav'),
  LearnItem('Purple', '', '🟣', 'color.wav'),
];

const animals = [
  LearnItem('أسد', 'Lion', '🦁', 'animal.wav'),
  LearnItem('فيل', 'Elephant', '🐘', 'animal.wav'),
  LearnItem('قطة', 'Cat', '🐱', 'animal.wav'),
  LearnItem('كلب', 'Dog', '🐶', 'animal.wav'),
  LearnItem('حصان', 'Horse', '🐴', 'animal.wav'),
  LearnItem('أرنب', 'Rabbit', '🐰', 'animal.wav'),
  LearnItem('بقرة', 'Cow', '🐮', 'animal.wav'),
  LearnItem('قرد', 'Monkey', '🐒', 'animal.wav'),
  LearnItem('زرافة', 'Giraffe', '🦒', 'animal.wav'),
  LearnItem('نمر', 'Tiger', '🐯', 'animal.wav'),
  LearnItem('دب', 'Bear', '🐻', 'animal.wav'),
  LearnItem('سمكة', 'Fish', '🐟', 'animal.wav'),
];

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  void open(BuildContext context, Widget page) {
    play('tap.wav');
    Navigator.push(context, MaterialPageRoute(builder: (_) => page));
  }

  @override
  Widget build(BuildContext context) {
    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        appBar: AppBar(title: const Text('طفلي الأول V4')),
        body: SafeArea(
          child: ListView(
            padding: const EdgeInsets.all(16),
            children: [
              const Text(
                'تعلم والعب',
                textAlign: TextAlign.center,
                style: TextStyle(fontSize: 36, fontWeight: FontWeight.w900),
              ),
              const SizedBox(height: 14),
              HomeButton('🇸🇦', 'العربية', () => open(context, const ArabicPage())),
              HomeButton('🇬🇧', 'English', () => open(context, const EnglishPage())),
              HomeButton('🐶', 'الحيوانات عربي + English', () => open(context, const LearnGrid(title: 'الحيوانات', items: animals, rtl: true))),
              HomeButton('🎮', 'الألعاب التعليمية', () => open(context, const GamesMenuPage())),
              HomeButton('🏆', 'إنجازاتي', () => open(context, const AchievementsPage())),
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

  const HomeButton(this.icon, this.text, this.onTap, {super.key});

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
              Expanded(
                child: Text(text, style: const TextStyle(fontSize: 26, fontWeight: FontWeight.bold)),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class ArabicPage extends StatelessWidget {
  const ArabicPage({super.key});

  void open(BuildContext context, Widget page) {
    play('tap.wav');
    Navigator.push(context, MaterialPageRoute(builder: (_) => page));
  }

  @override
  Widget build(BuildContext context) {
    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        appBar: AppBar(title: const Text('العربية')),
        body: ListView(
          padding: const EdgeInsets.all(16),
          children: [
            HomeButton('🔤', 'الحروف العربية', () => open(context, const LearnGrid(title: 'الحروف العربية', items: arLetters, rtl: true))),
            HomeButton('🔢', 'الأرقام 1 - 10', () => open(context, const LearnGrid(title: 'الأرقام', items: arNums, rtl: true))),
            HomeButton('🎨', 'الألوان', () => open(context, const LearnGrid(title: 'الألوان', items: arColors, rtl: true))),
            HomeButton('🐾', 'الحيوانات', () => open(context, const LearnGrid(title: 'الحيوانات', items: animals, rtl: true))),
          ],
        ),
      ),
    );
  }
}

class EnglishPage extends StatelessWidget {
  const EnglishPage({super.key});

  void open(BuildContext context, Widget page) {
    play('tap.wav');
    Navigator.push(context, MaterialPageRoute(builder: (_) => page));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('English')),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          HomeButton('🔠', 'Capital Letters', () => open(context, const LearnGrid(title: 'Capital Letters', items: enLetters, rtl: false))),
          HomeButton('🔢', 'Numbers 1 - 10', () => open(context, const LearnGrid(title: 'Numbers', items: enNums, rtl: false))),
          HomeButton('🎨', 'Colors', () => open(context, const LearnGrid(title: 'Colors', items: enColors, rtl: false))),
          HomeButton('🐾', 'Animals', () => open(context, const LearnGrid(title: 'Animals', items: animals, rtl: false))),
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
            childAspectRatio: .88,
          ),
          itemBuilder: (_, i) => LearnCard(items[i]),
        ),
      ),
    );
  }
}

class LearnCard extends StatelessWidget {
  final LearnItem item;

  const LearnCard(this.item, {super.key});

  @override
  Widget build(BuildContext context) {
    return Card(
      color: Colors.white,
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(26)),
      child: InkWell(
        borderRadius: BorderRadius.circular(26),
        onTap: () => play(item.sound),
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
                if (item.sub.isNotEmpty) ...[
                  const SizedBox(height: 6),
                  Text(item.sub, textAlign: TextAlign.center, style: const TextStyle(fontSize: 25, fontWeight: FontWeight.bold, color: Color(0xff555555))),
                ],
              ],
            ),
          ),
        ),
      ),
    );
  }
}

enum QuizKind { arabicLetters, englishLetters, arabicNumbers, englishNumbers, animals }

class QuizConfig {
  final String title;
  final String icon;
  final String questionPrefix;
  final List<LearnItem> items;
  final QuizKind kind;
  final bool rtl;

  const QuizConfig({
    required this.title,
    required this.icon,
    required this.questionPrefix,
    required this.items,
    required this.kind,
    required this.rtl,
  });
}

class GamesMenuPage extends StatelessWidget {
  const GamesMenuPage({super.key});

  void open(BuildContext context, QuizConfig config) {
    play('tap.wav');
    Navigator.push(context, MaterialPageRoute(builder: (_) => QuizPage(config: config)));
  }

  @override
  Widget build(BuildContext context) {
    const configs = [
      QuizConfig(
        title: 'لعبة الحروف العربية',
        icon: '🔤',
        questionPrefix: 'أين حرف',
        items: arLetters,
        kind: QuizKind.arabicLetters,
        rtl: true,
      ),
      QuizConfig(
        title: 'لعبة الأرقام العربية',
        icon: '🔢',
        questionPrefix: 'أين الرقم',
        items: arNums,
        kind: QuizKind.arabicNumbers,
        rtl: true,
      ),
      QuizConfig(
        title: 'English Letters Game',
        icon: '🔠',
        questionPrefix: 'Where is',
        items: enLetters,
        kind: QuizKind.englishLetters,
        rtl: false,
      ),
      QuizConfig(
        title: 'English Numbers Game',
        icon: '🔢',
        questionPrefix: 'Where is',
        items: enNums,
        kind: QuizKind.englishNumbers,
        rtl: false,
      ),
      QuizConfig(
        title: 'لعبة الحيوانات',
        icon: '🐶',
        questionPrefix: 'أين',
        items: animals,
        kind: QuizKind.animals,
        rtl: true,
      ),
    ];

    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        appBar: AppBar(title: const Text('الألعاب التعليمية')),
        body: ListView(
          padding: const EdgeInsets.all(16),
          children: [
            const Text('اختر اللعبة', textAlign: TextAlign.center, style: TextStyle(fontSize: 32, fontWeight: FontWeight.w900)),
            const SizedBox(height: 16),
            for (final config in configs) HomeButton(config.icon, config.title, () => open(context, config)),
          ],
        ),
      ),
    );
  }
}

class QuizPage extends StatefulWidget {
  final QuizConfig config;

  const QuizPage({super.key, required this.config});

  @override
  State<QuizPage> createState() => _QuizPageState();
}

class _QuizPageState extends State<QuizPage> {
  final Random _random = Random();
  late LearnItem _target;
  late List<LearnItem> _options;
  String _message = '';
  int _stars = 0;
  int _bestScore = 0;
  int _correctAnswers = 0;
  int _currentScore = 0;
  String _lastTarget = '';

  @override
  void initState() {
    super.initState();
    _target = widget.config.items.first;
    _options = widget.config.items.take(4).toList();
    _message = widget.config.rtl ? 'اختر الإجابة الصحيحة' : 'Choose the correct answer';
    _loadProgress();
    _newQuestion();
  }

  Future<void> _loadProgress() async {
    final stars = await loadSavedInt('stars');
    final best = await loadSavedInt('best_score');
    final correct = await loadSavedInt('correct_answers');
    if (!mounted) return;
    setState(() {
      _stars = stars;
      _bestScore = best;
      _correctAnswers = correct;
    });
  }

  void _newQuestion() {
    final all = List<LearnItem>.from(widget.config.items);
    LearnItem nextTarget = all[_random.nextInt(all.length)];
    if (all.length > 1) {
      int guard = 0;
      while (nextTarget.title == _lastTarget && guard < 20) {
        nextTarget = all[_random.nextInt(all.length)];
        guard++;
      }
    }
    _lastTarget = nextTarget.title;

    final others = all.where((item) => item.title != nextTarget.title).toList()..shuffle(_random);
    final newOptions = <LearnItem>[nextTarget, ...others.take(3)]..shuffle(_random);

    setState(() {
      _target = nextTarget;
      _options = newOptions;
    });
  }

  String get _questionText {
    if (widget.config.kind == QuizKind.animals) {
      return '${widget.config.questionPrefix} ${_target.title}؟';
    }
    if (widget.config.rtl) {
      return '${widget.config.questionPrefix} ${_target.title}؟';
    }
    return '${widget.config.questionPrefix} ${_target.title}?';
  }

  Future<void> _answer(LearnItem item) async {
    final isCorrect = item.title == _target.title;

    if (isCorrect) {
      final newStars = _stars + 1;
      final newScore = _currentScore + 1;
      final newCorrectAnswers = _correctAnswers + 1;
      final newBest = max(_bestScore, newScore);

      setState(() {
        _stars = newStars;
        _currentScore = newScore;
        _correctAnswers = newCorrectAnswers;
        _bestScore = newBest;
        _message = widget.config.rtl ? 'أحسنت يا بطل ⭐' : 'Great job ⭐';
      });

      await saveIntValue('stars', newStars);
      await saveIntValue('current_score', newScore);
      await saveIntValue('correct_answers', newCorrectAnswers);
      await saveIntValue('best_score', newBest);
      play('success.wav');

      Future.delayed(const Duration(milliseconds: 700), () {
        if (!mounted) return;
        _message = widget.config.rtl ? 'اختر الإجابة الصحيحة' : 'Choose the correct answer';
        _newQuestion();
      });
    } else {
      setState(() {
        _currentScore = 0;
        _message = widget.config.rtl ? 'حاول مرة أخرى' : 'Try again';
      });
      await saveIntValue('current_score', 0);
      play('wrong.wav');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Directionality(
      textDirection: widget.config.rtl ? TextDirection.rtl : TextDirection.ltr,
      child: Scaffold(
        appBar: AppBar(title: Text('${widget.config.title}  ⭐ $_stars')),
        body: SafeArea(
          child: Padding(
            padding: const EdgeInsets.all(16),
            child: Column(
              children: [
                Container(
                  width: double.infinity,
                  padding: const EdgeInsets.all(18),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(26),
                    boxShadow: const [BoxShadow(color: Color(0x22000000), blurRadius: 10, offset: Offset(0, 3))],
                  ),
                  child: Column(
                    children: [
                      Text(_questionText, textAlign: TextAlign.center, style: const TextStyle(fontSize: 30, fontWeight: FontWeight.w900)),
                      const SizedBox(height: 8),
                      Text(_message, textAlign: TextAlign.center, style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold, color: Color(0xff6C63FF))),
                      const SizedBox(height: 8),
                      Text('⭐ $_stars   🏆 $_bestScore', textAlign: TextAlign.center, style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold)),
                    ],
                  ),
                ),
                const SizedBox(height: 18),
                Expanded(
                  child: GridView.builder(
                    itemCount: _options.length,
                    gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 2,
                      mainAxisSpacing: 12,
                      crossAxisSpacing: 12,
                      childAspectRatio: .92,
                    ),
                    itemBuilder: (_, index) => QuizOptionCard(
                      item: _options[index],
                      showSubtitle: widget.config.kind == QuizKind.animals,
                      onTap: () => _answer(_options[index]),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class QuizOptionCard extends StatelessWidget {
  final LearnItem item;
  final bool showSubtitle;
  final VoidCallback onTap;

  const QuizOptionCard({super.key, required this.item, required this.showSubtitle, required this.onTap});

  @override
  Widget build(BuildContext context) {
    return Card(
      color: Colors.white,
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(26)),
      child: InkWell(
        borderRadius: BorderRadius.circular(26),
        onTap: onTap,
        child: Padding(
          padding: const EdgeInsets.all(10),
          child: FittedBox(
            fit: BoxFit.scaleDown,
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(item.emoji, style: const TextStyle(fontSize: 62)),
                const SizedBox(height: 8),
                Text(item.title, textAlign: TextAlign.center, style: const TextStyle(fontSize: 34, fontWeight: FontWeight.w900)),
                if (showSubtitle && item.sub.isNotEmpty) ...[
                  const SizedBox(height: 4),
                  Text(item.sub, textAlign: TextAlign.center, style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold, color: Color(0xff555555))),
                ],
              ],
            ),
          ),
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
  int _stars = 0;
  int _bestScore = 0;
  int _correctAnswers = 0;

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    final stars = await loadSavedInt('stars');
    final best = await loadSavedInt('best_score');
    final correct = await loadSavedInt('correct_answers');
    if (!mounted) return;
    setState(() {
      _stars = stars;
      _bestScore = best;
      _correctAnswers = correct;
    });
  }

  String get _medal {
    if (_stars >= 100) return '👑 بطل التعلم';
    if (_stars >= 50) return '🥇 كأس ذهبي';
    if (_stars >= 25) return '🥈 كأس فضي';
    if (_stars >= 10) return '🥉 كأس برونزي';
    return 'اجمع 10 نجوم لتحصل على أول ميدالية';
  }

  Future<void> _reset() async {
    await saveIntValue('stars', 0);
    await saveIntValue('best_score', 0);
    await saveIntValue('correct_answers', 0);
    await saveIntValue('current_score', 0);
    play('tap.wav');
    if (!mounted) return;
    setState(() {
      _stars = 0;
      _bestScore = 0;
      _correctAnswers = 0;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Directionality(
      textDirection: TextDirection.rtl,
      child: Scaffold(
        appBar: AppBar(title: const Text('إنجازاتي')),
        body: ListView(
          padding: const EdgeInsets.all(18),
          children: [
            Card(
              color: Colors.white,
              elevation: 2,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(30)),
              child: Padding(
                padding: const EdgeInsets.all(24),
                child: Column(
                  children: [
                    const Text('🏆', style: TextStyle(fontSize: 88)),
                    Text(_medal, textAlign: TextAlign.center, style: const TextStyle(fontSize: 30, fontWeight: FontWeight.w900)),
                    const SizedBox(height: 18),
                    StatRow(icon: '⭐', label: 'النجوم', value: _stars.toString()),
                    StatRow(icon: '🔥', label: 'أعلى نتيجة متتالية', value: _bestScore.toString()),
                    StatRow(icon: '✅', label: 'الإجابات الصحيحة', value: _correctAnswers.toString()),
                    const SizedBox(height: 18),
                    ElevatedButton.icon(
                      onPressed: _reset,
                      icon: const Icon(Icons.refresh),
                      label: const Text('تصفير الإنجازات'),
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class StatRow extends StatelessWidget {
  final String icon;
  final String label;
  final String value;

  const StatRow({super.key, required this.icon, required this.label, required this.value});

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.only(bottom: 10),
      padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 12),
      decoration: BoxDecoration(
        color: const Color(0xffFFF8EA),
        borderRadius: BorderRadius.circular(18),
      ),
      child: Row(
        children: [
          Text(icon, style: const TextStyle(fontSize: 28)),
          const SizedBox(width: 12),
          Expanded(child: Text(label, style: const TextStyle(fontSize: 21, fontWeight: FontWeight.bold))),
          Text(value, style: const TextStyle(fontSize: 24, fontWeight: FontWeight.w900)),
        ],
      ),
    );
  }
}
