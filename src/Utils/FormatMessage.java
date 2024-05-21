package Utils;

public class FormatMessage {
    public void formatMsg(String text, Integer spaces, Boolean isLastOne ) {
        int blank_spaces_count;
        blank_spaces_count = spaces - text.length();
        String blank_spaces = " ", repeated;
        repeated = new String( new char[blank_spaces_count]).replace("\0", blank_spaces);

        if( isLastOne ){
            System.out.println(text+repeated + "|");
        }else{
            System.out.print(text+repeated + "|");
        }
    }

    public void boxFormating(String letter, Integer repeatedTimes){
        String repeatedCharacter;
        repeatedCharacter = new String( new char[repeatedTimes]).replace("\0", letter);
        System.out.println(repeatedCharacter);
    }


    //? Mostrar mensaje si no hay datos
    public void emptyDataList(String msg){
        boxFormating("*", 65);
        formatMsg(msg,65, true);
        boxFormating("*", 65);
    }

    public void headerBox(){
        boxFormating("_", 130);
        formatMsg("| Codigo Municipio", 32, false);
        formatMsg(" Municipio",34, false);
        formatMsg(" Departamento",25, false);
        formatMsg(" Codigo departamento", 35, true);
//        formatMsg(" Total", 30, false);
//        formatMsg(" Ultima modificacion", 24, true);
        boxFormating("_", 130);
    }

    public void bodyBox(String codigo, String municipio, String nombre, String codigo_dep){
        formatMsg("| " + codigo, 32, false);
        formatMsg(" "+municipio,34, false);
        formatMsg(" "+ nombre,25, false);
        formatMsg(" " + codigo_dep, 35, true);
//        formatMsg(" "+ total, 30, false);
//        formatMsg(" "+ date, 24, true);
    }

    public void footerBox(int unidadades_totales, Float cantidad_total){
        boxFormating("_", 145);
        formatMsg("| ", 22, false);
        formatMsg(" ",34, false);
        formatMsg(" "+ unidadades_totales,12, false);
        formatMsg(" " , 18, false);
        formatMsg(" "+ cantidad_total+ " Gran Total", 30, false);
        formatMsg(" ", 24, true);
        boxFormating("_", 145);
    }

    public void footerEndBox(){
        boxFormating("_", 130);
    }

}
