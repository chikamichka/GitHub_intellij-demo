import 'package:flutter/material.dart';
import 'screens/login_screen.dart';
import 'screens/navigation_bar.dart'; // Ensure this is the correct path to the navigation_bar.dart file

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Conference App',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      initialRoute: '/',
      routes: {
        '/': (context) => LoginScreen(),
        '/nav': (context) => CustomNavigationBar(),
      },
    );
  }
}
