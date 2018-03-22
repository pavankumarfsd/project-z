(function() {
	
	var app = angular.module("app", ['ngRoute']);

	app.config(function($routeProvider) {	    
		$routeProvider
		        .when('/actor', {
		            templateUrl: 'actor.html',
		            controller: 'HttpCtrl'
		        }).when('/admin', {
		            templateUrl: 'admin.html',
		            controller: 'AdminCtrl'
		        })
		        .otherwise({
		            redirectTo: '/home'
		        });
		});
	app.controller("MainCtrl", function($rootScope,$scope, $http,$location) {
		$rootScope.loginPage = true;
		$rootScope.adminPage = false;
		$rootScope.passwordScreen = false;
		$scope.enterPage = function(path){
			$scope.loginObj = angular.toJson($scope.login, false);
			//console.log($scope.loginObj);
			var response = $http.post('/RestfulWebServiceExample/rest/actors/login',$scope.loginObj);
			response.success(function(data) {
				if(data == "Authorized"){
				    $location.path( path );
				}
				else{
					$scope.unauthorization = true;
				}
			})
			response.error(function(data, status, headers, config) {
				alert("AJAX failed to get data, status=" + status);
			})
		};
		
		$scope.leavePage = function(path){
			$rootScope.loginPage = true;
			$location.path( path );
			$scope.login = {userId:'',password:''};
			$scope.unauthorization = false;
		};
		
		$scope.goToAdmin = function(){
			$rootScope.passwordScreen = false;
			$scope.login = {userId:'',password:''};
			$scope.unauthorization = false;
			$location.path( '/admin' );
		}
		
		$scope.forgotPassword = function(){
			$scope.unauthorization = false;
			$rootScope.passwordScreen = true;
			$location.path( '/admin' );
		};
	});
	
	app.controller("AdminCtrl", function($rootScope,$scope, $http,$location) {
		$rootScope.loginPage = false;
		$rootScope.adminPage = true;
		$scope.createAdmin = false;
		if($rootScope.passwordScreen){
			$scope.createAdmin = false;
		    $scope.showAdmins = false;
			$scope.acessManagement = false;
			$scope.manage = false;
			$scope.resetScreen=false;
			
		}else{
			$scope.createAdmin = true;
		}
		
		$scope.navTitle = 'Admin Screen';
		
		$scope.clearAdmin = function(){
			$scope.admin = {};
		};
		$scope.addAdmin = function(){
			$scope.adminObj = angular.toJson($scope.admin, false);
			var elligiblity = 0;
			$scope.adminObj.elligiblity = elligiblity;
			var response = $http.post('/RestfulWebServiceExample/rest/actors/addAdmin',$scope.adminObj);
			response.success(function(data) {
				$scope.clearAdmin();
				
			})
			response.error(function(data, status, headers, config) {
				alert("AJAX failed to post data, status=" + status);
			})
		};
	
		$scope.fetchAdmins = function(){
			var response = $http.get('/RestfulWebServiceExample/rest/actors/fetchAdmins');
			response.success(function(data) {
				$scope.admins = data;
				$scope.showAdmins = true;
				//alert(JSON.stringify($scope.admins[0]));
				
			})
			response.error(function(data, status, headers, config) {
				alert("AJAX failed to get data, status=" + status);
			})
		};
		
		$scope.deleteAdmin = function(userId){
			var response = $http.delete('/RestfulWebServiceExample/rest/actors/deleteAdmin/' + userId);
			response.success(function(data, status) {
				console.log("Inside delete operation..." + angular.toJson(data, false) + ", status=" + status);
				$scope.deleteMsg = "You have successfuly deleted admin - "+userId;
				$scope.deleteSuccessMsg = true;
				$scope.fetchAdmins();
			});
				
			response.error(function(data, status) {
				alert("AJAX failed to get data, status=" + status);
			})	
		};
		
		$scope.grantAccess = function(userId){
            var response = $http.get('/RestfulWebServiceExample/rest/actors/access/'+userId  );
			
			response.success(function(data) {
				console.log("getActor data: " + angular.toJson(data, false));
				//$scope.admin = data;
				$scope.fetchAdmins();
		    })
			
			response.error(function(data, status, headers, config) {
				alert("AJAX failed to get data, status=" + status);
			})
		};
		
		$scope.revokeAccess = function(userId){
           var response = $http.get('/RestfulWebServiceExample/rest/actors/revoke/'+userId  );
			
			response.success(function(data) {
				console.log("getActor data: " + angular.toJson(data, false));
				//$scope.admin = data;
				$scope.fetchAdmins();
		    })
			
			response.error(function(data, status, headers, config) {
				alert("AJAX failed to get data, status=" + status);
			})
		};
		
		$scope.sendToken = function(userId){
            var response = $http.get('/RestfulWebServiceExample/rest/actors/sendToken/'+userId  );
			
			response.success(function(data) {
				console.log("getActor data: " + angular.toJson(data, false));
				
		    })
			
			response.error(function(data, status, headers, config) {
				alert("AJAX failed to get data, status=" + status);
			})	
		};
		
		$scope.verify = function(userId,token){
			console.log("verify function");
            var response = $http.get('/RestfulWebServiceExample/rest/actors/verify/'+userId +"/"+token  );
			
			response.success(function(data) {
				//console.log("getActor data: " + angular.toJson(data, false));
				if(data == 'true'){
					console.log("inside if loop");
					$scope.createAdmin = false;
				    $scope.showAdmins = false;
					$scope.acessManagement = false;
					$scope.manage = false;
					$rootScope.passwordScreen = false;
					$scope.wrongTokenError = false;
					$scope.resetScreen = true;
				}
				else{
					$scope.wrongTokenError = true;
				}
				
		    })
			
			response.error(function(data, status, headers, config) {
				alert("AJAX failed to get data, status=" + status);
			})	
		};
		
		$scope.resetPassword = function(userId,password){
			var response = $http.get('/RestfulWebServiceExample/rest/actors/reset/'+userId +"/"+password  );
			
			response.success(function(data) {
				//console.log("getActor data: " + angular.toJson(data, false));
				if(data == 'true'){
					$scope.updatePasswordSuccess = true;
				}
				else{
				}
				
		    })
			
			response.error(function(data, status, headers, config) {
				alert("AJAX failed to get data, status=" + status);
			})	
		};
		
		$scope.goToMain = function(){
			$rootScope.loginPage = true;
			$rootScope.adminPage = false;
			$location.path( "/home" );
			$scope.login = {userId:'',password:''};
			$scope.unauthorization = false;
		};
		
		$("#grant").click(function(){
			$(this).button('toggle');
		   $(this).button('Giving...');
	    });
		
		$("#revoke").click(function(){
			$(this).button('toggle');
		   $(this).button('Revoking...');
		});
		
	});
	
	app.controller("HttpCtrl", function($rootScope,$scope, $http) {
		$rootScope.loginPage = false;
		$rootScope.adminPage = false;
		$scope.navTitle = 'All Stars';
		$scope.operation="";
		$scope.isSaveDisabled = true;
		$scope.isDeleteDisabled = true;
		
		var response = $http.get('/RestfulWebServiceExample/rest/actors/');
		response.success(function(data) {
			$scope.actors = data;
			console.log("[main] # of items: " + data.length)
			angular.forEach(data, function(element) {
				console.log("[main] actor: " + element.name);
			});
		})
		response.error(function(data, status, headers, config) {
			alert("AJAX failed to get data, status=" + status);
		})
		
		
		$scope.getActor = function(id) {
			var response = $http.get('/RestfulWebServiceExample/rest/actors/getActor/'+ id );
			
			response.success(function(data) {
				console.log("getActor data: " + angular.toJson(data, false));
				$scope.actor = data;
				$scope.operation="update";
				$scope.isSaveDisabled = false;
				$scope.isDeleteDisabled = false;
		    })
			
			response.error(function(data, status, headers, config) {
				alert("AJAX failed to get data, status=" + status);
			})
		};
		
		$scope.searchActor = function(name) {
			$scope.navTitle = 'Search Criteria';
			
			var response = $http.get('/RestfulWebServiceExample/rest/actors/search/' + name);
			response.success(function(data) {
				$scope.actors = data;
				$scope.$apply();

				console.log("[searchActor] # of items: " + data.length)
				angular.forEach(data, function(element) {
					console.log("[searchActor] actor: " + element.name);
				});

		    });
			
			response.error(function(data, status, headers, config) {
				alert("AJAX failed to get data, status=" + status);
			})
		};
		
		$scope.clearForm = function() {
			$scope.actor = {
				id:'',
				name:'',
				birthName:'',
				birthDate:'',
				email:'',
				active:''
			};
		}
		
		$scope.addNew = function(element) {
			$scope.operation="create";
			$scope.clearForm();
			main.id.focus();
			$scope.isSaveDisabled = false;
			$scope.isDeleteDisabled = true;
		}
		
		$scope.saveActor = function(id) {
			$scope.jsonObj = angular.toJson($scope.actor, false);
			console.log("[update] data:------> " + $scope.jsonObj);

			if ($scope.operation == "update") {
				var response = $http.put('/RestfulWebServiceExample/rest/actors/' + id, $scope.jsonObj);
				response.success(function(data, status) {
					console.log("Inside update operation..." + angular.toJson(data, false) + ", status=" + status);
					$scope.resetSearch();
			    });
				
				response.error(function(data, status) {
					alert("AJAX failed to get data, status=" + status);
				})
			} else if ($scope.operation == "create") {
				console.log($scope.jsonObj);
				var response = $http.post('/RestfulWebServiceExample/rest/actors/add', $scope.jsonObj);
				response.success(function(data, status) {
					console.log("Inside create operation..." + angular.toJson(data, false) + ", status=" + status);
					$scope.resetSearch();
			    });
				
				response.error(function(data, status) {
					alert("AJAX failed to get data, status=" + status, scope);
				})	
			}
		};
		
		$scope.deleteActor = function(id) {
			var response = $http.delete('/RestfulWebServiceExample/rest/actors/' + id);
			response.success(function(data, status) {
				console.log("Inside delete operation..." + angular.toJson(data, false) + ", status=" + status);
				$scope.resetSearch();
			});
				
			response.error(function(data, status) {
				alert("AJAX failed to get data, status=" + status);
			})
		};
		
		$scope.resetSearch = function(name) {
			//var app = this;
			$scope.operation="";
			$scope.clearForm();
			$scope.isSaveDisabled = true;
			$scope.isDeleteDisabled = true;
			$scope.navTitle = 'All Stars';
			$scope.searchName = '';
			
			var response = $http.get('/RestfulWebServiceExample/rest/actors/');
			response.success(function(data) {
				$scope.actors = data;
				//$scope.$apply();
				console.log("[resetSearch] # of items: " + data.length)
		    });
			
			response.error(function(data, status, headers, config) {
				alert("AJAX failed to get data, status=" + status);
			})
		};
	});	
})();