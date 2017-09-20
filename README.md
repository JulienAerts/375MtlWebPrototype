

## Prérequis

- Java 1.8+
- Maven 3.0+
- PostgreSQL 9.4+ avec PostGIS 2.0+

## Migrations Dispo:\migrations

- create-database.sql
- create-schema.sql

## Document Raml Dispo:\src\main\resources\documentationRest

- API REST ACTIVITES.raml
- API REST STATIONBIXI.raml

## PostgreSQL

- [Installation de PostgreSQL et de PostGIS pour Windows](http://www.bostongis.com/PrinterFriendly.aspx?content_name=postgis_tut01)
- [PostgreSQL.app pour OSX](http://postgresapp.com/)

## Compilation et exécution

    $ mvn spring-boot:run

Le projet est alors disponible à l'adresse [http://localhost:8080/](http://localhost:8080/)
