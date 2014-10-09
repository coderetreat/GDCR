$(function() {
  var translateToMapCoords = function(coords) {
    return ol.proj.transform(coords, 'EPSG:4326', 'EPSG:3857')
  }

  var toPoint = function(loc) {
    var coords = translateToMapCoords([loc.coords[1], loc.coords[0]]);
    return new ol.geom.Point(coords);
  };

  var styleCache = {};
  var countriesLayer = new ol.layer.Vector({
    source: new ol.source.GeoJSON({
      projection: 'EPSG:3857',
      url: 'geodata/countries.geojson'
    }),
    style: function(feature, resolution) {
      var text = resolution < 5000 ? feature.get('name') : '';
      if (!styleCache[text]) {
        styleCache[text] = [new ol.style.Style({
          fill: new ol.style.Fill({
            color: '#3C3C3C'
          }),
          stroke: new ol.style.Stroke({
            color: '#3F3F3F',
            width: 1
          })
        })];
      }
      return styleCache[text];
    }
  });

  var eventIconStyle = new ol.style.Style({
    image: new ol.style.Icon({
      scale: 0.05,
      anchor: [0.5, 1],
      src: 'images/map-pin.png'
    })
  });

  var events = new ol.layer.Vector({
    source: new ol.source.Vector(),
    style: eventIconStyle
  });


  $.get('data/locations.json', function(locations) {
    $.each(locations, function() {
      events.getSource().addFeature(
        new ol.Feature({
          geometry: toPoint(this),
          name: this.city
        })
      );
    })

    var map = new ol.Map({
      target: 'map',
      layers: [countriesLayer, events],
      view: new ol.View({
        center: translateToMapCoords([0, 40]),
        zoom: 1.2
      })
    });
  })
});
