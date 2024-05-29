package AllData;

public class FullData {

    String country_name;
    String country_iso_code;
    String country_official_name;
    String prov_iso_code;
    String prov_name;
    String mun_iso_code;
    String mun_name;

    FullData(String country_iso_code, String country_name, String country_official_name, String prov_iso_code, String prov_name, String mun_iso_code, String mun_name){
        this.country_name = country_name;
        this.country_iso_code = country_iso_code;
        this.country_official_name = country_official_name;
        this.prov_iso_code = prov_iso_code;
        this.prov_name = prov_name;
        this.mun_iso_code = mun_iso_code;
        this.mun_name = mun_name;
    }
}
