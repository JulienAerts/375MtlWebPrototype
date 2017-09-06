
DROP TABLE IF EXISTS activites CASCADE;
DROP TABLE IF EXISTS dates;
DROP TABLE IF EXISTS stations_bixi CASCADE;
DROP EXTENSION IF EXISTS Postgis;

CREATE EXTENSION Postgis;

CREATE TABLE activites (
    id int primary key,
    nom text,
    description text,
    arrondissement text,
    lieu text,
    coordonner GEOMETRY(POINT, 4326)
);

CREATE TABLE dates (
    id serial primary key,
    date_activite timestamp with time zone,
    id_activite int references activites(id),
    constraint uniquesID unique (date_activite, id_activite)
);

CREATE TABLE stations_bixi (
    id int primary key,
    nom text,
    id_station text,
    etat int,
    est_bloquer boolean,
    est_maintenance boolean,
    est_hors_usages boolean,
    temps_derniere_mise bigint,
    temps_derniere_com bigint,
    bk boolean,
    bl boolean,
    coordonner GEOMETRY(POINT, 4326),
    nb_termino_dispo int,
    nb_termino_non_dispo int,
    nb_bixi_dispo int,
    nb_bixi_non_dispo int
);
