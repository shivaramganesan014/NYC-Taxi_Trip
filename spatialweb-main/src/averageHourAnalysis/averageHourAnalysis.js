import React from "react";
import averageDistance from '../data/average analysis by hour/average distance by hour.png';
import averageTip from '../data/average analysis by hour/average tip by hour.png';
import ImageRow from "../imageRow/imageRow";

const AverageHourAnalysis = () => {

    const images = [
        { heading: 'Average distance by hour', path: averageDistance },
        { heading: 'Average tip by hour', path: averageTip },
      ];    

    return (

            <ImageRow images={images} height="60%"/>

    );

};

export default AverageHourAnalysis;