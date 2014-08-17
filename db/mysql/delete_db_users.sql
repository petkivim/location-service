REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'loc_ser_login'@'localhost';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'loc_ser_admin'@'localhost';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'loc_ser'@'localhost';

FLUSH PRIVILEGES;

drop user 'loc_ser_login'@'localhost';
drop user 'loc_ser_admin'@'localhost';
drop user 'loc_ser'@'localhost';