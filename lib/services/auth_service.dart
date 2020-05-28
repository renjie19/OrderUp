import 'package:firebase_auth/firebase_auth.dart';

class AuthService {
  final FirebaseAuth _firebaseAuth = FirebaseAuth.instance;
  static String userId;

  // sign in
  Future signIn(String email, String password) async {
    try {
      AuthResult result = await _firebaseAuth.signInWithEmailAndPassword(
          email: email, password: password);
      userId = result.user.uid;
      return userId;
    } catch (e) {
      print(e);
    }
  }

  // sign out

  // sign up
  Future signUp(String email, String password) async {
    try {
      AuthResult result = await _firebaseAuth.createUserWithEmailAndPassword(
          email: email, password: password);
      return result.user.uid;
    } catch (e) {
      print('signup error $e');
    }
  }
}
