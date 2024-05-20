# DB configuration
To run this project you should have the following tools installed
* Docker
* Python
* Java

There is a `content.json` file at the root of this directory where all the information is stored, we need to find a way to save this content into a portable script which can be injected into the DB once created.

If you are running this the first time, follow the instructions

* You'll need to create a python `env`virtual environment so you can run the next step
* There is a ```generate_sql.py``` file that you have to execute first, once the file is executed, it will create a `init.sql` file that will be used later.
* There is a `compose-variables.templage.env` that you have to rename to `compose-variables.env` inside the file you'll have to provide the following info
    ```
  MYSQL_ROOT_PASSWORD=yourpass  <- add your password to connect to your mysql
    MYSQL_DATABASE=hash_table_db <- you can change this or keep the same name

    PMA_HOST=hash_container_db   <- DO NOT EDIT THIS as the compose file needs this
  ```
* There is a `docker-compose.yaml` file ready to be used, once executed this will pull and create two containers `MYSQL` & `phpmyadmin` and it will auto insert all the data into the DB
  * run `docker-compose up -d` at the same level of this directory.

Once everything set up, you should be able to go to `http://localhost:8081/index.php` access with the user as `root` and the same password you entered in the `env` file, once you logged in you should see two tables.
* `departaments` & `municipalities` with the content.