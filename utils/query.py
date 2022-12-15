class Query:
    def __init__(self):
        self.body = {"query": {}}

    
    def add_bool(self, 
                type: str,
                datetype: str =None,
                gte: str =None,
                lte: str =None,
                term: str =None,
                keyword: str =None # list로 넣어야하는지 확인하기
                ) -> None:

        tmp = []

        if datetype:
            date_range = {}
            date_range["range"] = self.add_range(datetype, gte, lte, add_bool=True)
            tmp.append(date_range)
        if term:
            term_range = {"term": {term : keyword}}
            tmp.append(term_range)

        if not self.body["query"].get("bool"):
            self.body["query"]["bool"] = {}
        if not self.body["query"]["bool"].get(type):
            self.body["query"]["bool"][type] = tmp
        else:
            self.body["query"]["bool"][type] += tmp

        
    def add_range(self,
                datetype: str,
                gte: str=None,
                lte: str=None,
                add_bool: bool=False,
                ) -> None:
        date_range = {datetype : {}}
        if gte:
            date_range[datetype]["gte"] = gte
        if lte:
            date_range[datetype]["lte"] = lte
        if add_bool:
            return date_range
        
        self.body["query"]["range"] = date_range