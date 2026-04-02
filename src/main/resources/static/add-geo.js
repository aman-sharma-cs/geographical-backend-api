console.log("Add Geo page loaded");

let lat = null;
let lon = null;

const user = JSON.parse(localStorage.getItem("loggedInUser"));

if(!user){
    window.location.href="login.html";
}


// ARCGIS MAP

require([
"esri/Map",
"esri/views/MapView",
"esri/Graphic"
], function(Map, MapView, Graphic){

const map = new Map({
basemap:"streets-navigation-vector"
});

const view = new MapView({
container:"viewDiv",
map:map,
center:[78.9629,20.5937],
zoom:5
});


// USER CLICK LOCATION

view.on("click", function(event){

lat = event.mapPoint.latitude;
lon = event.mapPoint.longitude;

console.log("Latitude:", lat);
console.log("Longitude:", lon);


// remove old marker

view.graphics.removeAll();

const point = {
type:"point",
longitude:lon,
latitude:lat
};

const marker = {
type:"simple-marker",
color:"blue",
size:"10px"
};

const graphic = new Graphic({
geometry:point,
symbol:marker
});

view.graphics.add(graphic);

});

});


// SAVE DATA FUNCTION

function saveData(){

if(!lat || !lon){

alert("Please select a location on the map");
return;

}

const description = document.getElementById("description").value;
const remarks = document.getElementById("remarks").value;

fetch("http://localhost:8080/api/geo/add",{

method:"POST",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify({

username:user.userName,
latitude:lat,
longitude:lon,
description:description,
remarks:remarks

})

})

.then(res=>res.json())

.then(data=>{

alert("Location added successfully");

window.location.href="user-dashboard.html";

})

.catch(err=>{

console.error("Error saving geo data:",err);

});

}