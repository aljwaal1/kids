import 'dart:math';
import 'package:flutter/material.dart';
import 'package:flutter_tts/flutter_tts.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() => runApp(const KidsApp());

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
}

class ProgressStore {
  static Future<int> getStars() async => (await SharedPreferences.getInstance()).getInt('stars') ?? 0;
  static Future<int> addStar() async {
    final p = await SharedPreferences.getInstance();
    final s = (p.getInt('stars') ?? 0) + 1;
    await p.setInt('stars', s);
    return s;
  }
  static Future<void> reset() async => (await SharedPreferences.getInstance()).setInt('stars', 0);
}

class LearnItem {
  final String icon, ar, en, main;
  const LearnItem(this.icon, this.ar, this.en, this.main);
}

const arabicLetters = [
  LearnItem('🦁','أ','أسد','أ'), LearnItem('🦆','ب','بطة','ب'), LearnItem('🍎','ت','تفاحة','ت'), LearnItem('🐍','ث','ثعبان','ث'),
  LearnItem('🐪','ج','جمل','ج'), LearnItem('🐴','ح','حصان','ح'), LearnItem('🥒','خ','خيار','خ'), LearnItem('🐓','د','ديك','د'),
  LearnItem('🌽','ذ','ذرة','ذ'), LearnItem('🪶','ر','ريشة','ر'), LearnItem('🌸','ز','زهرة','ز'), LearnItem('🐟','س','سمكة','س'),
  LearnItem('☀️','ش','شمس','ش'), LearnItem('🦅','ص','صقر','ص'), LearnItem('🐸','ض','ضفدع','ض'), LearnItem('🥁','ط','طبل','ط'),
  LearnItem('📩','ظ','ظرف','ظ'), LearnItem('👁️','ع','عين','ع'), LearnItem('☁️','غ','غيمة','غ'), LearnItem('🐘','ف','فيل','ف'),
  LearnItem('🌙','ق','قمر','ق'), LearnItem('📖','ك','كتاب','ك'), LearnItem('🍋','ل','ليمون','ل'), LearnItem('🍌','م','موز','م'),
  LearnItem('⭐','ن','نجمة','ن'), LearnItem('🎁','ه','هدية','ه'), LearnItem('🌹','و','وردة','و'), LearnItem('✋','ي','يد','ي'),
];

const englishLetters = [
  LearnItem('🍎','A','Apple','A'), LearnItem('⚽','B','Ball','B'), LearnItem('🐱','C','Cat','C'), LearnItem('🐶','D','Dog','D'),
  LearnItem('🐘','E','Elephant','E'), LearnItem('🐟','F','Fish','F'), LearnItem('🦒','G','Giraffe','G'), LearnItem('🏠','H','House','H'),
  LearnItem('🍦','I','Ice cream','I'), LearnItem('🧃','J','Juice','J'), LearnItem('🪁','K','Kite','K'), LearnItem('🦁','L','Lion','L'),
  LearnItem('🐒','M','Monkey','M'), LearnItem('🪺','N','Nest','N'), LearnItem('🍊','O','Orange','O'), LearnItem('🐼','P','Panda','P'),
  LearnItem('👑','Q','Queen','Q'), LearnItem('🐰','R','Rabbit','R'), LearnItem('☀️','S','Sun','S'), LearnItem('🐢','T','Turtle','T'),
  LearnItem('☂️','U','Umbrella','U'), LearnItem('🚐','V','Van','V'), LearnItem('⌚','W','Watch','W'), LearnItem('🎼','X','Xylophone','X'),
  LearnItem('🛥️','Y','Yacht','Y'), LearnItem('🦓','Z','Zebra','Z'),
];

const animals = [
  LearnItem('🦁','أسد','Lion',''), LearnItem('🐘','فيل','Elephant',''), LearnItem('🐱','قطة','Cat',''), LearnItem('🐶','كلب','Dog',''),
  LearnItem('🐴','حصان','Horse',''), LearnItem('🐰','أرنب','Rabbit',''), LearnItem('🐄','بقرة','Cow',''), LearnItem('🐔','دجاجة','Chicken',''),
  LearnItem('🐒','قرد','Monkey',''), LearnItem('🦒','زرافة','Giraffe',''), LearnItem('🐯','نمر','Tiger',''), LearnItem('🐻','دب','Bear',''),
  LearnItem('🦆','بطة','Duck',''), LearnItem('🐑','خروف','Sheep',''), LearnItem('🐟','سمكة','Fish',''), LearnItem('🐢','سلحفاة','Turtle',''),
];

const colors = [
  LearnItem('🔴','أحمر','Red',''), LearnItem('🔵','أزرق','Blue',''), LearnItem('🟢','أخضر','Green',''), LearnItem('🟡','أصفر','Yellow',''),
  LearnItem('⚫','أسود','Black',''), LearnItem('⚪','أبيض','White',''), LearnItem('🟤','بني','Brown',''), LearnItem('🟣','بنفسجي','Purple',''),
  LearnItem('🟠','برتقالي','Orange',''), LearnItem('🌸','وردي','Pink',''),
];

const numberNamesAr = ['واحد','اثنان','ثلاثة','أربعة','خمسة','ستة','سبعة','ثمانية','تسعة','عشرة'];
const numberNamesEn = ['One','Two','Three','Four','Five','Six','Seven','Eight','Nine','Ten'];

class HomePage extends StatefulWidget { const HomePage({super.key}); @override State<HomePage> createState()=>_HomePageState(); }
class _HomePageState extends State<HomePage> { int stars=0; @override void initState(){super.initState(); _load();} Future<void> _load() async {stars=await ProgressStore.getStars(); if(mounted)setState((){});} @override Widget build(BuildContext context){return Scaffold(body: SafeArea(child: Padding(padding: const EdgeInsets.all(18), child: Column(children:[
  Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children:[const Text('طفلي الأول', style: TextStyle(fontSize:30,fontWeight:FontWeight.w900,color:Color(0xFF5B3A29))), Chip(label: Text('⭐ $stars', style: const TextStyle(fontSize:20,fontWeight:FontWeight.bold)))]),
  const SizedBox(height:16), const Text('تعلم الحروف والأرقام والألوان والحيوانات بطريقة مرحة', textAlign: TextAlign.center, style: TextStyle(fontSize:18,color:Color(0xFF6B5B53))),
  const SizedBox(height:20), Expanded(child: GridView.count(crossAxisCount:2, mainAxisSpacing:14, crossAxisSpacing:14, children:[
    homeCard(context,'🇸🇦','العربية', const ArabicPage(), const Color(0xFFFFD6A5)),
    homeCard(context,'🇬🇧','English', const EnglishPage(), const Color(0xFFCDE7FF)),
    homeCard(context,'🎮','الألعاب', const GamesPage(), const Color(0xFFD8F8D2)),
    homeCard(context,'🏆','إنجازاتي', const AchievementsPage(), const Color(0xFFFFF1A8)),
  ]))
]))));}}

Widget homeCard(BuildContext c,String icon,String title,Widget page,Color color)=> InkWell(borderRadius:BorderRadius.circular(28), onTap:()=>Navigator.push(c,MaterialPageRoute(builder:(_)=>page)), child: Container(decoration:BoxDecoration(color:color,borderRadius:BorderRadius.circular(28), boxShadow:[BoxShadow(color:Colors.black.withOpacity(.08), blurRadius:10, offset: const Offset(0,5))]), child: Column(mainAxisAlignment:MainAxisAlignment.center, children:[Text(icon,style:const TextStyle(fontSize:54)), const SizedBox(height:12), Text(title,style:const TextStyle(fontSize:24,fontWeight:FontWeight.w900,color:Color(0xFF473225)))])));

class ArabicPage extends StatelessWidget{const ArabicPage({super.key});@override Widget build(BuildContext c)=>MenuPage(title:'العربية', cards:[
  MenuEntry('🔤','الحروف العربية', LearnGrid(title:'الحروف العربية',items:arabicLetters,mode:'arLetters')),
  MenuEntry('🔢','الأرقام 1 - 10', const NumbersPage(arabic:true)),
  MenuEntry('🎨','الألوان', LearnGrid(title:'الألوان',items:colors,mode:'colorsAr')),
  MenuEntry('🐶','الحيوانات عربي + English', LearnGrid(title:'الحيوانات',items:animals,mode:'animals')),
]);}
class EnglishPage extends StatelessWidget{const EnglishPage({super.key});@override Widget build(BuildContext c)=>MenuPage(title:'English', cards:[
  MenuEntry('🔠','Capital Letters', LearnGrid(title:'Capital Letters',items:englishLetters,mode:'enLetters')),
  MenuEntry('🔢','Numbers 1 - 10', const NumbersPage(arabic:false)),
  MenuEntry('🎨','Colors', LearnGrid(title:'Colors',items:colors,mode:'colorsEn')),
  MenuEntry('🐶','Animals عربي + English', LearnGrid(title:'Animals',items:animals,mode:'animals')),
]);}
class MenuEntry{final String icon,title;final Widget page;const MenuEntry(this.icon,this.title,this.page);} 
class MenuPage extends StatelessWidget{final String title;final List<MenuEntry> cards; const MenuPage({super.key,required this.title,required this.cards});@override Widget build(BuildContext c)=>Scaffold(appBar: AppBar(title: Text(title),centerTitle:true),body:Padding(padding:const EdgeInsets.all(16),child:GridView.count(crossAxisCount:2,mainAxisSpacing:14,crossAxisSpacing:14,children:cards.map((e)=>homeCard(c,e.icon,e.title,e.page,const Color(0xFFFFE8CC))).toList())));}

class LearnGrid extends StatelessWidget{final String title,mode;final List<LearnItem> items; const LearnGrid({super.key,required this.title,required this.items,required this.mode}); Future<void> speak(LearnItem it) async{ if(mode=='animals'){await TtsService.say(it.ar); Future.delayed(const Duration(milliseconds:850),()=>TtsService.say(it.en,english:true));} else if(mode=='enLetters'||mode=='colorsEn'){await TtsService.say(mode=='enLetters'?'${it.ar}. ${it.en}':it.en, english:true);} else {await TtsService.say(mode=='arLetters'?'${it.ar}. ${it.en}':it.ar);} }
@override Widget build(BuildContext c)=>Scaffold(appBar:AppBar(title:Text(title),centerTitle:true),body:Padding(padding:const EdgeInsets.all(12),child:GridView.builder(itemCount:items.length,gridDelegate:const SliverGridDelegateWithFixedCrossAxisCount(crossAxisCount:2,mainAxisSpacing:12,crossAxisSpacing:12,childAspectRatio:.9),itemBuilder:(c,i){final it=items[i]; final isAnimal=mode=='animals'; final primary= mode=='enLetters'?it.ar: mode=='colorsEn'?it.en: it.ar; final sub= isAnimal?'${it.ar}\n${it.en}': mode=='arLetters'||mode=='enLetters'?it.en:''; return InkWell(borderRadius:BorderRadius.circular(24),onTap:()=>speak(it),child:Container(decoration:BoxDecoration(color:Colors.white,borderRadius:BorderRadius.circular(24),border:Border.all(color:const Color(0xFFFFC39B),width:2)),padding:const EdgeInsets.all(10),child:Column(mainAxisAlignment:MainAxisAlignment.center,children:[Text(it.icon,style:const TextStyle(fontSize:58)),const SizedBox(height:8),Text(primary,textAlign:TextAlign.center,style:const TextStyle(fontSize:34,fontWeight:FontWeight.w900,color:Color(0xFF463125))),if(sub.isNotEmpty)Text(sub,textAlign:TextAlign.center,style:const TextStyle(fontSize:20,fontWeight:FontWeight.w700,color:Color(0xFF6B5B53)))])));}))));}

class NumbersPage extends StatelessWidget{final bool arabic; const NumbersPage({super.key,required this.arabic}); @override Widget build(BuildContext c)=>Scaffold(appBar:AppBar(title:Text(arabic?'الأرقام':'Numbers'),centerTitle:true),body:Padding(padding:const EdgeInsets.all(12),child:GridView.builder(itemCount:10,gridDelegate:const SliverGridDelegateWithFixedCrossAxisCount(crossAxisCount:2,mainAxisSpacing:12,crossAxisSpacing:12),itemBuilder:(c,i){final n=i+1; final name=arabic?numberNamesAr[i]:numberNamesEn[i]; return InkWell(borderRadius:BorderRadius.circular(24),onTap:()=>TtsService.say(name,english:!arabic),child:Container(decoration:BoxDecoration(color:Colors.white,borderRadius:BorderRadius.circular(24),border:Border.all(color:const Color(0xFFBCE3FF),width:2)),child:Column(mainAxisAlignment:MainAxisAlignment.center,children:[Text('$n',style:const TextStyle(fontSize:58,fontWeight:FontWeight.w900,color:Color(0xFF2C5D75))),Text(name,style:const TextStyle(fontSize:26,fontWeight:FontWeight.w800)),Text(List.filled(n,'🍎').join(' '),textAlign:TextAlign.center,style:const TextStyle(fontSize:18))])));}))));}

class GamesPage extends StatefulWidget{const GamesPage({super.key});@override State<GamesPage> createState()=>_GamesPageState();}
class _GamesPageState extends State<GamesPage>{ final rnd=Random(); late LearnItem answer; late List<LearnItem> options; String msg='اختر الحيوان الصحيح'; int stars=0; @override void initState(){super.initState(); next(); ProgressStore.getStars().then((v)=>setState(()=>stars=v));} void next(){ final list=[...animals]..shuffle(); answer=list.first; options=list.take(4).toList()..shuffle(); }
Future<void> pick(LearnItem it) async{ if(it.ar==answer.ar){ final s=await ProgressStore.addStar(); await TtsService.say('أحسنت'); setState((){stars=s; msg='🎉 أحسنت! اختر التالي'; next();}); } else { await TtsService.say('حاول مرة أخرى'); setState(()=>msg='🙂 حاول مرة أخرى'); }}
@override Widget build(BuildContext c)=>Scaffold(appBar:AppBar(title:Text('الألعاب  ⭐ $stars'),centerTitle:true),body:Padding(padding:const EdgeInsets.all(16),child:Column(children:[Text(msg,style:const TextStyle(fontSize:24,fontWeight:FontWeight.w900)),const SizedBox(height:12),Text('أين ${answer.ar} ؟',style:const TextStyle(fontSize:30,fontWeight:FontWeight.w900,color:Color(0xFF5B3A29))),const SizedBox(height:20),Expanded(child:GridView.count(crossAxisCount:2,mainAxisSpacing:14,crossAxisSpacing:14,children:options.map((it)=>InkWell(borderRadius:BorderRadius.circular(26),onTap:()=>pick(it),child:Container(decoration:BoxDecoration(color:Colors.white,borderRadius:BorderRadius.circular(26),border:Border.all(color:const Color(0xFFD8F8D2),width:3)),child:Column(mainAxisAlignment:MainAxisAlignment.center,children:[Text(it.icon,style:const TextStyle(fontSize:64)),Text(it.ar,style:const TextStyle(fontSize:24,fontWeight:FontWeight.w900)),Text(it.en,style:const TextStyle(fontSize:18,fontWeight:FontWeight.w700))]))).toList()))])));}

class AchievementsPage extends StatefulWidget{const AchievementsPage({super.key});@override State<AchievementsPage> createState()=>_AchievementsPageState();}
class _AchievementsPageState extends State<AchievementsPage>{int stars=0;@override void initState(){super.initState();ProgressStore.getStars().then((v)=>setState(()=>stars=v));}@override Widget build(BuildContext c){String cup=stars>=50?'🥇 كأس ذهبي':stars>=25?'🥈 كأس فضي':stars>=10?'🥉 كأس برونزي':'⭐ اجمع 10 نجوم للكأس الأول';return Scaffold(appBar:AppBar(title:const Text('إنجازاتي'),centerTitle:true),body:Center(child:Padding(padding:const EdgeInsets.all(24),child:Column(mainAxisAlignment:MainAxisAlignment.center,children:[const Text('🏆',style:TextStyle(fontSize:90)),Text('نجومك: $stars',style:const TextStyle(fontSize:34,fontWeight:FontWeight.w900)),const SizedBox(height:12),Text(cup,textAlign:TextAlign.center,style:const TextStyle(fontSize:28,fontWeight:FontWeight.w800,color:Color(0xFF5B3A29))),const SizedBox(height:30),FilledButton.icon(onPressed:()async{await ProgressStore.reset();setState(()=>stars=0);},icon:const Icon(Icons.refresh),label:const Text('تصفير النجوم'))]))));}}
