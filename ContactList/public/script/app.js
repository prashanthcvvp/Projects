var app = angular.module('ContactApp',['ngRoute']);


app.config(['$routeProvider',function($routeProvider){
    $routeProvider
        .when('/',{
            templateUrl:'partials/login.html',
            controller:'loginController as login'
        })
        .when('/contacts',{
            templateUrl:'partials/contacts.html',
            controller:'contactsController as contactsCtrl'
        })
        .when('/register',{
            templateUrl:'partials/register.html',
            controller:'registerController as register'
        })
        .otherwise({
            redirectTo:'/'
        });
}]);