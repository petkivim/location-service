CREATE USER 'loc_ser_login'@'localhost' IDENTIFIED BY 'location_service_login';
CREATE USER 'loc_ser_admin'@'localhost' IDENTIFIED BY 'location_service_admin';
CREATE USER 'loc_ser'@'localhost' IDENTIFIED BY 'location_service';

GRANT SELECT ON location_service.MY_USER TO 'loc_ser_login'@'localhost';
GRANT SELECT ON location_service.USER_INFO TO 'loc_ser_login'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE, LOCK TABLES ON location_service.* TO 'loc_ser_admin'@'localhost';
GRANT SELECT, LOCK TABLES ON location_service.* TO 'loc_ser'@'localhost';
GRANT RELOAD ON *.* TO 'loc_ser_login'@'localhost', 'loc_ser_admin'@'localhost', 'loc_ser'@'localhost';
GRANT INSERT ON location_service.SEARCH_EVENT TO 'loc_ser'@'localhost';

FLUSH PRIVILEGES;