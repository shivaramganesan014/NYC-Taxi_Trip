import React, {useState} from "react";
import './showBarChart.css';
import averageTipDistanceData from '../data/averageTip/averageTip.json';
import BarChart from '../charts/BarChart';
import AllBarChart from "../charts/AllBarChart";


const AverageTipDistance = ({selectedCategory}) => {

    function processAverageTip(filter){
        const labels = [];
        const values = [];
        const filterData = averageTipDistanceData[filter];
        filterData.map(row => {
            labels.push(row.day);
            if(selectedCategory == "Average Tip"){
              values.push(row.average_tip);
            }else {
              values.push(row.average_distance);
            }
        })

        return {labels: labels, values: values}
    }

    function processAverageTipAll(){

      const topLabels = ['2021', '2022', '2023'];
      const weekValues = new Map();
      const dataYear2021 = averageTipDistanceData[topLabels[0]];
      dataYear2021.map(row => {
        if(selectedCategory == "Average Tip"){
          weekValues.set(row.day, [row.average_tip]);
        }else {
          weekValues.set(row.day, [row.average_distance]);
        }
    })
    console.log("reached 1");
    const dataYear2022 = averageTipDistanceData[topLabels[1]];
    dataYear2022.map(row => {
      const arr = weekValues.get(row.day);
      if(selectedCategory == "Average Tip"){
        arr.push(row.average_tip);
      }else {
        arr.push(row.average_distance);
      }
      weekValues.set(row.day, arr);
  })
  console.log("reached 2");
    const dataYear2023 = averageTipDistanceData[topLabels[2]];
    dataYear2023.map(row => {
      const arr = weekValues.get(row.day);
      if(selectedCategory == "Average Tip"){
        arr.push(row.average_tip);
      }else {
        arr.push(row.average_distance);
      }
      weekValues.set(row.day, arr);
  })

  const chartData = {
    labels: topLabels,
    datasets: [
      {
        label: 'MONDAY',
        values: weekValues.get("MONDAY"),
      },

      {
        label: 'TUESDAY',
        values: weekValues.get("TUESDAY"),
      },

      {
        label: 'WEDNESDAY',
        values: weekValues.get("WEDNESDAY"),
      },

      {
        label: 'THURSDAY',
        values: weekValues.get("THURSDAY"),
      },

      {
        label: 'FRIDAY',
        values: weekValues.get("FRIDAY"),
      },

      {
        label: 'SATURDAY',
        values: weekValues.get("SATURDAY"),
      },

      {
        label: 'SUNDAY',
        values: weekValues.get("SUNDAY"),
      },
    ],
  };

  return chartData;
  }

    const dropdownOptions = ['2021', '2022', '2023', 'all'];

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
              <button>Selected Year: {selectedOption || 'None'}</button>
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
          {selectedOption != "all" ? <BarChart 
          data={processAverageTip(selectedOption)}
          XaxisLabel={selectedCategory == "Average Tip" ? "Weekdays" : "Weekdays"}
          YaxisLabel={selectedCategory == "Average Tip" ? "Tip percentage" : "Distance in Miles"}
           /> 
          
          :
          
          <AllBarChart 
          data={processAverageTipAll()}
          YLabel={selectedCategory == "Average Tip" ? "Tip percentage" : "Distance in Miles"}
          />}
          </div>
        </div>
    );
}

export default AverageTipDistance;
