

class Item{
  String name;
  String package;
  int quantity;
  double price;

  Item({this.name, this.package, this.quantity, this.price});

  Map toJson() {
    return {
      'name' : this.name,
      'package' : this.package,
      'quantity' : this.quantity,
      'price' : this.price
    };
  }
}