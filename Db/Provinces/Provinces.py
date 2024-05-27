import requests
import json
from unidecode import unidecode
import os


class Provinces:
    def __init__(self, file_content_name, file_to_save_name, prov=""):
        self.prov = prov
        self.file_content_name = file_content_name
        self.file_to_save_name = file_to_save_name


    @staticmethod
    def get_hash_code(departament):
        hash_code = 0
        for letter in departament:
            hash_code += ord(letter) + 256
        return hash_code

    def get_converted_prov(self, provinces):
        converted_dep = []
        for province in provinces:
            hash_code = self.get_hash_code(unidecode(province))
            search_name = unidecode(province).lower().replace(' ', '')
            converted_dep.append({"prov": unidecode(province), "prov_code": hash_code, "search_name": search_name, "municipalities": []})
        return converted_dep

    def get_provinces_by_country(self, country_code):
        url = f'http://api.geonames.org/searchJSON?country={country_code}&featureCode=ADM1&maxRows=1000&username=jtuyuc17'
        response = requests.get(url)
        data = response.json()
        provinces = [place['name'] for place in data['geonames']]
        print(len(provinces), ' <- dep ')
        converted_provinces = self.get_converted_prov(provinces=provinces)
        # print(prov, 'converted')
        return converted_provinces

    def get_all_provinces(self):
        print('starting')
        converted_data = []
        with open('./Content/' + self.file_content_name + '.json', 'r') as json_content:
            data = json.load(json_content)
            countries = data['countries']
            for country in countries:
                print(country['isoCode'])
                country_code = country['isoCode']
                provinces = self.get_provinces_by_country(country_code)
                country['provinces'] = provinces
                print(provinces, 'combinados?')
                yield country

    def combine_countries_and_prov(self):
        all_countries_with_prov = []
        for country in self.get_all_provinces():
            all_countries_with_prov.append(country)

        os.makedirs('./Content', exist_ok=True)  # Create directory if it doesn't exist
        filepath = os.path.join('./Content',  self.file_to_save_name + '.json')  # Build full path
        with open(filepath, 'w', encoding='utf-8') as json_file:
            json.dump({"countries": all_countries_with_prov}, json_file, ensure_ascii=False, indent=2)

