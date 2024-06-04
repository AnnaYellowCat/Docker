db = db.getSiblingDB('admin');
db.auth("root", "example");
db = db.getSiblingDB('shop');

db.createCollection("clients");
db.createCollection("products");
db.createCollection("sales");

db.clients.insertOne({"username": "Alice", "phone": "+79643576435", "password": "000", "role": "USER"});
db.clients.insertOne({"username": "Bob", "phone": "+79465768495", "password": "456", "role": "USER"});
db.clients.insertOne({"username": "Anna", "phone": "+79498368973", "password": "123", "role": "MANAGER"});
db.clients.insertOne({"username": "Tom", "phone": "+78396254786", "password": "123456", "role": "DIRECTOR"});
db.clients.insertOne({"username": "Anonymous", "phone": "+79374658374", "password": "666", "role": "USER"});
db.clients.insertOne({"username": "Alex", "phone": "+78273495872", "password": "321", "role": "MANAGER"});

db.products.insertOne({"title": "Мягкий домик для кошек", "description": "Ferplast Домик-трансформер с двухсторонней подушкой для кошек и собак...", "price": 5600, "quantity": 20});
db.products.insertOne({"title": "Аквариум для рыб", "description": "Аква Гесса Аква Гесса Нано аквариум 20л серый", "price": 3400, "quantity": 15});
db.products.insertOne({"title": "Сухой корм для собак", "description": "Farmina Vet Life UltraHypo диетический сухой корм для собак, гипоаллер...", "price": 4500, "quantity": 45});
db.products.insertOne({"title": "Гамак для грызунов", "description": "Petmax Гамак для грызунов 2-этажный", "price": 600, "quantity": 25});
db.products.insertOne({"title": "Качели для птиц", "description": "Triol Игрушка для птиц Качели с шариками, 230/350*180мм", "price": 500, "quantity": 25});
db.products.insertOne({"title": "Успокоительное средство для кошек", "description": "Астрафарм Экспресс Успокоин Таблетки от стресса для кошек, 6 таблеток", "price": 600, "quantity": 15});
db.products.insertOne({"title": "Автокормушка для кошек", "description": "Hiper Умная Wi-Fi кормушка для кошек и собак IoT Pet Feeder, 3 л.", "price": 8200, "quantity": 10});

db.sales.insertOne({"clientName": "Alice", "date": "2024-04-12", "productTitlesByQuantity": {"Мягкий домик для кошек": 2, "Сухой корм для собак": 3}});
db.sales.insertOne({"clientName": "Bob", "date": "2020-07-20", "productTitlesByQuantity": {"Сухой корм для собак": 1}});
db.sales.insertOne({"clientName": "Anonymous", "date": "2019-07-06", "productTitlesByQuantity": {"Аквариум для рыб": 2}});
db.sales.insertOne({"clientName": "Alice", "date": "2020-09-14", "productTitlesByQuantity": {"Автокормушка для кошек": 1, "Успокоительное средство для кошек": 2, "Качели для птиц": 3}});
db.sales.insertOne({"clientName": "Anonymous", "date": "2022-01-01", "productTitlesByQuantity": {"Гамак для грызунов": 5}});

db.clients.createIndex({ "username": 1 });
db.clients.createIndex({ "role": 1 });

db.products.createIndex({ "price": 1 });
db.products.createIndex({ "quantity": 1 });
db.products.createIndex({ "price": 1, "quantity": -1 });

db.sales.createIndex({ "date": 1 });

db = db.getSiblingDB('admin');

db.createRole({
    role: "readerOfClients",
    privileges: [
        { resource: { db: "shop", collection: "clients" }, actions: ["find"] }
    ],
    roles: []
});
db.createUser({
    user: "login",
    pwd: "login",
    roles: ["readerOfClients"]
});

db.createRole({
    role: "readerOfAll",
    privileges: [
        {
            resource: { db: "shop", collection: "" },
            actions: ["find"]
        }
    ],
    roles: []
})
db.createUser({
    user: "client",
    pwd: "client",
    roles: ["readerOfAll"]
});

db.createRole({
    role: "updaterOfAll",
    privileges: [
        { resource: { db: "shop", collection: "" }, actions: ["find", "update"] }
    ],
    roles: []
});
db.createUser({
    user: "manager",
    pwd: "manager",
    roles: ["updaterOfAll"]
});

db.createRole({
    role: "doerOfAll",
    privileges: [
        {
            resource: { db: "shop", collection: "" },
            actions: ["find", "update", "insert", "remove"]
        }
    ],
    roles: []
})
db.createUser({
    user: "director",
    pwd: "director",
    roles: ["doerOfAll"]
});

 exit;
