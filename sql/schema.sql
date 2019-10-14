comment on schema public is 'standard public schema';

alter schema public owner to postgres;

create table "user"
(
    user_id serial not null
        constraint user_pk
            primary key,
    email varchar(128) not null,
    firstname varchar(64),
    lastname varchar(64),
    password varchar(128) not null,
    ranking integer default 0,
    created_at timestamp default now()
);



create unique index user_email_uindex
    on "user" (email);

create table view
(
    view_id serial not null
        constraint view_pk
            primary key,
    user_id integer not null
        constraint view_user__fk
            references "user",
    view_code varchar(64) not null,
    private boolean default false
);



create unique index view_view_code_uindex
    on view (view_code);

create table book
(
    book_id serial not null
        constraint book_pk
            primary key,
    title varchar(64) not null,
    testament varchar(2)
);



create unique index book_title_uindex
    on book (title);

create table chapter
(
    chapter_id serial not null
        constraint chapter_pk
            primary key,
    book_id integer not null
        constraint chapter_book__fk
            references book,
    chapter_no integer not null
);


create table note
(
    note_id serial not null
        constraint note_pk
            primary key,
    note text,
    user_id integer
        constraint note_user__fk
            references "user",
    book_id integer
        constraint note_book__fk
            references book,
    chapter_id integer
        constraint note_chapter__fk
            references chapter,
    verse integer not null,
    ranking integer default 0,
    private boolean default false not null,
    lang varchar(64),
    created_at timestamp default now() not null,
    last_modified timestamp
);


create table view_note
(
    view_note_id integer not null
        constraint view_note_pk
            primary key,
    view_id integer
        constraint view_note_view__fk
            references view,
    note_id integer
        constraint view_note_note__fk
            references note
);


create table comment
(
    comment_id integer not null
        constraint comment_pk
            primary key,
    user_id integer
        constraint comment_user_user_id_fk
            references "user",
    note_id integer
        constraint comment_note_note_id_fk
            references note,
    created_at timestamp default now()
);

comment on table comment is 'Used for user comments on notes';


