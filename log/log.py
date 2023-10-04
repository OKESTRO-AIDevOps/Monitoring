import logging
import os
import sys

class StandardLog:
    def __init__(self):
        self.name_path = os.path.abspath(__file__)
        self.file_path = os.path.splitext(os.path.basename(sys._getframe(1).f_globals['__file__']))[0]
        self.project_path = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
        self.logger = logging.getLogger(self.name_path)
        self.logger.setLevel(logging.INFO)
        self.stream_handler = logging.StreamHandler()
        self.total_handler = logging.FileHandler(os.path.join(self.project_path, 'utils', 'logs', 'total.log'), mode='a')
        self.file_handler = logging.FileHandler(os.path.join(self.project_path, 'utils', 'logs', f'{self.file_path}.log'), mode='a')
        self.formatter = None
        self.file_name = None
        self.func_name = None

    def set_filename(self):
        self.file_name = sys._getframe(3).f_globals['__file__']
        self.func_name = sys._getframe(2).f_code.co_name

    def log_handle(self):
        self.stream_handler.setFormatter(self.formatter)
        self.total_handler.setFormatter(self.formatter)
        self.file_handler.setFormatter(self.formatter)
        if isinstance(self.logger, logging.LoggerAdapter):
            return
        self.formatter.converter = time.gmtime
        self.logger.addHandler(self.stream_handler)
        self.logger.addHandler(self.file_handler)
        self.logger.addHandler(self.total_handler)

    def sending_log(self, level, e=None):
        self.set_filename()
        self.logger = logging.getLogger(self.name_path)
        extra = {'context_message': self.file_name,
                 'context_message2': self.func_name}
        self.logger = logging.getLogger(self.name_path)

        if level == 'success':
            context = 'Succeeded!'
        elif level == 'warning':
            context = 'Warning!'
        elif level == 'error':
            context = 'Failed!'
        else:
            raise ValueError(f"Invalid log level: {level}")

        self.formatter = logging.Formatter(
            '[Time: %(asctime)s] '
            '[Level: %(levelname)s] '
            '[MainPipeline: %(context_message)s] '
            '[FunctionFile: %(pathname)s] '
            '[Fline: %(lineno)d] '
            f'[Context: The "{self.func_name}" is {context}] '
            f'[Reason: %(message)s] '
            f'[Error Type: {type(e)}]' if level == 'error' else f'[Context: {context}]'
        )

        self.log_handle()
        self.logger = logging.LoggerAdapter(self.logger, extra)
        return self.logger