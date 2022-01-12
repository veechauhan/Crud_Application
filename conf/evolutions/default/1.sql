-- !Ups

create table users (
    id uuid primary key,
    name text not null,
    password text not null
);

-- !Downs

drop table users;
