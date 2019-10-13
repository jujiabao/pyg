app.service("searchService", function ($http) {
    /**
     * 搜索方法
     * @param searchMap
     * @returns {*|void}
     */
    this.search = function (searchMap) {
        return $http.post("itemsearch/search.do", searchMap);
    }
})