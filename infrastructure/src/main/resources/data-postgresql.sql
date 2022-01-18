insert into samplemonolith.products (productid, name, price) values ('2021-PRODUCT-PEACH-01', '천중도', 20000);
insert into samplemonolith.products (productid, name, price) values ('2021-PRODUCT-PEACH-02', '황도', 20000);
insert into samplemonolith.products (productid, name, price) values ('2021-PRODUCT-PEACH-03', '당금도', 20000);

insert into samplemonolith.inventories (inventoryid, "count", "date", productid)
values (1, 10, to_date('2022/08/11', 'yyyy/mm/dd'), '2021-PRODUCT-PEACH-01');
insert into samplemonolith.inventories (inventoryid, "count", "date", productid)
values (2, 10, to_date('2022/08/12', 'yyyy/mm/dd'), '2021-PRODUCT-PEACH-01');
insert into samplemonolith.inventories (inventoryid, "count", "date", productid)
values (3, 13, to_date('2022/08/12', 'yyyy/mm/dd'), '2021-PRODUCT-PEACH-01');
insert into samplemonolith.inventories (inventoryid, "count", "date", productid)
values (4, 14, to_date('2022/08/12', 'yyyy/mm/dd'), '2021-PRODUCT-PEACH-01');
insert into samplemonolith.inventories (inventoryid, "count", "date", productid)
values (5, 17, to_date('2022/08/12', 'yyyy/mm/dd'), '2021-PRODUCT-PEACH-01');
insert into samplemonolith.inventories (inventoryid, "count", "date", productid)
values (6,20, to_date('2022/08/12', 'yyyy/mm/dd'), '2021-PRODUCT-PEACH-01');
insert into samplemonolith.inventories (inventoryid, "count", "date", productid)
values (7,10, to_date('2022/08/20', 'yyyy/mm/dd'), '2021-PRODUCT-PEACH-02');
insert into samplemonolith.inventories (inventoryid, "count", "date", productid)
values (8,10, to_date('2022/08/21', 'yyyy/mm/dd'), '2021-PRODUCT-PEACH-02');
insert into samplemonolith.inventories (inventoryid, "count", "date", productid)
values (9,14, to_date('2022/08/20', 'yyyy/mm/dd'), '2021-PRODUCT-PEACH-03');
insert into samplemonolith.inventories (inventoryid, "count", "date", productid)
values (10,20, to_date('2022/08/21', 'yyyy/mm/dd'), '2021-PRODUCT-PEACH-03');

insert into samplemonolith.members (memberid, membername)
values ('jieun','아이유');
insert into samplemonolith.members (memberid, membername)
values ('boyoung','박보영');
insert into samplemonolith.members (memberid, membername)
values ('tony','이희종');