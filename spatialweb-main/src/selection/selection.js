import React, { useState, useEffect } from 'react';
import './selection.css';
import OpenSelection from '../openSelection/openSelection';


const Selection = () => {
      // Arrays of options
  const visualizationOptions = ["Busiest pickup", "Busiest dropoff", "Fare amount based on pickup location", "Pickups near subway stations", "Carbon emission estimation", "Aggregate data on map"];
  const analysisOptions = ["Fare amount vs tip amount", "Duration vs Distance", "Fare amount prediction", "Average Tip", "Average Distance", "Carbon Emission", "Average hour analysis", "Distance Cost", "Point of Interest Pickup & Drop off", "Top Busiest Locations"];
  const [selectedType, setSelectedType] = useState('Analysis');
  const [selectionOption, setSelectedOption] = useState('');
  const [selectedOptions, setSelectedOptions] = useState(analysisOptions);
  const [csvData, setCsvData] = useState();

  return (
    <div className="selectionContainer">
        <div className='selectionColumn'>
        <div className='selectionRow'>
            <div className='selectionButtonBox'>
                <div style={{
                cursor: 'pointer', 
                padding: '5px',
                margin: '10px',
                borderRadius: '10px',
                backgroundColor: selectedType == "Analysis" ? 'green' : '#fff',
            }} value={"Analysis"} onClick={() => {
                setSelectedType('Analysis');
                setSelectedOptions(analysisOptions);
            }}>
                <div style={{
                    color: selectedType == "Analysis" ? 'white' : '#000',
                    fontWeight: selectedType == "Analysis" ? 'bold' : 'normal'
                }}> Analysis </div>
            </div>

            <div style={{
                cursor: 'pointer', 
                margin: '10px',
                padding: '5px',
                borderRadius: '10px',
                backgroundColor: selectedType == "Visualization" ? 'green' : '#fff',
            }} value={"Visualization"} onClick={() => {
                setSelectedType('Visualization');
                setSelectedOptions(visualizationOptions);
            }}>
                <div style={{
                    color: selectedType == "Visualization" ? 'white' : '#000',
                    fontWeight: selectedType == "Visualization" ? 'bold' : 'normal'
                }}> Visualization </div>
            </div>
            </div>
        </div>

        <div className='selectionBodyRow'>
        <div className='selectionColumn-1'>
       {selectedOptions.map((option, index) => (
        <div key={index}>
          <option style={{
            cursor: 'pointer', 
            margin: '10px',
            padding: '8px',
            width: '85%',
            display: 'flex',
            flexDirection: 'column',
            marginBottom: '15px',
            borderRadius: '5px',
            whiteSpace: 'pre-wrap',
            border: selectionOption == option ? '2px solid green' : '0.5px solid black',
            backgroundColor: '#fff',
            boxShadow: '0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19)',
        }} value={option} onClick={() => {
            setSelectedOption(option);
        }}>
            {option}
        </option>
         </div>
       ))}
       </div>
       <div className='selectionColumn-2'>
        <OpenSelection selectionOption={selectionOption} />
       </div>
        </div>
        </div>
    </div>
  );
};

export default Selection;
