#### MySQL Database

init_db.sql file generates the 'location_service' database and creates admin user for the Location Service Admin application. 

How to import the SQL file in the database.

```
mysql -u root -p < init_db.sql
```

Create the database users: loc_ser_login, loc_ser_admin, loc_ser.

```
mysql -u root -p < create_db_users.sql
```

Default passwords for the database users are listed below.

* username: _loc_ser_login_, password: _location_service_login_
  * Used for the admin application's user authentication only
* username: _loc_ser_admin_, password: _location_service_admin_
  * Used by the admin application
* username: _loc_ser_, password: _location_service_
  * Used by the endpoint application
