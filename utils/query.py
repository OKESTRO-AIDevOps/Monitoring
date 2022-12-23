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


    def add_aggregation(self,
                        main: str=None,
                        main_field: str=None,
                        main_agg_size: int=None,
                        sub: dict=None,
                        sub_agg_size: int=None,
                        interval: str="5m",
                        percents: list=None
                        ) -> None:
        self.body["aggs"] = {}

        self.body["aggs"]["main_aggs"] = {main: {"field": main_field}}

        if main == "date_histogram":
            self.body["aggs"]["main_aggs"][main]["fixed_interval"] = interval
        
        if main_agg_size:
            self.body["aggs"]["main_aggs"][main]["size"] = main_agg_size

        if sub:
            tmp = {}
            for agg, _type in sub.items():
                tmp[agg+'_'+_type] = {sub[agg]:{"field":agg}}

                if sub_agg_size:
                    tmp[agg+'_'+_type][sub[agg]]["size"] = sub_agg_size
                
                if sub[agg] == 'percentiles':
                    tmp[agg+'_'+_type][sub[agg]]["percents"] = percents

            self.body["aggs"]["main_aggs"]["aggs"] = tmp
    

    def add_size(self,
                size: int
                ) -> None:
        self.body["size"] = size


    def add_option(self,
                option: dict
                ) -> None:
        self.body.update(option)


    def add_source(self,
                source: list
                ) -> None:
        self.body = dict()
        self.body["_source"] = source
        self.body["query"] = dict()


    def sort(self,
            field: str,
            by: str
            ) -> None:
        if not self.body.get("sort"):
            self.body["sort"] = [{}]
        self.body["sort"][0][field] = {"order" : by}


    def remove_bool(self,
                    type: str=None
                    ) -> None:
        try:
            if type:
                del self.body["query"]["bool"][type]
            else:
                del self.body["query"]["bool"]
        except KeyError:
            pass


    def remove_aggregation(self) -> None:
        try:
            del self.body["aggs"]
        except KeyError:
            pass