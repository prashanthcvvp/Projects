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
app.controller('loginController',['$http','$log','$scope',function($http,$log,$scope){
    var self =this;
    
    self.validate=function(){
        console.log($scope.emailId);
        console.log($scope.password);
    };
}]);
app.service('ContactsList',['$http','$log','filterFilter',function($http,$log,filterFilter){
    var contactList=[];
    var self = this;
    var refresh=function(){
        $http.get('/contacts')
            .success(function(response){
                contactList=response;
            })
            .error(function(){
                $log.info("Error");
        });
    };
    refresh();
    self.addContact=function(contact){
        
        $http.post('/contacts',contact)
        .success(function(res){
            contactList.push(res);
        }).error(function(){
            $log.error("Error!!");
        });
        //contactList.push({name:name,email:email,phone:phone});
    };
    
    self.getContacts=function(input){
        var array = filterFilter(contactList, input); 
        $log.info("Array = "+ array);
        return array;
    };
    
    self.removeContact=function(id){
        $http.delete('/contacts/'+id).success(function(res){
            refresh();
        }).error(function(){
            $log.error('Error!!!');
        });
    };
    
    self.updateContact=function(contact){
        
        $http.put('/contacts/'+contact._id,contact).success(function(res){
            refresh();
        }).error(function(){
            $log.error("Error !!");
        });
    }
    
}]);

app.controller('contactsController',['ContactsList','$scope','$log','$http',function(ContactsList,$scope,$log,$http){
    var self =this;
    self.contact;
    self.addContact=function(contact){
        ContactsList.addContact(contact);
        $scope.contact='';
    };
    
    self.getContacts=function(val){
        return ContactsList.getContacts(val);
    }
    
    self.removeContact=function(id){
        ContactsList.removeContact(id);
    };
    
    self.editContact=function(index){
        contacts=ContactsList.getContacts();
        $scope.contact=contacts[index];
        
    };
    
    self.updateContact=function(){
        //console.log(id);
        ContactsList.updateContact($scope.contact);
        $scope.contact='';
    };
    
}]);
app.controller('registerController',function(){
    
});