

public class DynamicTextProcessor {

    public static String trendJson(){
        String json = """
                [
                    { "id":"11", "time":"2022-11-11 19:07", "posterName":"hnss", "message":"Trip in England.", "location":"[43,11]" },
                    { "id":"8" , "time":"2022-11-10 19:07", "posterName":"guomaimang", "message":"A rainy day...", "location":"[99,18]" },
                    { "id":"3" , "time":"2022-08-11 03:07", "posterName":"hongshu", "message":"This is my new Pet!" , "location":"[-43,11]"},
                ]
                """;
        return json;
    }

    public static String map(){
        return """
                <!DOCTYPE html>
                <html>
                <head>
                <meta charset="utf-8">
                <title>Add custom icons with Markers</title>
                <meta name="viewport" content="initial-scale=1,maximum-scale=1,user-scalable=no">
                <link href="https://api.mapbox.com/mapbox-gl-js/v2.10.0/mapbox-gl.css" rel="stylesheet">
                <script src="https://api.mapbox.com/mapbox-gl-js/v2.10.0/mapbox-gl.js"></script>
                <style>
                body { margin: 0; padding: 0; }
                #map { position: absolute; top: 0; bottom: 0; width: 100%; }
                </style>
                </head>
                <body>
                <style>
                .marker {
                display: block;
                border: none;
                border-radius: 50%;
                cursor: pointer;
                padding: 0;
                }
                </style>
                \s
                <div id="map"></div>
                \s
                <script>
                	mapboxgl.accessToken = 'pk.eyJ1IjoiaGFuamlhbWluZyIsImEiOiJjbDYzZ29hZWIwY2l5M29uam5taTc0MjJqIn0.tcjuacfyZfjsHR3B3aIQyA';
                const geojson = {
                'type': 'FeatureCollection',
                'features': [
                {
                'type': 'Feature',
                'properties': {
                'message': 'Foo',
                'iconSize': [60, 60]
                },
                'geometry': {
                'type': 'Point',
                'coordinates': [-66.324462, -16.024695]
                }
                },
                {
                'type': 'Feature',
                'properties': {
                'message': 'Bar',
                'iconSize': [50, 50]
                },
                'geometry': {
                'type': 'Point',
                'coordinates': [-61.21582, -15.971891]
                }
                },
                {
                'type': 'Feature',
                'properties': {
                'message': 'Baz',
                'iconSize': [40, 40]
                },
                'geometry': {
                'type': 'Point',
                'coordinates': [-63.292236, -18.281518]
                }
                }
                ]
                };
                \s
                const map = new mapboxgl.Map({
                container: 'map',
                // Choose from Mapbox's core styles, or make your own style with Mapbox Studio
                style: 'mapbox://styles/mapbox/streets-v11',
                center: [-65.017, -16.457],
                zoom: 5
                });
                \s
                // Add markers to the map.
                for (const marker of geojson.features) {
                // Create a DOM element for each marker.
                const el = document.createElement('div');
                const width = marker.properties.iconSize[0];
                const height = marker.properties.iconSize[1];
                el.className = 'marker';
                el.style.backgroundImage = `url(https://placekitten.com/g/${width}/${height}/)`;
                el.style.width = `${width}px`;
                el.style.height = `${height}px`;
                el.style.backgroundSize = '100%';
                \s
                el.addEventListener('click', () => {
                window.alert(marker.properties.message);
                });
                \s
                // Add markers to the map.
                new mapboxgl.Marker(el)
                .setLngLat(marker.geometry.coordinates)
                .addTo(map);
                }
                </script>
                \s
                </body>
                </html>
                """;
    }

}
