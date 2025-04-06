create schema if not exists users;

create table if not exists users.user(
    id bigint generated by default as identity primary key ,
    first_name varchar(60) not null ,
    last_name varchar(60) not null ,
    email varchar(70) not null unique ,
    password varchar not null ,
    role varchar not null,
    email_confirmed boolean not null
);

create table if not exists users.confirmation_code(
    id bigint generated by default as identity primary key ,
    code int not null ,
    expiration_date timestamp not null ,
    type varchar not null ,
    user_id bigint references users.user(id) on delete cascade
);

create index on users.user(email);

create table if not exists users.refresh_token (
    id int generated by default as identity primary key,
    user_id bigint references users.user(id) on delete cascade,
    token varchar not null,
    expiry_date timestamp not null,
    foreign key(user_id) references users.user(id) on delete cascade
);