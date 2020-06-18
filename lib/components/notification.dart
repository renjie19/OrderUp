import 'package:flutter_local_notifications/flutter_local_notifications.dart';

class OrderNotification {

  static show(bool newOrder) async{
    FlutterLocalNotificationsPlugin notificationsPlugin = FlutterLocalNotificationsPlugin();
    var initSettings = InitializationSettings(AndroidInitializationSettings('@mipmap/ic_launcher'), null);
    notificationsPlugin.initialize(initSettings);

    var android = AndroidNotificationDetails('id', 'name', 'description');
    var notificationDetails = NotificationDetails(android, null);
    await notificationsPlugin.show(0, newOrder ? 'New Order Arrived' : 'Order Update', 'You have a new order update', notificationDetails);
  }
}