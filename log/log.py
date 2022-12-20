import logging
import time
import os
import sys



class standardLog:
    """
    This standardLog class is base of making logs
    """
    def __init__(self):
        """
        This is logs init function

        Arguments:
            name -- operate function`s file path
            file_name -- operate function`s file name
        """
        self.name_path = os.path.abspath(__file__)
        self.file_path = sys._getframe(1).f_globals['__file__'].split('/')[-1].split('.')[0]
        self.project_path = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
        self.logger = logging.getLogger(self.name_path)
        # self.name = name
        # self.file_name = file_name
        self.logger.setLevel(logging.INFO)
        self.streamHandler = logging.StreamHandler()
        self.totalHandler = logging.FileHandler(self.project_path + '/utils/logs/total.log', mode='a')
        self.fileHandler = logging.FileHandler(self.project_path + '/utils/logs/' + self.file_path + '.log', mode='a')
        self.formatter = None
        self.file_name = None
        self.func_anme = None

    def set_filename(self):
        self.file_name = sys._getframe(3).f_globals['__file__']
        self.func_name = sys._getframe(2).f_code.co_name

    def log_handle(self):
        """
        This is logs common part
        """
        self.streamHandler.setFormatter(self.formatter)
        self.totalHandler.setFormatter(self.formatter)
        self.fileHandler.setFormatter(self.formatter)
        if type(self.logger) is logging.LoggerAdapter:
            return
        self.formatter.converter = time.gmtime
        self.logger.addHandler(self.streamHandler)
        self.logger.addHandler(self.fileHandler)
        self.logger.addHandler(self.totalHandler)