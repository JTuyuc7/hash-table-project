from Countries import Countries
from Provinces import Provinces
from Utils import Utils
from CentralAmericaData import CentralAmericaData
from GenerateSQLScript import GenerateScript


class MainApp:
    def __init__(self):
        self.utils = Utils.Utilities

    def get_all_world_data(self):

        # if not self.utils.has_already_content('./Content', 'countriesIso.json'):
        #     print('Generating ISO countries info')
        #     countries = Countries.GetCountries()
        #     countries.generate_codes()
        #     print('Generated successfully')

        if not self.utils.has_already_content('./Content', 'content.json'):
            print('Creating provinces data')
            refactored_data = Provinces.Provinces('countriesIso', 'content')
            refactored_data.combine_countries_and_prov()
            print('Completed...')
        else:
            print("Should generate the sql file")

    def get_central_america_data(self):
        # if self.utils.has_already_content('./Content', 'countriesIso_ca.json'):
        #     print("get all central america data")
        #     ca_data = Provinces.Provinces('countriesIso_ca', 'prov_data_ca')
        #     ca_data.combine_countries_and_prov()
        #     print("data generated")
        # else:
        #     print("No CA data found, please check it")

        if not self.utils.has_already_content('./Content', 'prov_data_ca.json'):
            print("Unable to continue, please check the content")
        else:
            print("Creating CA data...")
            central_america = CentralAmericaData.CentralAmericaData()
            central_america.combine_mun_and_prov()
            print("CA data created...")

    def generate_sql_script(self):
        print("generating sql script ....")
        generate_script = GenerateScript.GenerateScript()
        generate_script.new_script_imp()
        print("data genrated")


main_app = MainApp()
## ! Get all the ISO content from all the world
# main_app.get_all_world_data()

## Get the data only form CA and Mexico
# main_app.get_central_america_data()

## Generate the sql file
main_app.generate_sql_script()