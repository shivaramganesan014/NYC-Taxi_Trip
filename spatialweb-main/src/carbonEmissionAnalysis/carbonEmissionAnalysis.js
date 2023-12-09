import React, {useState} from "react";
import './carbonEmissionAnalysis.css';
import carbonEmissionAnalysisData from '../data/carbonEmission/carbonEmissionAnalysis.json';
import BarChart from '../charts/BarChart';

const CarbonEmissionAnalysis = () => {

    const xLabel = {
        "Hourly": "Hours",
        "Daily": "Days",
        "Monthly": "Months",
        "Yearly": "Years"
    };

    function processCarbonEmissionData(filter){
        const labels = [];
        const values = [];
        const filterData = carbonEmissionAnalysisData[filter];
        if(filter == "Yearly"){
            filterData.map((row, index) => {
                labels.push(row.Year);
                values.push(row.value);
            })
        }else{
            filterData.map((row, index) => {
                labels.push(index + 1);
                values.push(row.value);
            })
        }

        return {labels: labels, values: values}
    }

    const dropdownOptions = ['Hourly', 'Daily', 'Monthly', 'Yearly'];

    const [selectedOption, setSelectedOption] = useState(dropdownOptions[0]);
  
    const handleItemClick = (option) => {
      console.log(`Selected option: ${option}`);
      setSelectedOption(option);
    };

    return (
        <div style={{
          width: '100%',
          height: '100%'
        }}>
          <div className="rowShortBarChar">
            <div className="alignEnd">
            <div className="dropdown">
              <button>Selected Frequency: {selectedOption || 'None'}</button>
              <div className="dropdown-content">
                {dropdownOptions.map((option, index) => (
                  <div key={index} onClick={() => handleItemClick(option)}>
                    {option}
                  </div>
                ))}
              </div>
            </div>
          </div>
          </div>
          <div style={{
            margin: '20px'
          }}>
          <BarChart 
          data={processCarbonEmissionData(selectedOption)}
          XaxisLabel={xLabel[selectedOption]}
          YaxisLabel={"Carbon emmissions in KG"}
          barColor={'rgba(255, 69, 0, 1)'}
           /> 
          </div>
        </div>
    );
}

export default CarbonEmissionAnalysis;
