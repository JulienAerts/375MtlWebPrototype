var baseURL = new URL(window.location.origin)

var montreal =  L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
})
var map = L.map('mapid', {
    center: [45.50894093,  -73.56863737],
    zoom: 10,
    layers: [montreal]
});
var markers = new L.FeatureGroup();
markers.addTo(map);

var renderActivite = function (activite) {
  var marker = L.marker([activite.lieu.lng, activite.lieu.lat]).addTo(markers)
  
  marker.bindPopup("<b>"+activite.nom+"</b><br>"+activite.description+"<br>"+activite.dates+"<br>"+activite.lieu.nom+"<br>");
  var id = activite.id
  var htmlTag = '<li> <button class="Activite" id="'+id+'"'+' lat ="'+activite.lieu.lat+'"'+' lng="'+activite.lieu.lng+'"'+' class="btn btn-primary">Voir sur la carte</button>'+ activite.nom +'</li></br>'
  return htmlTag
}


var fitOnMarker = function (lat ,lng){
    map.fitBounds([
    [40.712, -74.227],
    [40.774, -74.125]
]);
    
}
var renderListeActivites = function (activites) {
  return '<ul>'+ activites.map(renderActivite).join('') +'</ul>'
}

var renderErreur = function (erreur) {
  return '<li>'+'<ul>'+ erreur.code +' &mdash;'+ erreur.message+'</ul>' +'</li>'
}

var afficherErreur = function (erreur) {
  document.getElementById('erreur').innerHTML = erreur
}

var resetData = function () {
  var reset = ""
  map.removeLayer(markers);
  markers = new L.FeatureGroup();
  markers.addTo(map);
  document.getElementById('erreur').innerHTML = reset
  document.getElementById('liste-activites').innerHTML = reset
}

var installerListeActivites = function (listeActivitesHtml) {
  document.getElementById('liste-activites').innerHTML = listeActivitesHtml
}

var fetchActivites = function (url) {
  resetData()
  fetch(url).then(function(resp) {
       if (resp.status === 404 || resp.status === 400 ) {
    resp.json().then(function(data) {
      afficherErreur(renderErreur(data))
    })
  } else if (resp.status === 200) {
    resp.json().then(function(data) {
      installerListeActivites(renderListeActivites(data))
      map.fitBounds(markers.getBounds());
    })
  }
}) 
}

var rechercher = function (termDu ,termAu) {
  var url = new URL('/activites-375e', baseURL)
  url.searchParams.append('rayon', 5000)
  url.searchParams.append('du', termDu)
  url.searchParams.append('au', termAu)
  url.searchParams.append('lat',  map.getCenter().lat )
  url.searchParams.append('lng', map.getCenter().lng)
  fetchActivites(url)
}

var lierFormulaire = function () {
  var form = document.getElementById('search-form')
  var inputDateDu = document.getElementById('search-du')
  var inputDateAu = document.getElementById('search-au')
  form.addEventListener('submit', function (e) {
    e.preventDefault()
    rechercher(inputDateDu.value,inputDateAu.value)
  })
}

document.addEventListener('DOMContentLoaded', function () {
  fetchActivites(new URL('/activites-375e/activites', baseURL))
  lierFormulaire()
})

document.addEventListener("mouseover", myFunction);

function myFunction() {

    $(".Activite").click(function (event) {
     map.fitBounds([
    [event.target.getAttribute("lng"), event.target.getAttribute("lat")],
    [event.target.getAttribute("lng"), event.target.getAttribute("lat")]
    ]);
    
});

}
