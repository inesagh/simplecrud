drop table if exists user;

create table user(
    id bigint not null AUTO_INCREMENT,
    first_name varchar(255),
    last_name varchar (255),

    primary key (id)
);