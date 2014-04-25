angular.module('starter.controllers', [])

.controller('AppCtrl', function($scope) {
})

.controller('BrowseCtrl', function($scope) {
  $scope.oauth = function() {
    // replace with your oewn oauth.io public key
    OAuth.initialize('nIz5pP6_YPsuE0CrnNJZdGTScgM');
    OAuth.popup('runkeeper', function(err, result) {
      //handle error with err
      //use result.access_token in your API request
      alert('asdf: ' + result.access_token);
    });
  }
})

.controller('PlaylistsCtrl', function($scope) {
  $scope.playlists = [
    { title: 'Reggae', id: 1 },
    { title: 'Chill', id: 2 },
    { title: 'Dubstep', id: 3 },
    { title: 'Indie', id: 4 },
    { title: 'Rap', id: 5 },
    { title: 'Cowbell', id: 6 }
  ];
})

.controller('PlaylistCtrl', function($scope, $stateParams) {
})
