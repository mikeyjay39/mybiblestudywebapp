create schema public;

comment on schema public is 'standard public schema';

alter schema public owner to postgres;

create table if not exists "user"
(
	user_id serial not null
		constraint user_pk
			primary key,
	email varchar(99) not null,
	firstname varchar(99),
	lastname varchar(99),
	password varchar(999) not null,
	ranking integer default 0,
	created_at timestamp default now()
);

alter table "user" owner to developer;

create unique index if not exists user_email_uindex
	on "user" (email);

create table if not exists view
(
	view_id serial not null
		constraint view_pk
			primary key,
	user_id integer not null
		constraint view_user__fk
			references "user",
	view_code varchar(99) not null,
	private boolean default false
);

alter table view owner to developer;

create unique index if not exists view_view_code_uindex
	on view (view_code);

create table if not exists book
(
	book_id serial not null
		constraint book_pk
			primary key,
	title varchar(99) not null,
	testament varchar(2)
);

alter table book owner to developer;

create unique index if not exists book_title_uindex
	on book (title);

create table if not exists chapter
(
	chapter_id serial not null
		constraint chapter_pk
			primary key,
	book_id integer not null
		constraint chapter_book__fk
			references book,
	chapter_no integer not null
);

alter table chapter owner to developer;

create table if not exists note
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

alter table note owner to developer;

create table if not exists view_note
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

alter table view_note owner to developer;

create table if not exists comment
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

alter table comment owner to developer;

