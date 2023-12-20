create user dbuser with encrypted password 'pass';

create database authservice;
grant all privileges on database authservice to dbuser;
