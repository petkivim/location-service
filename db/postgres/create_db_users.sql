CREATE USER loc_ser_login WITH password 'location_service_login';
CREATE USER loc_ser_admin WITH password 'location_service_admin';
CREATE USER loc_ser WITH password 'location_service';

\connect location_service

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public to loc_ser_admin;

GRANT CONNECT ON DATABASE location_service to loc_ser;
GRANT USAGE ON SCHEMA public TO loc_ser;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO loc_ser;
GRANT INSERT ON search_event TO loc_ser;

GRANT CONNECT ON DATABASE location_service to loc_ser_login;
GRANT USAGE ON SCHEMA public TO loc_ser_login;
GRANT SELECT ON my_user, user_info TO loc_ser_login;