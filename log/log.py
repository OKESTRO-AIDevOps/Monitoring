import logging
import time
import os
import sys



class standardLog:
    def __init__(self):
        self.name_path = os.path.abspath(__file__)
        self.file_path = sys._getframe(1).f_globals['__file__'].split('/')[-1].split('.')[0]
        self.project_path = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
        self.logger = logging.getLogger(self.name_path)
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
        self.streamHandler.setFormatter(self.formatter)
        self.totalHandler.setFormatter(self.formatter)
        self.fileHandler.setFormatter(self.formatter)
        if type(self.logger) is logging.LoggerAdapter:
            return
        self.formatter.converter = time.gmtime
        self.logger.addHandler(self.streamHandler)
        self.logger.addHandler(self.fileHandler)
        self.logger.addHandler(self.totalHandler)

    def sending_log(self, level, e=None):
        self.set_filename()
        self.logger = logging.getLogger(self.name_path)
        extra = {'context_message': self.file_name,
                 'context_message2': self.func_name}
        self.logger = logging.getLogger(self.name_path)

        if level == 'success':
            context = 'Successed !'
            self.formatter = logging.Formatter(
                '[Time: %(asctime)s] '
                '[Level: %(levelname)s] '
                '[MainPipeline: %(context_message)s] '
                '[FunctionFile: %(pathname)s] '
                '[Fline: %(lineno)d] '
                f'[Context: The "%(context_message2)s" is {context}] '
                f'[Reason: %(message)s]')

        elif level == 'warning':
            context = 'Warning !!'
            self.formatter = logging.Formatter(
                '[Time: %(asctime)s] '
                '[Level: %(levelname)s] '
                '[MainPipeline: %(context_message)s] '
                '[FunctionFile: %(pathname)s] '
                '[Fline: %(lineno)d] '
                f'[Context: The "%(context_message2)s" is {context}] '
                f'[Reason: %(message)s]')

        elif level == 'error':
            context = 'Failed !!!'
            self.formatter = logging.Formatter(
                '[Time: %(asctime)s] '
                '[Level: %(levelname)s] '
                '[MainPipeline: %(context_message)s] '
                '[FunctionFile: %(pathname)s] '
                '[Fline: %(lineno)d] '
                f'[Context: The "%(context_message2)s" is {context}] '
                f'[Reason: %(message)s] '
                f'[Error Type: {type(e)}]')

        self.log_handle()
        self.logger = logging.LoggerAdapter(self.logger, extra)
        return self.logger
