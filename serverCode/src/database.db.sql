DROP TABLE IF EXISTS "authToken";
CREATE TABLE "authToken" ("userID" INTEGER NOT NULL , "token" VARCHAR PRIMARY KEY  NOT NULL  UNIQUE );
DROP TABLE IF EXISTS "event";
CREATE TABLE "event" ("eventID" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , "descendentID" INTEGER NOT NULL , "personID" INTEGER NOT NULL , "latitude" INTEGER NOT NULL , "longitude" INTEGER NOT NULL , "country" VARCHAR NOT NULL , "city" VARCHAR NOT NULL , "eventType" VARCHAR NOT NULL , "year" VARCHAR NOT NULL );
DROP TABLE IF EXISTS "persons";
CREATE TABLE "persons" ("personID" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , "descendentID" INTEGER NOT NULL , "firstName" VARCHAR NOT NULL , "lastName" VARCHAR NOT NULL , "fatherID" INTEGER, "motherID" INTEGER, "spouseID" INTEGER, "gender" VARCHAR NOT NULL );
DROP TABLE IF EXISTS "users";
CREATE TABLE "users" ("personID" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , "username" VARCHAR NOT NULL  UNIQUE , "email " VARCHAR NOT NULL , "firstName" VARCHAR NOT NULL , "lastName" VARCHAR NOT NULL , "gender" VARCHAR NOT NULL );
