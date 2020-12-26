CREATE DATABASE todo;

\c todo;

CREATE TABLE IF NOT EXISTS roles
(
    id integer NOT NULL DEFAULT nextval('roles_id_seq'::regclass),
    name character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT roles_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users
(
    id integer NOT NULL DEFAULT nextval('users_id_seq'::regclass),
    name character varying(100) COLLATE pg_catalog."default",
    email character varying(100) COLLATE pg_catalog."default",
    password character varying(100) COLLATE pg_catalog."default",
    role integer,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_roles_fkey FOREIGN KEY (role)
        REFERENCES public.roles (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS items
(
    id integer NOT NULL DEFAULT nextval('items_id_seq'::regclass),
    description character varying(200) COLLATE pg_catalog."default",
    done boolean,
    created timestamp without time zone,
    userid integer,
    CONSTRAINT items_pkey PRIMARY KEY (id),
    CONSTRAINT items_users_fkey FOREIGN KEY (userid)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

INSERT INTO roles (name)
VALUES ('admin'),
       ('user'),
       ('anonymous');