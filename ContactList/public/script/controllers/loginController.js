app.controller('loginController',['$http','$log','$scope',function($http,$log,$scope){
    var self =this;
    
    self.validate=function(){
        console.log($scope.emailId);
        console.log($scope.password);
    };
}]);