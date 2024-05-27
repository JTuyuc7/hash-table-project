import os


class Utilities:

    @staticmethod
    def has_already_content(directory, file_name):
        full_path = os.path.join(directory, file_name)
        return os.path.isfile(full_path) and full_path.endswith('.json')
