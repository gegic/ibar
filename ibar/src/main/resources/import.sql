/*------------------------ROLE-----------------------------------------*/
INSERT INTO "authority" (name) VALUES ('ROLE_READER');
INSERT INTO "authority" (name) VALUES ('ROLE_ADMIN');

/*------------------------reader-----------------------------------------*/

insert into "user" values (1,	'harry.gegic@gmail.com',	true,	'Haris',	'Gegic',	null,	'$2a$10$TfA5Evj/8pQr0GIrFmWeRe.w0jWZD.1zoFl5MkpyzPZmvbPYWJds.');
insert into reader values (20,	true,	1);
insert into user_authority values (1,	1);

/*------------------------admin-----------------------------------------*/

insert into "user" values (2,	'admin@mail.com',	true,	'Admin',	'Adminic',	null,	'$2a$10$TfA5Evj/8pQr0GIrFmWeRe.w0jWZD.1zoFl5MkpyzPZmvbPYWJds.');
insert into admin values (2);
insert into user_authority values (2,	2);

/*------------------------category-----------------------------------------*/

insert into category values (1,	true,	'Neka kategorija',	'Kategorija');

/*-----------------------author--------------------------------------------*/
INSERT INTO author VALUES(1,	0,	'1900-08-07 00:00:00',	'1969-08-07 00:00:00',	'Some description',	null,	'Agata Kristi');
INSERT INTO author VALUES(2,	0,	'1900-08-07 00:00:00',	'1969-08-07 00:00:00',	'Some description',	null,	'Donato Karizi');
INSERT INTO author VALUES(3,	0,	'1900-08-07 00:00:00',	'1969-08-07 00:00:00',	'Some description',	null,	'Fjodor Mihailovič Dostojevski');

/*------------------------book-----------------------------------------*/

insert into book values (1, 	0,	null,	'Some description',	'Ubistvo u Mesopotamiji',   	    0,	'1',	164,	1,	1);
insert into book values (2,	    4,	null,	'Some description',	'Rani slučajevi Herkula Poaroa',	1,  '1',	336,	1,	1);
insert into book values (3, 	0,	null,	'Some description',	'Kukavičje jaje',	                0,	'1',	272,	1,	1);
insert into book values (4, 	0,	null,	'Some description',	'Slonovi pamte',	                0,	'1',	264,	1,	1);
insert into book values (5, 	0,	null,	'Some description',	'Ubistvo Rodžera Akrojda',	        0,	'1',	288,	1,	1);
insert into book values (6,	    0,	null,	'Some description',	'Smrt na Nilu',                 	0,	'1',	306,	1,	1);
insert into book values (7,	    0,	null,	'Some description',	'Ubistvo u Orijent Ekspresu',	    0,	'1',	254,	1,	1);

insert into book values (8,	    5,	null,	'Some description',	'Lovac na duše',	                1,	'1',	319,	1,	1);
insert into book values (9,	    5,	null,	'Some description',	'Dečak od stakla',	                1,	'1',	320,	1,	1);
insert into book values (10,	0,	null,	'Some description',	'Vladar iz senke',	                0,	'1',	332,	1,	1);
insert into book values (11,	0,	null,	'Some description',	'Šaptač',	                        0,	'1',	360,	1,	1);
insert into book values (12,	0,	null,	'Some description',	'Noć mi te uzima',              	0,	'1',	296,	1,	1);
insert into book values (13,	0,	null,	'Some description',	'Gospodar senki',	                0,	'1',	264,	1,	1);

insert into book values (14,	0,	null,	'Some description',	'Braća karamazovi I',	            0,	'1',	458,	1,	1);
insert into book values (15,	0,	null,	'Some description',	'Braća karamazovi II',	            0,	'1',	586,	1,	1);
insert into book values (16,	0,	null,	'Some description',	'Bele noći',	                    0,	'1',	70,	    1,	1);
insert into book values (17,	0,	null,	'Some description',	'Zapisi iz podezemlja',	            0,	'1',	194,	1,	1);
insert into book values (18,	1,	null,	'Some description',	'Idiot - I tom',	                1,	'1',	490,	1,	1);
insert into book values (19,	0,	null,	'Some description',	'Idiot - II tom',	                0,	'1',	426,	1,	1);
insert into book values (20,	0,	null,	'Some description',	'Zločin i kazna',	                0,	'1',	714,	1,	1);
insert into book values (21,	0,	null,	'Some description',	'Bedni ljudi',	                    0,	'1',	159,	1,	1);
insert into book values (22,	0,	null,	'Some description',	'Kockar',                       	0,	'1',	189,	1,	1);

/*-------------------------book_authors-------------------------*/
INSERT INTO book_authors VALUES(1,	1);
INSERT INTO book_authors VALUES(2,	1);
INSERT INTO book_authors VALUES(3,	1);
INSERT INTO book_authors VALUES(4,	1);
INSERT INTO book_authors VALUES(5,	1);
INSERT INTO book_authors VALUES(6,	1);
INSERT INTO book_authors VALUES(7,	1);

INSERT INTO book_authors VALUES(8,	2);
INSERT INTO book_authors VALUES(9,	2);
INSERT INTO book_authors VALUES(10,	2);
INSERT INTO book_authors VALUES(11,	2);
INSERT INTO book_authors VALUES(12,	2);
INSERT INTO book_authors VALUES(13,	2);

INSERT INTO book_authors VALUES(14,	3);
INSERT INTO book_authors VALUES(15,	3);
INSERT INTO book_authors VALUES(16,	3);
INSERT INTO book_authors VALUES(17,	3);
INSERT INTO book_authors VALUES(18,	3);
INSERT INTO book_authors VALUES(19,	3);
INSERT INTO book_authors VALUES(20,	3);
INSERT INTO book_authors VALUES(21,	3);
INSERT INTO book_authors VALUES(22,	3);

/*----------------reading_progress------------------------------------*/
INSERT INTO reading_progress VALUES(1,	'2021-05-23 12:20:00',	80, 	11,	1);

INSERT INTO reading_progress VALUES(2,	'2021-05-23 12:20:00',	310,	8,	1);
INSERT INTO reading_progress VALUES(3,	'2021-05-23 12:20:00',	490,	18,	1);
INSERT INTO reading_progress VALUES(4,	'2021-05-23 12:20:00',	310,	9,	1);
INSERT INTO reading_progress VALUES(5,	'2021-05-23 12:20:00',	328,	2,	1);

/*-------------review---------------------------------*/
INSERT INTO review VALUES(1,	'Mislim da je knjiga zaista sjajna. Sve preporuke!',	5,	'2021-05-23 12:20:00',	8,	1);
INSERT INTO review VALUES(2,	'Smeće!',	                                            1,	'2021-05-23 12:20:00',	18,	1);
INSERT INTO review VALUES(3,	'9/10',                                             	5,	'2021-05-23 12:20:00',	9,	1);
INSERT INTO review VALUES(4,	'Jako uzbudljivo,	kraj je veoma nepredvidljiv.',    	4,	'2021-05-23 12:20:00',	2,	1);

/*-------------------reading_list---------------------------------*/
INSERT INTO reading_list VALUES(1,	15,	1);
INSERT INTO reading_list VALUES(2,	20,	1);
INSERT INTO reading_list VALUES(3,	17,	1);
INSERT INTO reading_list VALUES(4,	7,	1);
INSERT INTO reading_list VALUES(5,	5,	1);
