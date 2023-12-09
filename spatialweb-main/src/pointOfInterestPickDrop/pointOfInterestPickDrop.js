import React from "react";
import ImageRow from "../imageRow/imageRow";
import drop from '../data/point of interest pickup and dropoff/Dropoff_X_POI.png';
import pickup from '../data/point of interest pickup and dropoff/Pickups_X_POI.png';

const PointOfInterestPickDrop = () => {

    const images = [
        { heading: 'Pickup Point of Interest', path: pickup },
        { heading: 'Drop Off Point of Interest', path: drop },
      ];  

      return (
        <ImageRow images={images} height="80%"/>
      );

};

export default PointOfInterestPickDrop;