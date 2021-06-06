-- TODO replace with variables once you figure out how to declare them

-- reader 1 - b39f0709-574b-4e62-82b4-6dd98eca93a6
-- admin 1 - 55a08e2e-3a28-42a2-89f6-01e798723968
-- category 1 - 22e4c2d7-cbdc-4513-98cd-917d24c3d32d
-- authors
--       68d0f630-808c-42e3-a97c-75150203ff6c
--       bbf41804-ad07-4cf9-ad6c-6cc9f02c956c
--       17462d5b-59f2-42b2-99f3-9bba1c1631c5

-- books
--       ffa1fd63-9b73-4fa9-8cf1-7492b4c0b874
--       099f22b5-d839-4b60-be60-c4c299726f15
--       1e3ddcaf-c0f0-442d-8087-b6f3bee418a8
--       0c2a1eb0-3d92-4dee-8fa7-e06d49c63b2c
--       e8b7719a-2bdd-4365-96a4-079a75e53898
--       3f207bf0-5256-4599-ab1d-7e050f6bd0c5
--       91005749-dbd4-4e12-95fa-308bfd956e10
--       af4ffa04-2967-4ca7-95a7-2de5aced405a
--       7761d291-efed-4e81-bb1a-cb9f02a8770f
--       9f7abb57-8153-4945-9e68-64f0d0827468
--       2a52dbf7-560f-4fc6-b85e-324e08a77da8
--       6147c0ff-fe26-4303-b417-46e1eb9a998d
--       1668b709-06bb-4aed-a5a1-253344879d9e
--       a57c82bc-24f5-4358-b303-6c789609b48b
--       48459a83-99de-4899-893d-f73a0b48488f
--       7d093d94-44fd-4514-8e7f-7e00810d40be
--       dd31fdc0-e882-4e15-8deb-73d2984e8315
--       3ec4e897-bc9d-441b-9cea-215a1a0e7018
--       f9b1891c-c507-4027-9946-991c54b17bd2
--       28d17353-7c03-4833-b46d-3c11e118294a
--       7401a322-5d43-4abf-90dd-126515c08057
--       054865b6-262c-4ab7-9dad-72b0c43c1be8

-- reading progress
--       c3dbaf91-1aa0-4f7e-811f-d5d6146fa03a
--       f5f61a38-cdb8-4271-954c-460bd0f56349
--       aa3a181e-2e55-4dc8-861c-ad3a86401d38
--       d9bc4821-beb8-4a25-86ed-8cf6032b6cb7
--       380e08e1-7ae8-42fb-8599-9ffa4a9967e2

-- reviews
--       1c8db6c5-6514-42a0-9ee1-00a1f95e0f09
--       38afff96-c146-4974-b8d1-2b6764b07af9
--       ceed9dc0-a134-4f78-ba47-6f8c3c6bfad4
--       012115df-8349-4289-918d-450051ed3f2e

-- reading list
--       75f5a8b2-ca9f-4ef7-ae75-ca496434eccb
--       1dcee1c9-95d9-42dc-ac4a-c0497392e028
--       85cb3933-7110-4db4-a34e-b23c04312329
--       76175e0c-8dca-4d74-b279-0c469ff50080
--       dbaa1985-9313-4eb9-8440-61f04fa49e25
/*------------------------ROLE-----------------------------------------*/
INSERT INTO "authority" (name) VALUES ('ROLE_READER');
INSERT INTO "authority" (name) VALUES ('ROLE_ADMIN');

/*------------------------reader-----------------------------------------*/

insert into "user" values ('b39f0709-574b-4e62-82b4-6dd98eca93a6',	'harry.gegic@gmail.com',	true,	'Haris',	'Gegic',	null,	'$2a$10$TfA5Evj/8pQr0GIrFmWeRe.w0jWZD.1zoFl5MkpyzPZmvbPYWJds.');
insert into reader values (20,	true,	'b39f0709-574b-4e62-82b4-6dd98eca93a6');
insert into user_authority values ('b39f0709-574b-4e62-82b4-6dd98eca93a6',	1);

/*------------------------admin-----------------------------------------*/

insert into "user" values ('55a08e2e-3a28-42a2-89f6-01e798723968',	'admin@mail.com',	true,	'Admin',	'Adminic',	null,	'$2a$10$TfA5Evj/8pQr0GIrFmWeRe.w0jWZD.1zoFl5MkpyzPZmvbPYWJds.');
insert into admin values ('55a08e2e-3a28-42a2-89f6-01e798723968');
insert into user_authority values ('55a08e2e-3a28-42a2-89f6-01e798723968',	2);

/*------------------------category-----------------------------------------*/

insert into category values ('22e4c2d7-cbdc-4513-98cd-917d24c3d32d',	true,	'Neka kategorija',	'Kategorija');

/*------------------------plan-----------------------------------------*/

insert into plan values ('a8f0fd4e-982b-42db-bf72-f94ee26c2d5d', 3, 'Our plan for beginners', 'Starter', 2.99);
insert into plan_categories values ('a8f0fd4e-982b-42db-bf72-f94ee26c2d5d', '22e4c2d7-cbdc-4513-98cd-917d24c3d32d');


insert into plan values ('981b8f72-60b9-4c31-a880-1abab63c4d15', 10, 'Our second plan', 'Silver', 4.99);
insert into plan_categories values ('981b8f72-60b9-4c31-a880-1abab63c4d15', '22e4c2d7-cbdc-4513-98cd-917d24c3d32d');

insert into plan values ('9cb3e2d9-9799-4777-9d7c-26ab5b5c51be', 30, 'Our recommended plan', 'Gold', 9.99);
insert into plan_categories values ('9cb3e2d9-9799-4777-9d7c-26ab5b5c51be', '22e4c2d7-cbdc-4513-98cd-917d24c3d32d');

/*-----------------------author--------------------------------------------*/
INSERT INTO author VALUES('68d0f630-808c-42e3-a97c-75150203ff6c',	0,	'1900-08-07 00:00:00',	'1969-08-07 00:00:00',	'Some description',	null,	'Agata Kristi');
INSERT INTO author VALUES('bbf41804-ad07-4cf9-ad6c-6cc9f02c956c',	0,	'1900-08-07 00:00:00',	'1969-08-07 00:00:00',	'Some description',	null,	'Donato Karizi');
INSERT INTO author VALUES('17462d5b-59f2-42b2-99f3-9bba1c1631c5',	0,	'1900-08-07 00:00:00',	'1969-08-07 00:00:00',	'Some description',	null,	'Fjodor Mihailovič Dostojevski');

/*------------------------book-----------------------------------------*/

insert into book values ('ffa1fd63-9b73-4fa9-8cf1-7492b4c0b874', 	0,	null,	'Some description',	'Ubistvo u Mesopotamiji',   	    0,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('099f22b5-d839-4b60-be60-c4c299726f15',	5,	null,	'Some description',	'Rani slučajevi Herkula Poaroa',	1,  '1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('1e3ddcaf-c0f0-442d-8087-b6f3bee418a8', 	0,	null,	'Some description',	'Kukavičje jaje',	                0,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('0c2a1eb0-3d92-4dee-8fa7-e06d49c63b2c', 	1,	null,	'Some description',	'Slonovi pamte',	                1,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('e8b7719a-2bdd-4365-96a4-079a75e53898', 	4,	null,	'Some description',	'Ubistvo Rodžera Akrojda',	        1,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('3f207bf0-5256-4599-ab1d-7e050f6bd0c5',	0,	null,	'Some description',	'Smrt na Nilu',                 	0,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('91005749-dbd4-4e12-95fa-308bfd956e10',	0,	null,	'Some description',	'Ubistvo u Orijent Ekspresu',	    0,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('af4ffa04-2967-4ca7-95a7-2de5aced405a',	0,	null,	'Some description',	'Lovac na duše',	                0,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('7761d291-efed-4e81-bb1a-cb9f02a8770f',	0,	null,	'Some description',	'Dečak od stakla',	                0,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('9f7abb57-8153-4945-9e68-64f0d0827468',	0,	null,	'Some description',	'Vladar iz senke',	                0,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('2a52dbf7-560f-4fc6-b85e-324e08a77da8',	0,	null,	'Some description',	'Šaptač',	                        0,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('6147c0ff-fe26-4303-b417-46e1eb9a998d',	0,	null,	'Some description',	'Noć mi te uzima',              	0,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('1668b709-06bb-4aed-a5a1-253344879d9e',	0,	null,	'Some description',	'Gospodar senki',	                0,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('a57c82bc-24f5-4358-b303-6c789609b48b',	0,	null,	'Some description',	'Braća karamazovi I',	            0,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('48459a83-99de-4899-893d-f73a0b48488f',	0,	null,	'Some description',	'Braća karamazovi II',	            0,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('7d093d94-44fd-4514-8e7f-7e00810d40be',	0,	null,	'Some description',	'Bele noći',	                    0,	'1',	112,    1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('dd31fdc0-e882-4e15-8deb-73d2984e8315',	0,	null,	'Some description',	'Zapisi iz podezemlja',	            0,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('3ec4e897-bc9d-441b-9cea-215a1a0e7018',	5,	null,	'Some description',	'Idiot - I tom',	                1,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('f9b1891c-c507-4027-9946-991c54b17bd2',	0,	null,	'Some description',	'Idiot - II tom',	                0,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('28d17353-7c03-4833-b46d-3c11e118294a',	0,	null,	'Some description',	'Zločin i kazna',	                0,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('7401a322-5d43-4abf-90dd-126515c08057',	0,	null,	'Some description',	'Bedni ljudi',	                    0,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');
insert into book values ('054865b6-262c-4ab7-9dad-72b0c43c1be8',	0,	null,	'Some description',	'Kockar',                       	0,	'1',	112,	1,	'22e4c2d7-cbdc-4513-98cd-917d24c3d32d');

/*-------------------------book_authors-------------------------*/
INSERT INTO book_authors VALUES('ffa1fd63-9b73-4fa9-8cf1-7492b4c0b874',	'68d0f630-808c-42e3-a97c-75150203ff6c');
INSERT INTO book_authors VALUES('099f22b5-d839-4b60-be60-c4c299726f15',	'68d0f630-808c-42e3-a97c-75150203ff6c');
INSERT INTO book_authors VALUES('1e3ddcaf-c0f0-442d-8087-b6f3bee418a8',	'68d0f630-808c-42e3-a97c-75150203ff6c');
INSERT INTO book_authors VALUES('0c2a1eb0-3d92-4dee-8fa7-e06d49c63b2c',	'68d0f630-808c-42e3-a97c-75150203ff6c');
INSERT INTO book_authors VALUES('e8b7719a-2bdd-4365-96a4-079a75e53898',	'68d0f630-808c-42e3-a97c-75150203ff6c');
INSERT INTO book_authors VALUES('3f207bf0-5256-4599-ab1d-7e050f6bd0c5',	'68d0f630-808c-42e3-a97c-75150203ff6c');
INSERT INTO book_authors VALUES('91005749-dbd4-4e12-95fa-308bfd956e10',	'68d0f630-808c-42e3-a97c-75150203ff6c');
INSERT INTO book_authors VALUES('af4ffa04-2967-4ca7-95a7-2de5aced405a',	'bbf41804-ad07-4cf9-ad6c-6cc9f02c956c');
INSERT INTO book_authors VALUES('7761d291-efed-4e81-bb1a-cb9f02a8770f',	'bbf41804-ad07-4cf9-ad6c-6cc9f02c956c');
INSERT INTO book_authors VALUES('9f7abb57-8153-4945-9e68-64f0d0827468',	'bbf41804-ad07-4cf9-ad6c-6cc9f02c956c');
INSERT INTO book_authors VALUES('2a52dbf7-560f-4fc6-b85e-324e08a77da8',	'bbf41804-ad07-4cf9-ad6c-6cc9f02c956c');
INSERT INTO book_authors VALUES('6147c0ff-fe26-4303-b417-46e1eb9a998d',	'bbf41804-ad07-4cf9-ad6c-6cc9f02c956c');
INSERT INTO book_authors VALUES('1668b709-06bb-4aed-a5a1-253344879d9e',	'bbf41804-ad07-4cf9-ad6c-6cc9f02c956c');
INSERT INTO book_authors VALUES('a57c82bc-24f5-4358-b303-6c789609b48b',	'17462d5b-59f2-42b2-99f3-9bba1c1631c5');
INSERT INTO book_authors VALUES('48459a83-99de-4899-893d-f73a0b48488f',	'17462d5b-59f2-42b2-99f3-9bba1c1631c5');
INSERT INTO book_authors VALUES('7d093d94-44fd-4514-8e7f-7e00810d40be',	'17462d5b-59f2-42b2-99f3-9bba1c1631c5');
INSERT INTO book_authors VALUES('dd31fdc0-e882-4e15-8deb-73d2984e8315',	'17462d5b-59f2-42b2-99f3-9bba1c1631c5');
INSERT INTO book_authors VALUES('3ec4e897-bc9d-441b-9cea-215a1a0e7018',	'17462d5b-59f2-42b2-99f3-9bba1c1631c5');
INSERT INTO book_authors VALUES('f9b1891c-c507-4027-9946-991c54b17bd2',	'17462d5b-59f2-42b2-99f3-9bba1c1631c5');
INSERT INTO book_authors VALUES('28d17353-7c03-4833-b46d-3c11e118294a',	'17462d5b-59f2-42b2-99f3-9bba1c1631c5');
INSERT INTO book_authors VALUES('7401a322-5d43-4abf-90dd-126515c08057',	'17462d5b-59f2-42b2-99f3-9bba1c1631c5');
INSERT INTO book_authors VALUES('054865b6-262c-4ab7-9dad-72b0c43c1be8',	'17462d5b-59f2-42b2-99f3-9bba1c1631c5');

/*----------------reading_progress------------------------------------*/
INSERT INTO reading_progress VALUES('c3dbaf91-1aa0-4f7e-811f-d5d6146fa03a',	'2021-05-23 12:20:00',	14, 	'ffa1fd63-9b73-4fa9-8cf1-7492b4c0b874',	'b39f0709-574b-4e62-82b4-6dd98eca93a6');
INSERT INTO reading_progress VALUES('f5f61a38-cdb8-4271-954c-460bd0f56349',	'2021-05-23 12:20:00',	104, 	'099f22b5-d839-4b60-be60-c4c299726f15',	'b39f0709-574b-4e62-82b4-6dd98eca93a6');
INSERT INTO reading_progress VALUES('aa3a181e-2e55-4dc8-861c-ad3a86401d38',	'2021-05-23 12:20:00',	110,	'0c2a1eb0-3d92-4dee-8fa7-e06d49c63b2c',	'b39f0709-574b-4e62-82b4-6dd98eca93a6');
INSERT INTO reading_progress VALUES('d9bc4821-beb8-4a25-86ed-8cf6032b6cb7',	'2021-05-23 12:20:00',	112,	'3ec4e897-bc9d-441b-9cea-215a1a0e7018',	'b39f0709-574b-4e62-82b4-6dd98eca93a6');
INSERT INTO reading_progress VALUES('380e08e1-7ae8-42fb-8599-9ffa4a9967e2',	'2021-05-23 12:20:00',	101, 	'e8b7719a-2bdd-4365-96a4-079a75e53898',	'b39f0709-574b-4e62-82b4-6dd98eca93a6');

/*-------------review---------------------------------*/
INSERT INTO review VALUES('1c8db6c5-6514-42a0-9ee1-00a1f95e0f09',	'Mislim da je knjiga zaista sjajna. Sve preporuke!',	5,	'2021-05-23 12:20:00',	'099f22b5-d839-4b60-be60-c4c299726f15',	'b39f0709-574b-4e62-82b4-6dd98eca93a6');
INSERT INTO review VALUES('38afff96-c146-4974-b8d1-2b6764b07af9',	'Smeće!',	                                            1,	'2021-05-23 12:20:00',	'0c2a1eb0-3d92-4dee-8fa7-e06d49c63b2c',	'b39f0709-574b-4e62-82b4-6dd98eca93a6');
INSERT INTO review VALUES('ceed9dc0-a134-4f78-ba47-6f8c3c6bfad4',	'9/10',                                             	5,	'2021-05-23 12:20:00',	'3ec4e897-bc9d-441b-9cea-215a1a0e7018',	'b39f0709-574b-4e62-82b4-6dd98eca93a6');
INSERT INTO review VALUES('012115df-8349-4289-918d-450051ed3f2e',	'Jako uzbudljivo,	kraj je veoma nepredvidljiv.',    	4,	'2021-05-23 12:20:00',	'e8b7719a-2bdd-4365-96a4-079a75e53898',	'b39f0709-574b-4e62-82b4-6dd98eca93a6');

/*-------------------reading_list---------------------------------*/
INSERT INTO reading_list VALUES('75f5a8b2-ca9f-4ef7-ae75-ca496434eccb',	'054865b6-262c-4ab7-9dad-72b0c43c1be8',	'b39f0709-574b-4e62-82b4-6dd98eca93a6');
INSERT INTO reading_list VALUES('1dcee1c9-95d9-42dc-ac4a-c0497392e028',	'3ec4e897-bc9d-441b-9cea-215a1a0e7018',	'b39f0709-574b-4e62-82b4-6dd98eca93a6');
INSERT INTO reading_list VALUES('85cb3933-7110-4db4-a34e-b23c04312329',	'6147c0ff-fe26-4303-b417-46e1eb9a998d',	'b39f0709-574b-4e62-82b4-6dd98eca93a6');
INSERT INTO reading_list VALUES('76175e0c-8dca-4d74-b279-0c469ff50080',	'28d17353-7c03-4833-b46d-3c11e118294a',	'b39f0709-574b-4e62-82b4-6dd98eca93a6');
INSERT INTO reading_list VALUES('dbaa1985-9313-4eb9-8440-61f04fa49e25',	'91005749-dbd4-4e12-95fa-308bfd956e10',	'b39f0709-574b-4e62-82b4-6dd98eca93a6');
