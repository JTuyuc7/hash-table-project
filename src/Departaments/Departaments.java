package Departaments;

import DbConnection.DbConnection;
import Utils.FormatMessage;
import Utils.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Departaments {

    DbConnection connection = new DbConnection();
    ResultSet departamentsData = null;
    Queries queries = new Queries();
    FormatMessage formatMessage = new FormatMessage();
    public void showDepData() throws SQLException {
        departamentsData = connection.readFromDb(queries.ALL_DATA);

        if(departamentsData != null) {
            formatMessage.headerBox();
            while(departamentsData.next()) {
                String codigo = departamentsData.getString("codigo");
                String municipio = departamentsData.getString("municipio");
                String nombre = departamentsData.getString("nombre");
                String codigo_dep = departamentsData.getString("codigo_dep");
//                System.out.println(departamentsData.getString("municipio"));
                formatMessage.bodyBox(codigo, municipio, nombre, codigo_dep);
            }
            formatMessage.footerEndBox();
        }else {
            System.out.println("No data found");
        }

        connection.closeConnection();
    }
}
