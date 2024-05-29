package AllData;

import java.util.Objects;

public class CompositeKey {
    private String countryIsoCode;
//    private String countryName;
    private String provIsoCode;
    private String munIsoCode;

    public CompositeKey(String countryIsoCode, String provIsoCode, String munIsoCode) {
        this.countryIsoCode = countryIsoCode;
        this.provIsoCode = provIsoCode;
        this.munIsoCode = munIsoCode;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public String getProvIsoCode() {
        return provIsoCode;
    }

    public String getMunIsoCode() {
        return munIsoCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeKey that = (CompositeKey) o;
        return Objects.equals(countryIsoCode, that.countryIsoCode) &&
                Objects.equals(provIsoCode, that.provIsoCode) &&
                Objects.equals(munIsoCode, that.munIsoCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryIsoCode, provIsoCode, munIsoCode);
    }
}
