(function() {
	var backend_app = angular.module('data_search', []);

	backend_app.controller('getData', function($scope,$http) {		
		this.name="";	
		$scope.show_btn=true;		
		this.bar_code_search=function() {
			
			$http({
				url:"http://10.213.162.99:8080/Search-Angular/publisher",
				method:"GET",
				params:{name:this.name}
			}).success(function(response){
				$scope.resp=response; 
				$scope.ifData=response.product.ifData;
				$scope.show_btn=response.product.ifData;
				
			});
		}
		
		this.add_bar_code=function() {
			$http({
				url:"http://10.213.162.99:8080/Search-Angular/publisher",
				method:"POST",
				params:{details:$scope.resp,"html":"true"}
			}).success(function(response){
				console.log(response);
				alert(response);
			});
		}
		this.ifDataCheck=function(key1){
			return key1.indexOf('ifData')==-1;
		}
	});
	
	
})();