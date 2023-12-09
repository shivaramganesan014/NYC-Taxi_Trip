import Visualization from '../visualization/visualization';
import './openSelection.css';
import React, { useState, useEffect } from 'react';
import AverageTipDistance from '../averageTipDistance/ShowBarChart';
import DisplayMap from '../map/DisplayMap';
import CarbonEmissionAnalysis from '../carbonEmissionAnalysis/carbonEmissionAnalysis';
import AverageHourAnalysis from '../averageHourAnalysis/averageHourAnalysis';
import DistanceCost from '../distanceCost/distanceCost';
import PointOfInterestPickDrop from '../pointOfInterestPickDrop/pointOfInterestPickDrop';
import TopBusiestLocations from '../topBusiestLocations/topBusiestLocations';

const OpenSelection = ({selectionOption}) => {

    console.log(selectionOption);

    if(selectionOption == "Average Tip"){
        return (
            <div className='openSelectionTopContainer'>
                <AverageTipDistance selectedCategory={"Average Tip"} />
            </div>
        );
    }

    if(selectionOption == "Average Distance"){
        return (
            <div className='openSelectionTopContainer'>
                <AverageTipDistance selectedCategory={"Average Distance"} />
            </div>
        );
    }

    if(selectionOption == "Carbon Emission"){
        return (
            <div className='openSelectionTopContainer'>
                <CarbonEmissionAnalysis />
            </div>
        );
    }

    if(selectionOption == "Option 2"){
        return (
            <div className='openSelectionTopContainer'>

            </div>
        );
    }

    if(selectionOption == "Busiest pickup"){

        return (
            <div className='openSelectionTopContainer'>
                <Visualization />
            </div>
        );
    }

    if(selectionOption == "Busiest dropoff"){

        return (
            <div className='openSelectionTopContainer'>
                <Visualization />
            </div>
        );
    }

    if(selectionOption == "Pickups near subway stations"){

        return (
            <div className='openSelectionTopContainer'>
                <Visualization />
            </div>
        );

    }

    if(selectionOption == "Average hour analysis"){

        return (
            <div className='openSelectionTopContainer'>
                <AverageHourAnalysis />
            </div>
        );

    }

    if(selectionOption == "Distance Cost"){

        return (
            <div className='openSelectionTopContainer'>
                <DistanceCost />
            </div>
        );

    }

    if(selectionOption == "Point of Interest Pickup & Drop off"){

        return (
            <div className='openSelectionTopContainer'>
                <PointOfInterestPickDrop />
            </div>
        );

    }

    if(selectionOption == "Top Busiest Locations"){

        return (
            <div className='openSelectionTopContainer'>
                <TopBusiestLocations />
            </div>
        );

    }

    if(selectionOption === "Aggregate data on map") {
        return (
            <DisplayMap />
        );
    }

};


export default OpenSelection;