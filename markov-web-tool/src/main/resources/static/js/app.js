var app = angular.module("markovTool", []);

app.directive('fileModel', [ '$parse', function($parse) {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			var model = $parse(attrs.fileModel);
			var modelSetter = model.assign;

			element.bind('change', function() {
				scope.$apply(function() {
					modelSetter(scope, element[0].files[0]);
				});
			});
		}
	};
} ]);

app.service('MarkovChainService', [ '$http', '$rootScope', function($http, $rootScope) {
	this.transform = function(file, prefixLen, maxTokens, tokenStrategy) {
		var fd = new FormData();
		fd.append('file', file);
		if (prefixLen != null) {
			fd.append('prefixLen', prefixLen);
		}
		if (maxTokens != null) {
			fd.append('maxTokens', maxTokens);
		}
		fd.append('tokenStrategy', tokenStrategy)
		$http.post("/upload", fd, {
			transformRequest : angular.identity,
			headers : {
				'Content-Type' : undefined
			}
		}).then(function(response) {
			$rootScope.result = response.data;
			$rootScope.errMsg = undefined;
		},function(response) {
			$rootScope.result = undefined;
			$rootScope.errMsg = (response.data?response.data.message:"Service seems to be down!");
		});
	}
} ]);

app.service('ParametersService', [ '$http', '$q', function($http, $q) {
	var that = this;
	this.get = function() {
		if (!that.cachedParams) {
			return $http.get("/parameters").then(function(response) {
				that.cachedParams = response.data;
				return response.data;
			});
		}
		return $q.resolve(that.cachedParams);
	}
} ]);

app.controller('MarkovToolCtrl', [
		'$scope',
		'$rootScope',
		'MarkovChainService',
		'ParametersService',
		function($scope, $rootScope, MarkovChainService, ParametersService) {
			ParametersService.get().then(function(response) {
				$scope.prefixLen = response.defaultPrefixLen;
				$scope.maxTokens = response.defaultMaxTokens;
				$scope.tokenStrategies = response.tokenStrategies;
				$scope.tokenStrategy = 0;
			});

			$scope.uploadFile = function() {
				MarkovChainService.transform($scope.sourceFile, $scope.prefixLen,
						$scope.maxTokens, $scope.tokenStrategy);
			};
		} ]);
