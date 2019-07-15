

drop table users if exists;
create table users (
    id varchar(80) not null,
    name varchar(80) not null,
    constraint pk_user primary key (id)
);

drop table persons if exists;
create table persons  (
    person_id integer identity not null primary key,
    first_name varchar(20),
    last_name varchar(20),
    operation_by varchar(64),
    operation_at timestamp
);
