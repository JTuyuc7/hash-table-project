import os
import pycountry
import json
import warnings
from unidecode import unidecode

warnings.filterwarnings("ignore", category=UserWarning)


class GetCountries:
    def __init__(self):
        self.country_list = []
        self.excluded_countries = {'MX', 'BZ', 'CR', 'SV', 'GT', 'HN', 'NI', 'PA'}

    def get_countries_iso_info(self):
        countries = []
        for country in pycountry.countries:
            if country.alpha_2 in self.excluded_countries:
                continue
            official_name = getattr(country, 'official_name', "") if hasattr(country, 'official_name') else ""
            name = unidecode(country.name)
            official_n = unidecode(official_name)
            countries.append({"name": name, "isoCode": country.alpha_2, "official_name": official_n})
        print(len(countries), ' <- cantidad de paises')
        return {"countries": countries}

    def generate_codes(self) -> object:
        country_list = self.get_countries_iso_info()

        os.makedirs('./Content', exist_ok=True)  # Create directory if it doesn't exist
        filepath = os.path.join('./Content', 'countriesIso.json')  # Build full path
        with open(filepath, 'w', encoding='utf-8') as json_file:
            json.dump(country_list, json_file, ensure_ascii=False, indent=2)

        print("Archivo JSON generado correctamente.")
