package Countries;

public class Country {

    String name;
    String isoCode;
    String officialName;
    int province_count;
    int municipality_count;

    Country(String name, String isoCode, String officialName, int province_count, int municipality_count){
        this.name = name;
        this.isoCode = isoCode;
        this.officialName = officialName;
        this.province_count = province_count;
        this.municipality_count = municipality_count;
    }

//    @Override
//    public String toString() {
//        return "Name: " + name + ", ISO Code: " + isoCode + ", Official Name: " + officialName;
//    }
}
