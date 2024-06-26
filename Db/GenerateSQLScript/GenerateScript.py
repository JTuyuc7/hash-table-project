import hashlib
import string
import requests
import json
from unidecode import unidecode
import os

BASE62 = string.digits + string.ascii_letters

class GenerateScript:
    def __init__(self):
        pass

    @staticmethod
    def base62_encode(num):
        """Encode a number in Base62 (0-9, A-Z, a-z)."""
        if num == 0:
            return BASE62[0]
        arr = []
        base = len(BASE62)
        while num:
            num, rem = divmod(num, base)
            arr.append(BASE62[rem])
        arr.reverse()
        return ''.join(arr)

    @staticmethod
    def get_hash_code(departament):
        # Create a SHA-256 hash of the input
        sha = hashlib.sha256()
        sha.update(departament.encode('utf-8'))
        hash_code = int(sha.hexdigest(), 16)

        # Convert the hash code to base62
        base62_hash = GenerateScript.base62_encode(hash_code)

        # Ensure the hash code is 10 characters long
        if len(base62_hash) > 10:
            return base62_hash[:10]
        else:
            return base62_hash.zfill(10)

    def escape_sql_string(self, value):
        """Escapes single quotes in a string for SQL."""
        return value.replace("'", "''")

    def new_script_imp(self):
        # Load JSON data
        try:
            with open('./Content/content.json', encoding='utf-8') as f:
                data = json.load(f)
        except FileNotFoundError:
            print("Error: 'content.json' file not found.")
            exit(1)  # Exit the script or handle the error as appropriate
        except json.JSONDecodeError:
            print("Error: Invalid JSON format in 'content.json' file.")
            exit(1)  # Exit the script or handle the error as appropriate

        # SQL file to be generated
        sql_file = 'init.sql'

        with open(sql_file, 'w', encoding='utf-8') as f:
            f.write("-- Create the database schema with UTF-8 encoding\n")
            f.write("CREATE DATABASE IF NOT EXISTS hash_table_db\n")
            f.write("    DEFAULT CHARACTER SET utf8mb4\n")
            f.write("    DEFAULT COLLATE utf8mb4_unicode_ci;\n")
            f.write("USE hash_table_db;\n\n")

            f.write("-- Create the countries table with UTF-8 encoding\n")
            f.write("CREATE TABLE IF NOT EXISTS countries (\n")
            f.write("    id INT AUTO_INCREMENT PRIMARY KEY,\n")
            f.write("    name VARCHAR(255) NOT NULL,\n")
            f.write("    iso_code VARCHAR(10) NOT NULL,\n")
            f.write("    official_name VARCHAR(255) NOT NULL\n")
            f.write(") CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;\n\n")

            f.write("-- Create the provinces table with UTF-8 encoding\n")
            f.write("CREATE TABLE IF NOT EXISTS provinces (\n")
            f.write("    id INT AUTO_INCREMENT PRIMARY KEY,\n")
            f.write("    country_id INT,\n")
            f.write("    prov VARCHAR(255) NOT NULL,\n")
            f.write("    prov_code VARCHAR(10) NOT NULL,\n")
            f.write("    search_name VARCHAR(255) NOT NULL,\n")
            f.write("    FOREIGN KEY (country_id) REFERENCES countries(id)\n")
            f.write(") CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;\n\n")

            f.write("-- Create the municipalities table with UTF-8 encoding\n")
            f.write("CREATE TABLE IF NOT EXISTS municipalities (\n")
            f.write("    id INT AUTO_INCREMENT PRIMARY KEY,\n")
            f.write("    province_id INT,\n")
            f.write("    mun VARCHAR(255) NOT NULL,\n")
            f.write("    mun_code VARCHAR(10) NOT NULL,\n")
            f.write("    search_name VARCHAR(255) NOT NULL,\n")
            f.write("    FOREIGN KEY (province_id) REFERENCES provinces(id)\n")
            f.write(") CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;\n\n")

            # Insert countries, provinces, and municipalities
            for country in data['countries']:
                country_name = self.escape_sql_string(country['name'])
                country_iso_code = self.escape_sql_string(country['isoCode'])
                country_official_name = self.escape_sql_string(country['official_name'])
                f.write(f"INSERT INTO countries (name, iso_code, official_name) VALUES ('{country_name}', '{country_iso_code}', '{country_official_name}');\n")

            f.write("COMMIT;\n")

            for country in data['countries']:
                country_name = self.escape_sql_string(country['name'])
                country_iso_code = self.escape_sql_string(country['isoCode'])
                country_official_name = self.escape_sql_string(country['official_name'])
                country_id_query = f"(SELECT id FROM countries WHERE name='{country_name}' AND iso_code='{country_iso_code}' AND official_name='{country_official_name}')"

                for province in country['provinces']:
                    province_name = self.escape_sql_string(province['prov'])
                    province_code = self.escape_sql_string(province['prov_code'])
                    province_search_name = self.escape_sql_string(province['search_name'])
                    f.write(f"INSERT INTO provinces (country_id, prov, prov_code, search_name) VALUES ({country_id_query}, '{province_name}', '{province_code}', '{province_search_name}');\n")

            f.write("COMMIT;\n")

            for country in data['countries']:
                country_name = self.escape_sql_string(country['name'])
                country_iso_code = self.escape_sql_string(country['isoCode'])
                country_official_name = self.escape_sql_string(country['official_name'])
                country_id_query = f"(SELECT id FROM countries WHERE name='{country_name}' AND iso_code='{country_iso_code}' AND official_name='{country_official_name}')"

                for province in country['provinces']:
                    province_name = self.escape_sql_string(province['prov'])
                    province_code = self.escape_sql_string(province['prov_code'])
                    province_search_name = self.escape_sql_string(province['search_name'])
                    province_id_query = f"(SELECT id FROM provinces WHERE country_id={country_id_query} AND prov='{province_name}' AND prov_code='{province_code}' AND search_name='{province_search_name}')"

                    for municipality in province['municipalities']:
                        mun_name = self.escape_sql_string(municipality['mun'])
                        mun_code = self.escape_sql_string(municipality['mun_code'])
                        mun_search_name = self.escape_sql_string(municipality['search_name'])
                        f.write(f"INSERT INTO municipalities (province_id, mun, mun_code, search_name) VALUES ({province_id_query}, '{mun_name}', '{mun_code}', '{mun_search_name}');\n")

            f.write("COMMIT;\n")

        print(f"New SQL file '{sql_file}' has been generated.")


    def old_implementation(self):
        # Load JSON data
        try:
            with open('content.json', encoding='utf-8') as f:
                data = json.load(f)
        except FileNotFoundError:
            print("Error: 'content.json' file not found.")
            # Exit the script or handle the error as appropriate
        except json.JSONDecodeError:
            print("Error: Invalid JSON format in 'content.json' file.")

        # SQL file to be generated
        sql_file = 'init_old.sql'

        with open(sql_file, 'w', encoding='utf-8') as f:
            f.write("-- Create the database schema with UTF-8 encoding\n")
            f.write("CREATE DATABASE IF NOT EXISTS hash_table_db\n")
            f.write("    DEFAULT CHARACTER SET utf8mb4\n")
            f.write("    DEFAULT COLLATE utf8mb4_unicode_ci;\n")
            f.write("USE hash_table_db;\n\n")

            f.write("-- Create the departments table with UTF-8 encoding\n")
            f.write("CREATE TABLE IF NOT EXISTS departments (\n")
            f.write("    id INT AUTO_INCREMENT PRIMARY KEY,\n")
            f.write("    nombre VARCHAR(255) NOT NULL,\n")
            f.write("    codigo VARCHAR(10) NOT NULL\n")
            f.write(") CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;\n\n")

            f.write("-- Create the municipalities table with UTF-8 encoding\n")
            f.write("CREATE TABLE IF NOT EXISTS municipalities (\n")
            f.write("    id INT AUTO_INCREMENT PRIMARY KEY,\n")
            f.write("    codigo INT NOT NULL,\n")
            f.write("    municipio VARCHAR(255) NOT NULL,\n")
            f.write("    department_id INT,\n")
            f.write("    FOREIGN KEY (department_id) REFERENCES departments(id)\n")
            f.write(") CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;\n\n")

            # Insert departments and municipalities
            for department in data['departamentos']:
                nombre = department['nombre']
                codigo = department['codigo']
                f.write(f"INSERT INTO departments (nombre, codigo) VALUES ('{nombre}', '{codigo}');\n")

                department_id_query = f"(SELECT id FROM departments WHERE nombre='{nombre}' AND codigo='{codigo}')"
                for municipio in department['municipios']:
                    if isinstance(municipio, str):
                        try:
                            municipio_dict = json.loads(municipio)  # Try parsing municipio as JSON
                            mun_codigo = municipio_dict.get('codigo', '')  # Get the codigo field
                            mun_nombre = municipio_dict.get('municipio', '')  # Get the municipio field
                            f.write(f"INSERT INTO municipalities (codigo, municipio, department_id) VALUES ({mun_codigo}, '{mun_nombre}', {department_id_query});\n")
                        except json.JSONDecodeError:
                            print(f"Error: Unable to parse municipio as JSON: {municipio}")
                    elif isinstance(municipio, dict):
                        mun_codigo = municipio.get('codigo', '')
                        mun_nombre = municipio.get('municipio', '')
                        f.write(f"INSERT INTO municipalities (codigo, municipio, department_id) VALUES ({mun_codigo}, '{mun_nombre}', {department_id_query});\n")
                    else:
                        print(f"Error: Unexpected type for municipio: {type(municipio)}")
                # for municipio in department['municipios']:
                #     mun_codigo = int(municipio['codigo'])
                #     mun_nombre = municipio['municipio']
                #     f.write(f"INSERT INTO municipalities (codigo, municipio, department_id) VALUES ({mun_codigo}, '{mun_nombre}', {department_id_query});\n")

        print(f"SQL file '{sql_file}' has been generated.")