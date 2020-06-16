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
    } catch (e) {
      return e;
    }
  }

  // sign out
  void signOut() {
    _firebaseAuth.signOut();
  }

  // sign up
  Future signUp(String email, String password) async {
    try {
      AuthResult result = await _firebaseAuth.createUserWithEmailAndPassword(
          email: email, password: password);
      result.user.uid;
    } catch (e) {
      print('signup error $e');
    }
  }

  // get user onAuthChange
  Stream<FirebaseUser> get user {
    return _firebaseAuth.onAuthStateChanged;
  }
}
