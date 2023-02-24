import 'dart:convert';
import 'dart:developer';
import 'dart:io';
import 'package:http/http.dart' as http;

import 'package:flutter/material.dart';
import 'package:localstorage/localstorage.dart';

class UserMenu extends StatefulWidget {
  const UserMenu({Key? key}) : super(key: key);

  @override
  State<UserMenu> createState() => _UserMenuState();
}

class _UserMenuState extends State<UserMenu> {

  LocalStorage localStorage = LocalStorage('voicebeam');
  List UserListItems = <User>[];

  @override
  void initState(){
    super.initState();
    getMenuUsers(localStorage.getItem("accessToken")).then((value) => {
      if(value != null){
        for(int i = 0;i < value.data.length; i++){
          UserListItems.add(value.data[i])
        }
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          SizedBox(
            height: 300,
            child: ListView.builder(
                itemBuilder: (BuildContext context, int index) {
                  return GestureDetector(
                    child: Container(
                      margin: const EdgeInsets.all(5),
                      height: 50,
                      decoration: BoxDecoration(
                          color: Colors.amber[600],
                          border: Border.all(color: Colors.black),
                          borderRadius: BorderRadius.circular(16)),
                      child: const Center(child: Text("Hallo")),
                    ),
                  );
                }
            ),
          )
        ],
      ),
    );
  }
}

Future<Users?> getMenuUsers(String accessToken) async{
  try {
    var response = await http.post(Uri.parse("http://localhost:3000/auth/getUsers"), headers: {"Content-Type": "application/json", HttpHeaders.authorizationHeader: 'Bearer $accessToken'});

    if (response.statusCode == 200) {
      return Users.fromJson(jsonDecode(response.body)["users"]);
    }else{
      throw Exception('Failed to load Users');
    }
  }on Error catch (e){
    print(e.stackTrace);
    return null;
  }
}

class Users {
  final List<User> data;

  Users({required this.data});

  factory Users.fromJson(Map<String, dynamic> parsedJson){

    var list = parsedJson['data'] as List;
    print(list.runtimeType);
    List<User> dataList = list.map((i) => User.fromJson(i)).toList();

    return Users(
        data: dataList
    );
  }
}

class User {
  final int id;
  final String username;

  const User({
    required this.id,
    required this.username,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      id: json['_id'],
      username: json['username'],
    );
  }
}