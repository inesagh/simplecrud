alter table user
    add column username varchar(255) unique not null;
alter table user
    add column password varchar(255) not null;