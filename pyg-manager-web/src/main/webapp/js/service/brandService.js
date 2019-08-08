//品牌service
app.service("brandService", function ($http) {
    this.findAll = function () {
        return $http.get("../brand/findAll");
    };

    this.findPage = function (page, rows) {
        return $http.get('../brand/findPage?page=' + page + '&rows=' + rows);
    };

    this.findOne = function (id) {
        return $http.get("../brand/findOne?id="+id);
    };

    this.addOne = function (entity) {
        return $http.post("../brand/add", entity);
    };

    this.updateOne = function (entity) {
        return $http.post("../brand/update", entity);
    }

    this.dele = function (ids) {
        return $http.get("../brand/delete?ids="+ids)
    }

    this.search = function(page, rows, searchEntity) {
        return $http.post("../brand/search?page="+page+"&rows="+rows, searchEntity)
    }

});