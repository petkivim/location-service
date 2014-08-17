REVOKE ALL PRIVILEGES ON DATABASE location_service FROM loc_ser_login;
REVOKE ALL PRIVILEGES ON SCHEMA public FROM loc_ser_login;

REVOKE ALL PRIVILEGES ON DATABASE location_service FROM loc_ser_admin;
REVOKE ALL PRIVILEGES ON SCHEMA public FROM loc_ser_admin;

REVOKE ALL PRIVILEGES ON DATABASE location_service FROM loc_ser;
REVOKE ALL PRIVILEGES ON SCHEMA public FROM loc_ser;

DROP role IF EXISTS loc_ser_login;
DROP role IF EXISTS loc_ser_admin;
DROP role IF EXISTS loc_ser;