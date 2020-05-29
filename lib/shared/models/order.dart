class Order {
  String to;
  String from;
  String id;
  int date;
  bool forPayment;
  List items;
  String status;
  double total;

  Order({
    this.id,
    this.date,
    this.forPayment,
    this.items,
    this.status,
    this.total,
    this.to,
    this.from,
  });
}
