import json

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
sql_file = 'init.sql'

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

