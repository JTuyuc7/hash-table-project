import json
import requests
from unidecode import unidecode
import os
import hashlib
import string

BASE62 = string.digits + string.ascii_letters

class CentralAmericaData:
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
        base62_hash = CentralAmericaData.base62_encode(hash_code)

        # Ensure the hash code is 10 characters long
        if len(base62_hash) > 10:
            return base62_hash[:10]
        else:
            return base62_hash.zfill(10)

    def get_converted_mun(self, municipalities):
        converted_mun = []
        for municipality in municipalities:
            hash_code = self.get_hash_code(unidecode(municipality))
            search_name = unidecode(municipality).lower().replace(' ', '')
            converted_mun.append({"mun": unidecode(municipality), "mun_code": hash_code, "search_name": search_name})
        return converted_mun

    def get_department_geoname_id(self, country_code, department_name):
        url = f'http://api.geonames.org/searchJSON?country={country_code}&name_equals={department_name}&featureCode=ADM1&username=jtuyuc17'
        response = requests.get(url)
        data = response.json()
        if data['geonames']:
            return data['geonames'][0]['geonameId']
        else:
            return None

    def get_municipalities(self, department_geoname_id):
        url = f'http://api.geonames.org/childrenJSON?geonameId={department_geoname_id}&maxRows=10&username=jtuyuc17'
        response = requests.get(url)
        data = response.json()
        municipalities = [place['name'] for place in data['geonames']]
        converted_data = self.get_converted_mun(municipalities)
        return converted_data

    def get_central_america_data(self):
        with open('./Content/prov_data_ca.json', 'r') as json_content:
            data = json.load(json_content)
            countries = data['countries']
            for country in countries:
                provinces = country['provinces']
                country_iso_code = country['isoCode']
                for province in provinces:
                    prov_name = province['prov']
                    department_geoname_id = self.get_department_geoname_id(country_iso_code, prov_name)
                    if department_geoname_id:
                        mun_by_prov = self.get_municipalities(department_geoname_id)
                        province['municipalities'] = mun_by_prov
                    else:
                        print(f"{prov_name} does exit on {country['name']} not found, or does not have municipalities")
            return countries

    def combine_mun_and_prov(self):
        complete_data = self.get_central_america_data()
        os.makedirs('./Content', exist_ok=True)  # Create directory if it doesn't exist
        filepath = os.path.join('./Content/content_ca.json')  # Build full path
        with open(filepath, 'w', encoding='utf-8') as json_file:
            json.dump({"countries": complete_data}, json_file, ensure_ascii=False, indent=2)
