
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