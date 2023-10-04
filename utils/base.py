import os
import sys
import logging
from configparser import ConfigParser, NoSectionError
from elasticsearch import Elasticsearch


class ESBase:
    def __init__(self):
        self.project_path = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
        self.config_path = os.path.join(self.project_path, 'config.ini')
        self.config = self.read_config(self.config_path)

    def read_config(self, config_path):
        config = ConfigParser()
        config.read(config_path)
        return config

    def connect(self, config):
        try:
            es_host = config.get('ES', 'HOST')
            es_port = config.getint('ES', 'PORT')  # Assume PORT is an integer
            es_id = config.get('ES', 'USER')
            es_pw = config.get('ES', 'PASSWORD')

            es = Elasticsearch(hosts=f"http://{es_id}:{es_pw}@{es_host}:{es_port}/", timeout=1000)

            if not es.ping():
                logging.error('ES Connection Error')
                raise Exception("ES Connection Error")

            return es

        except NoSectionError:
            logging.error('Failed to find ES config')


if __name__ == "__main__":
    project_path = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
    logs_path = os.path.join(project_path, "utils", "logs")
    log_file = os.path.join(logs_path, "es.log")

    if not os.path.exists(logs_path):
        os.makedirs(logs_path)
        with open(log_file, 'a'):
            pass

    logging.basicConfig(filename=log_file, level=logging.INFO)

    es_base = ESBase()
    es_config = es_base.config
    es_connection = es_base.connect(es_config)