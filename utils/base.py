import os
import sys
import logging
from configparser import ConfigParser, NoSectionError

from elasticsearch import Elasticsearch

project_path = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
logs_path =  project_path + "/utils/logs"
log_file = logs_path + "/es.log"

if not os.path.exists(logs_path):
    os.mkdir(logs_path)
    f = open(log_file, 'a')
    f.write('')
logging.basicConfig(filename=log_file, level=logging.INFO)

sys.path.append(project_path)

class ESBase:
    def __init__(self):
        self.project_path = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
        self.config_path = self.project_path + '/config.ini'
        self.config = self.read_config(self.config_path)


    def read_config(self, config_path):
        config = ConfigParser()
        config.read(config_path)
        return config


    def connect(self, config):
        try:
            es_host = config.get('ES','HOST')
            es_port = config.get('ES','PORT')
            es_id = config.get('ES','USER')
            es_pw = config.get('ES','PASSWORD')

            es = Elasticsearch(hosts=f"http://{es_id}:{es_pw}@{es_host}:{es_port}/", timeout=1000)
            
            if not es.ping():
                logging.error('ES Connetion Error')
                raise(Exception(f"ES Connetion Error"))
            
            return es

        except NoSectionError:
            logging.error(f'Fail to find ES config')


