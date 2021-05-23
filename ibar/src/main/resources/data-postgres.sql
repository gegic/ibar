/*------------------------ROLE-----------------------------------------*/
INSERT INTO authority (name) VALUES ('ROLE_READER');
INSERT INTO authority (name) VALUES ('ROLE_ADMIN');

/*------------------------reader-----------------------------------------*/

insert into "user" values (1, 'harry.gegic@gmail.com', true, 'Haris', 'Gegic', null, '$2a$10$TfA5Evj/8pQr0GIrFmWeRe.w0jWZD.1zoFl5MkpyzPZmvbPYWJds.');
insert into reader values (20, 1);
insert into user_authority values (1, 1);

/*------------------------admin-----------------------------------------*/

insert into "user" values (2, 'admin@mail.com', true, 'Admin', 'Adminic', null, '$2a$10$TfA5Evj/8pQr0GIrFmWeRe.w0jWZD.1zoFl5MkpyzPZmvbPYWJds.');
insert into admin values (2);
insert into user_authority values (2, 2);

/*------------------------category-----------------------------------------*/

insert into category values (1, true, 'Neka kategorija', 'Kategorija');

/*-----------------------author--------------------------------------------*/
INSERT INTO author VALUES(1, 0, null, null, 'Some description', null, 'Agata Kristi');
INSERT INTO author VALUES(2, 0, null, null, 'Some description', null, 'Donato Karizi');
INSERT INTO author VALUES(3, 0, null, null, 'Some description', null, 'Fjodor Mihailovič Dostojevski');

/*------------------------book-----------------------------------------*/

insert into book values (1, 0, 'Some description', null, 'Ubistvo u Mesopotamiji', 164, 1, 1);
insert into book values (2, 0, 'Some description', null, 'Rani slučajevi Herkula Poaroa', 336, 1, 1);
insert into book values (3, 0, 'Some description', null, 'Kukavičje jaje', 272, 1, 1);
insert into book values (4, 0, 'Some description', null, 'Slonovi pamte', 264, 1, 1);
insert into book values (5, 0, 'Some description', null, 'Ubistvo Rodžera Akrojda', 288, 1, 1);
insert into book values (6, 0, 'Some description', null, 'Smrt na Nilu', 306, 1, 1);
insert into book values (7, 0, 'Some description', null, 'Ubistvo u Orijent Ekspresu', 254, 1, 1);

insert into book values (8, 0, 'Some description', null, 'Lovac na duše', 319, 1, 1);
insert into book values (9, 0, 'Some description', null, 'Dečak od stakla', 320, 1, 1);
insert into book values (10, 0, 'Some description', null, 'Vladar iz senke', 332, 1, 1);
insert into book values (11, 0, 'Some description', null, 'Šaptač', 360, 1, 1);
insert into book values (12, 0, 'Some description', null, 'Noć mi te uzima', 296, 1, 1);
insert into book values (13, 0, 'Some description', null, 'Gospodar senki', 264, 1, 1);

insert into book values (14, 0, 'Some description', null, 'Braća karamazovi I', 458, 1, 1);
insert into book values (15, 0, 'Some description', null, 'Braća karamazovi II', 586, 1, 1);
insert into book values (16, 0, 'Some description', null, 'Bele noći', 70, 1, 1);
insert into book values (17, 0, 'Some description', null, 'Zapisi iz podezemlja', 194, 1, 1);
insert into book values (18, 0, 'Some description', null, 'Idiot - I tom', 490, 1, 1);
insert into book values (19, 0, 'Some description', null, 'Idiot - II tom', 426, 1, 1);
insert into book values (20, 0, 'Some description', null, 'Zločin i kazna', 714, 1, 1);
insert into book values (21, 0, 'Some description', null, 'Bedni ljudi', 159, 1, 1);
insert into book values (22, 0, 'Some description', null, 'Kockar', 189, 1, 1);

/*-------------------------book_authors-------------------------*/
INSERT INTO book_authors VALUES(1, 1);
INSERT INTO book_authors VALUES(2, 1);
INSERT INTO book_authors VALUES(3, 1);
INSERT INTO book_authors VALUES(4, 1);
INSERT INTO book_authors VALUES(5, 1);
INSERT INTO book_authors VALUES(6, 1);
INSERT INTO book_authors VALUES(7, 1);

INSERT INTO book_authors VALUES(8, 2);
INSERT INTO book_authors VALUES(9, 2);
INSERT INTO book_authors VALUES(10, 2);
INSERT INTO book_authors VALUES(11, 2);
INSERT INTO book_authors VALUES(12, 2);
INSERT INTO book_authors VALUES(13, 2);

INSERT INTO book_authors VALUES(14, 3);
INSERT INTO book_authors VALUES(15, 3);
INSERT INTO book_authors VALUES(16, 3);
INSERT INTO book_authors VALUES(17, 3);
INSERT INTO book_authors VALUES(18, 3);
INSERT INTO book_authors VALUES(19, 3);
INSERT INTO book_authors VALUES(20, 3);
INSERT INTO book_authors VALUES(21, 3);
INSERT INTO book_authors VALUES(22, 3);

/*----------------reading_progress------------------------------------*/
INSERT INTO reading_progress VALUES(1, null, 80, 11, 1);

INSERT INTO reading_progress VALUES(2, null, 310, 8, 1);
INSERT INTO reading_progress VALUES(3, null, 490, 18, 1);
INSERT INTO reading_progress VALUES(4, null, 310, 9, 1);
INSERT INTO reading_progress VALUES(5, null, 328, 2, 1);

/*-------------review---------------------------------*/
INSERT INTO review VALUES(1, 'Mislim da je knjiga zaista sjajna. Sve preporuke!', 5, 8, 1);
INSERT INTO review VALUES(2, 'Smeće!', 1, 18, 1);
INSERT INTO review VALUES(3, '9/10', 5, 9, 1);
INSERT INTO review VALUES(4, 'Jako uzbudljivo, kraj je veoma nepredvidljiv.', 4, 2, 1);

/*-------------------reading_list---------------------------------*/
INSERT INTO reading_list VALUES(1, 15, 1);
INSERT INTO reading_list VALUES(2, 20, 1);
INSERT INTO reading_list VALUES(3, 17, 1);
INSERT INTO reading_list VALUES(4, 7, 1);
INSERT INTO reading_list VALUES(5, 5, 1);
