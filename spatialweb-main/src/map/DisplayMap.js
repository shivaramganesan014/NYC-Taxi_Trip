import React, { useState, useEffect, useRef } from 'react';
import './DisplayMap.css';
import pickupData from '../data/displayMap/pickups.json';
import dropoffData from '../data/displayMap/dropoffs.json';
import {Map, View, Overlay} from 'ol';
import {Vector as VectorLayer, Tile} from 'ol/layer';
import {Vector as VectorSource, OSM} from 'ol/source';
import {GeoJSON} from 'ol/format';
import {Style, Stroke, Fill, Text} from 'ol/style';

const DisplayMap = () => {
    const mainOptions = ['Pickups', 'Dropoffs'];
    const dayOptions = ['all', 'morning', 'afternoon', 'evening', 'night'];
    const weekOptions = ['all', 'weekdays', 'weekends'];
    const yearOptions = ['all', '2021', '2022', '2023']

    const [selectedMain, setSelectedMain] = useState(mainOptions[0]);
    const [selectedDay, setSelectedDay] = useState(dayOptions[0]);
    const [selectedWeek, setSelectedWeek] = useState(weekOptions[0]);
    const [selectedYear, setSelectedYear] = useState(yearOptions[0]);

    const popupRef = useRef(null);

    useEffect(() => {
        const popup = popupRef.current;
        let extent;
        const vectorSource = new VectorSource({
            url: 'https://gist.githubusercontent.com/bhavyagada/4ebaf654d3714689b24415bd9657d0fc/raw/f8d8fc22a1fe161a93c1ae4629766a5de8f9fe62/taxi_zones',
            format: new GeoJSON()
        });

        vectorSource.once('change',function(e){
            if(vectorSource.getState() === 'ready') {
                const nyExtent = vectorSource.getExtent();
                map.getView().fit(nyExtent, map.getSize());
                map.setView(
                    new View({
                        center: map.getView().getCenter(),
                        // extent: map.getView().calculateExtent(map.getSize()),
                        zoom: map.getView().getZoom(),
                        minZoom: map.getView().getZoom() - 1
                    })
                );
            }
        });

        const vectorLayer = new VectorLayer({
            source: vectorSource,
            style: function (feature) {
                return new Style({
                    text: new Text({
                        text: feature.getProperties()["zone"],
                        font: '16px Calibri,sans-serif',
                        fill: new Fill({
                            color: '#FFF'
                        }),
                        stroke: new Stroke({
                            color: '#000',
                            width: 2
                        })
                    }),
                    stroke: new Stroke({
                        color: "black",
                        width: 1.5
                    }),
                    fill: new Fill({
                        color: [255, 0, 0, 0.4],
                    })
                });
            },
            zIndex: 1
        })

        vectorLayer.setExtent(extent);

        const overlay = new Overlay({
            element: popup,
        });

        function displayPopup(evt) {
            var pixel = evt.pixel;
            var feature = map.forEachFeatureAtPixel(pixel, function(feature) {
                return feature;
            });
            
            popup.style.display = feature ? '' : 'none';
            if (feature) {
                var html = "";
                console.log(feature);
                overlay.setPosition(evt.coordinate);
                var attributeNames = feature.getProperties();
                console.log(attributeNames);

                var filteredData;
                if (selectedMain === "Pickups") {
                    filteredData = pickupData.filter(row => {
                        return String(row['PULocationID']) === attributeNames['OBJECTID'];
                    });
                } else {
                    filteredData = dropoffData.filter(row => {
                        return String(row['DOLocationID']) === attributeNames['OBJECTID'];
                    });
                }
    
                // filter data based on selected options
                if (selectedYear !== 'all') {
                    filteredData = filteredData.filter(row => String(row['year']) === selectedYear);
                }
                console.log(filteredData);

                if (selectedDay !== 'all') {
                    filteredData = filteredData.filter(row => row['time_of_day'] === selectedDay);
                }

                if (selectedWeek !== 'all') {
                    const isWeekend = selectedWeek === 'weekends' ? 1 : 0;
                    filteredData = filteredData.filter(row => row['is_weekend'] === isWeekend);
                }

                console.log(`filtered data for ${selectedDay}, ${selectedWeek}, ${selectedYear}: ${filteredData}`);

                const totalTrips = filteredData.reduce((acc, row) => acc + row['total_trips'], 0);
                const averageTotalAmount = filteredData.reduce((acc, row) => acc + row['average_total_amount'], 0) / filteredData.length;
                const averagePassengerCount = filteredData.reduce((acc, row) => acc + row['average_passenger_count'], 0) / filteredData.length;
                const averageDistance = filteredData.reduce((acc, row) => acc + row['average_trip_distance'], 0) / filteredData.length;
                console.log(totalTrips, averageTotalAmount, averagePassengerCount, averageDistance);
                
                html += `${attributeNames['zone']}<br><hr>`;
                html += `${selectedMain}: ${String(totalTrips)}<br>`;
                html += `Average Cost: ${String(averageTotalAmount.toFixed(2))}<br>`;
                html += `Average Trip Distance: ${String(averageDistance.toFixed(2))}<br>`;
                html += `Average Passenger Count: ${String(averagePassengerCount.toFixed(2))}`;
                console.log(html);
                popup.innerHTML = html;
            }
        };

        const map = new Map({
            layers: [
                new Tile({
                    source: new OSM(),
                }),
                vectorLayer
            ],
            overlays: [overlay],
            target: 'map',
            view: new View({
                center: [0, 0],
                zoom: 2,
            }),
        });

        map.on('click', displayPopup);
        return () => {
            map.dispose();
        };
    }, [selectedMain, selectedDay, selectedWeek, selectedYear]);

    return (
        <div id="displayMap">
            <div className="rowOptions">
                <table>
                <tbody>
                    <tr className='categoryRow'>
                        {mainOptions.map((option, index) => (
                            <td key={index} onClick={() => setSelectedMain(option)} className={selectedMain === option ? "selectedOption" : ""}>
                                {option}
                            </td>
                        ))}
                    </tr>
                    <tr className='categoryRow'>
                        {dayOptions.map((option, index) => (
                            <td key={index} onClick={() => setSelectedDay(option)} className={selectedDay === option ? "selectedOption" : ""}>
                                {option}
                            </td>
                        ))}
                    </tr>
                    <tr className='categoryRow'>
                        {weekOptions.map((option, index) => (
                            <td key={index} onClick={() => setSelectedWeek(option)} className={selectedWeek === option ? "selectedOption" : ""}>
                                {option}
                            </td>
                        ))}
                    </tr>
                    <tr className='categoryRow'>
                        {yearOptions.map((option, index) => (
                            <td key={index} onClick={() => setSelectedYear(option)} className={selectedYear === option ? "selectedOption" : ""}>
                                {option}
                            </td>
                        ))}
                    </tr>
                </tbody>
                </table>
            </div>
            <div id="map"></div>
            <div ref={popupRef} id="popup" className="ol-popup"></div>
        </div>
    );
}

export default DisplayMap;