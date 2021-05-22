/*------------------------ROLE-----------------------------------------*/
INSERT INTO authority (name) VALUES ('ROLE_READER');
INSERT INTO authority (name) VALUES ('ROLE_ADMIN');

/*------------------------reader-----------------------------------------*/

insert into "user" values (1, 'harry.gegic@gmail.com', true, 'Haris', 'Gegic', null, 'hashedPassword');
insert into reader values (20, 1);
insert into user_authority values (1, 1);

/*------------------------admin-----------------------------------------*/

insert into "user" values (2, 'admin@mail.com', true, 'Admin', 'Adminic', null, 'hashedPassword');
insert into admin values (2);
insert into user_authority values (2, 2);

/*------------------------book-----------------------------------------*/

insert into category values (1, true, 'Neka kategorija', 'Kategorija');

/*------------------------book-----------------------------------------*/

insert into book values (1, 0, 'Some description', null, 'Knjiga', 240, 1, 1);