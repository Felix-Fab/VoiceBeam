import 'dart:async';
import 'dart:convert';
import 'dart:developer';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:localstorage/localstorage.dart';
import 'package:voicebeam/Pages/UserMenu.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  static const String _title = 'Tester App';

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: _title,
      home: const Login(),
      theme: ThemeData(
        primarySwatch: Colors.orange,
      ),
    );
  }
}

class Login extends StatelessWidget {
  const Login({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    LocalStorage localStorage = LocalStorage('voicebeam');

    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;

    final GlobalKey<FormState> formKey = GlobalKey<FormState>();

    TextEditingController emailController = TextEditingController();
    TextEditingController passwordController = TextEditingController();

    if(localStorage.getItem("accessToken") != null){
      //Navigate to UserMenu

    }

    return Scaffold(
      backgroundColor: Colors.orange,
      body: Form(
        key: formKey,
        child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Container(
                    margin: const EdgeInsets.only(bottom: 100),
                    child: const Text(
                      "VoiceBeam",
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 50,
                        fontStyle: FontStyle.italic,
                        fontWeight: FontWeight.bold
                      ),
                    ),
                  ),
                  Container(
                    margin: const EdgeInsets.only(bottom: 12),
                    width: width * 0.85,
                    child: TextFormField(
                      controller: emailController,
                      decoration: const InputDecoration(
                          hintText: 'Email',
                          border: OutlineInputBorder(),
                          fillColor: Colors.white,
                          filled: true
                      ),
                      style: const TextStyle(
                          fontStyle: FontStyle.italic,
                          fontWeight: FontWeight.bold,
                        fontSize: 15
                      ),
                      validator: (String? value) {
                        if (value == null || value.isEmpty){
                          return 'Please enter some text';
                        }
                        return null;
                      },
                    ),
                  ),
                  Container(
                    width: width * 0.85,
                    margin: const EdgeInsets.only(bottom: 5),
                    child: TextFormField(
                      controller: passwordController,
                      decoration: const InputDecoration(
                          hintText: 'Password',
                          border: OutlineInputBorder(),
                          fillColor: Colors.white,
                          filled: true
                      ),
                      style: const TextStyle(
                          fontStyle: FontStyle.italic,
                          fontWeight: FontWeight.bold,
                          fontSize: 15
                      ),
                      validator: (String? value) {
                        if (value == null || value.isEmpty){
                          return 'Please enter some text';
                        }
                        return null;
                      },
                    ),
                  ),
                  SizedBox(
                    width: width * 0.85,
                    child: GestureDetector(
                      child: const Text(
                        "Passwort vergessen",
                        style: TextStyle(
                          color: Colors.white
                        ),
                      ),
                    ),
                  ),
                  Container(
                    margin: const EdgeInsets.only(top: 55, bottom: 5),
                    width: width * 0.85,
                    height: 50,
                    child: ElevatedButton(
                      style: ButtonStyle(
                        backgroundColor: MaterialStatePropertyAll(HexColor("#d2691e")),
                      ),
                      onPressed: () async => {
                        if(formKey.currentState!.validate()){
                          if(await checkLoginData(emailController.text.toString(), passwordController.text.toString(), context, localStorage)){
                            Navigator.push(context, MaterialPageRoute(builder: (context) => const UserMenu()))
                          }
                        }
                      },
                      child: const Text(
                        'Login',
                        style: TextStyle(
                          color: Colors.white,
                          fontSize: 30
                        ),
                      ),
                    ),
                  ),
                  SizedBox(
                    width: width * 0.85,
                    child: GestureDetector(
                      child: const Text(
                        "Create a new account",
                        style: TextStyle(
                          color: Colors.white
                        ),
                      ),
                    ),
                  )
                ],
              )
            ]
        ),
      )
    );
  }
}

class HexColor extends Color {
  static int _getColor(String hex) {
    String formattedHex =  "FF${hex.toUpperCase().replaceAll("#", "")}";
    return int.parse(formattedHex, radix: 16);
  }
  HexColor(final String hex) : super(_getColor(hex));
}

Future<bool> checkLoginData(String email, String password, BuildContext context, LocalStorage localStorage) async {

  Map data = {
    'email': email,
    'password': password
  };

  var body = json.encode(data);

  try{
    var response = await http.post(Uri.parse("http://localhost:3000/auth/login"), headers: {"Content-Type": "application/json"}, body: body);

    String responseMessage;

    switch(response.statusCode){
      case 400:
        responseMessage = "Invalid Credentials Form";
        break;

      case 401:
        responseMessage = "Invalid Credentials";
        break;

      case 200:
        final body = json.decode(response.body);
        localStorage.setItem("accessToken", body["accessToken"]);
        return true;

      default:
        responseMessage = "unknown error";
        break;
    }

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(responseMessage))
    );
  }on SocketException {
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text('No Internet connection'))
    );
  }on TimeoutException{
    ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Connection timeout'))
    );
  }
  on Error catch (e){
    ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Invalid Credentials'))
    );
  }

  return true;
}

