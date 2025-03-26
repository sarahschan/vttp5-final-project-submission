CREATE TABLE users ( 
    id int auto_increment,
    name varchar(125) not null,
    username varchar(50) unique not null,
    email varchar(125) unique not null,
    password_hash varchar(255) not null,
    profile_picture varchar(255),
    short_bio text,

    constraint pk_id primary key (id)
);
