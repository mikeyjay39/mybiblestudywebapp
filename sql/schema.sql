
create table users
(
    user_id bigserial not null
        constraint user_pk
            primary key,
    email varchar(128) not null,
    firstname varchar(64),
    lastname varchar(64),
    password varchar(128) not null,
    ranking integer default 0,
    created_at timestamp default now(),
    enabled boolean default true
);

comment on table users is '// Enabled column is for Spring Security. Only enabled accounts (active) will be authenticated
// admin@admin.com password is 12345';

create unique index user_email_uindex
    on users (email);

create table views
(
    view_id bigserial not null
        constraint view_pk
            primary key,
    user_id bigint not null
        constraint view_user__fk
            references users,
    view_code uuid default uuid_generate_v1() not null,
    priv boolean default false
);

create unique index views_view_code_uindex
    on views (view_code);

create table books
(
    book_id bigserial not null
        constraint book_pk
            primary key,
    title varchar(64) not null,
    testament varchar(2)
);

create unique index book_title_uindex
    on books (title);

create table chapters
(
    chapter_id bigserial not null
        constraint chapter_pk
            primary key,
    book_id bigint not null
        constraint chapter_book__fk
            references books,
    chapter_no integer not null
);

create table notes
(
    note_id bigserial not null
        constraint note_pk
            primary key,
    note text,
    user_id bigint
        constraint note_user__fk
            references users,
    book_id bigint
        constraint note_book__fk
            references books,
    chapter_id bigint
        constraint note_chapter__fk
            references chapters,
    verse_start integer not null,
    ranking integer default 0,
    priv boolean default false not null,
    lang varchar(64) default 'en'::character varying,
    created_at timestamp default now() not null,
    last_modified timestamp,
    verse_end integer
);

create table view_note
(
    view_note_id bigserial not null
        constraint view_note_pk
            primary key,
    view_id bigint
        constraint view_note_view__fk
            references views
            on update cascade on delete cascade,
    note_id bigint
        constraint view_note_note__fk
            references notes
            on update cascade on delete cascade,
    constraint view_note_pk_2
        unique (view_id, note_id)
);

create table comments
(
    comment_id bigserial not null
        constraint comment_pk
            primary key,
    user_id integer
        constraint comment_user_user_id_fk
            references users
            on update cascade on delete cascade,
    note_id integer
        constraint comment_note_note_id_fk
            references notes
            on update cascade on delete cascade,
    created_at timestamp default now(),
    comment text
);

comment on table comments is 'Used for user comments on notes';

create table user_authorities
(
    user_authorities_id bigserial not null
        constraint user_authorities_pk
            primary key,
    email varchar(128) not null
        constraint user_authorities_users_email_fk
            references users (email)
            on update cascade on delete cascade,
    authority varchar(64) default 'USER'::character varying not null
);

create unique index user_authorities_user_authorities_id_uindex
    on user_authorities (user_authorities_id);

create unique index user_authorities_email_uindex
    on user_authorities (email);

create table rank_note
(
    rank_note_id bigserial not null
        constraint rank_note_pk
            primary key,
    note_id bigint not null
        constraint rank_note_notes_note_id_fk
            references notes
            on update cascade on delete cascade,
    user_id bigint not null
        constraint rank_note_users_user_id_fk
            references users
            on update cascade on delete cascade,
    ranking_value integer default 0,
    constraint rank_note_pk_2
        unique (note_id, user_id)
);

comment on table rank_note is 'Used to map users to notes so that they can only rank a note once';

create unique index rank_note_rank_note_id_uindex
    on rank_note (rank_note_id);