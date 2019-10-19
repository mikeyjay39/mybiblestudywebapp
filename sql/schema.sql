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
	created_at timestamp default now()
);

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
	verse integer not null,
	ranking integer default 0,
	priv boolean default false not null,
	lang varchar(64),
	created_at timestamp default now() not null,
	last_modified timestamp
);

create table view_note
(
	view_note_id bigserial not null
		constraint view_note_pk
			primary key,
	view_id bigint
		constraint view_note_view__fk
			references views,
	note_id bigint
		constraint view_note_note__fk
			references notes
);

create table comments
(
    comment_id bigserial not null
        constraint comment_pk
            primary key,
    user_id integer
        constraint comment_user_user_id_fk
            references users,
    note_id integer
        constraint comment_note_note_id_fk
            references notes,
    created_at timestamp default now(),
    comment text
);

comment on table comments is 'Used for user comments on notes';

