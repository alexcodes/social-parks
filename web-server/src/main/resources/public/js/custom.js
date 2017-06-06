$(document).ready(function () {
    resizeMap();
});

function resizeMap() {
    var header = $("#header").height();
    var footer = $("#footer").height();
    var height = window.innerHeight - header - footer;
    console.log("window: " + window.innerHeight);
    console.log("result: " + height);
    $("#YMapsID").height(height);
}

var buttonSelected;

var leisure = [],
    culture = [],
    catering = [];

$.get("/map/objects", function (response) {
    leisure = response.leisure;
    culture = response.culture;
    catering = response.catering;
});

$(function () {

    ymaps.ready(function () {
        $.get("/map/coordinates", onSucceed).fail(onFail);
    });

    function onSucceed(response) {
        initMap(response.coordinates);
    }

    function onFail() {
        initMap([]);
    }

    function initMap(coordinates) {
        var data = coordinates;
        var map = new ymaps.Map('YMapsID', {
                center: [55.751588, 37.617861],
                controls: ['zoomControl', 'typeSelector', 'fullscreenControl'],
                zoom: 11, type: 'yandex#map'
            }),

            buttons = {
                cateringObjects: new ymaps.control.Button({
                    data: {
                        content: 'Общественное питание'
                    },
                    options: {
                        selectOnClick: true,
                        maxWidth: 150
                    }
                }),
                cultureObjects: new ymaps.control.Button({
                    data: {
                        content: 'Культура'
                    },
                    options: {
                        selectOnClick: true,
                        maxWidth: 150
                    }
                }),
                leisureObjects: new ymaps.control.Button({
                    data: {
                        content: 'Досуг'
                    },
                    options: {
                        selectOnClick: true,
                        maxWidth: 150
                    }
                })
            },

            gradients = [{
                0.1: 'rgba(128, 255, 0, 0.7)',
                0.2: 'rgba(255, 255, 0, 0.8)',
                0.7: 'rgba(234, 72, 58, 0.9)',
                1.0: 'rgba(162, 36, 25, 1)'
            }, {
                0.1: 'rgba(162, 36, 25, 0.7)',
                0.2: 'rgba(234, 72, 58, 0.8)',
                0.7: 'rgba(255, 255, 0, 0.9)',
                1.0: 'rgba(128, 255, 0, 1)'
            }],
            radiuses = [5, 10, 20, 30],
            opacities = [0.4, 0.6, 0.8, 1];

        buttons.leisureObjects.events.add('press', function () {
            buttons.cultureObjects.state.set('selected', false);
            buttons.cateringObjects.state.set('selected', false);

            if (buttonSelected != buttons.leisureObjects) {
                addOpenDataObjects(leisure);
                buttonSelected = buttons.leisureObjects;
            } else {
                buttonSelected = null;
                addOpenDataObjects([]);
            }
        });
        buttons.cultureObjects.events.add('press', function () {
            buttons.leisureObjects.state.set('selected', false);
            buttons.cateringObjects.state.set('selected', false);

            if (buttonSelected != buttons.cultureObjects) {
                addOpenDataObjects(culture);
                buttonSelected = buttons.cultureObjects;
            } else {
                buttonSelected = null;
                addOpenDataObjects([]);
            }
        });
        buttons.cateringObjects.events.add('press', function () {
            buttons.leisureObjects.state.set('selected', false);
            buttons.cultureObjects.state.set('selected', false);
            addOpenDataObjects(catering);

            if (buttonSelected != buttons.cateringObjects) {
                addOpenDataObjects(catering);
                buttonSelected = buttons.cateringObjects;
            } else {
                buttonSelected = null;
                addOpenDataObjects([]);
            }
        });

        function addOpenDataObjects(objects) {
            map.geoObjects.removeAll();

            for (var i = 0; i < objects.length; i++) {
                var object = objects[i];
                map.geoObjects
                    .add(new ymaps.Placemark([object.location.latitude, object.location.longitude],
                        {balloonContent: object.name},
                        {preset: 'islands#greenDotIconWithCaption'})
                    );
            }
        }

        for (var key in buttons) {
            if (buttons.hasOwnProperty(key)) {
                map.controls.add(buttons[key]);
            }
        }

        ymaps.modules.require(['Heatmap'], function (Heatmap) {
            var heatmap = new Heatmap(data, {
                gradient: gradients[0],
                radius: radiuses[2],
                opacity: opacities[2]
            });
            heatmap.setMap(map);
        });
    }

});