import React, {useState} from "react";
import './topBusiestLocations.css';
import busy2021Drop from '../data/top k busiest dropoff/Top k busiest dropoff 2021.png';
import busy2022Drop from '../data/top k busiest dropoff/Top k busiest dropoff 2022.png';
import busy2023Drop from '../data/top k busiest dropoff/Top k busiest dropoff 2023.png';
import busy2021Pick from '../data/top k busiest pickup/Top k busiest pickup 2021.png';
import busy2022Pick from '../data/top k busiest pickup/Top k busiest pickup 2022.png';
import busy2023Pick from '../data/top k busiest pickup/Top k busiest pickup 2023.png';

import ImageRow from "../imageRow/imageRow";



const TopBusiestLocations = () => {

    const dropdownOptions = ['2021', '2022', '2023'];

    const [selectedOption, setSelectedOption] = useState(dropdownOptions[0]);

    const image1 = [
        { heading: 'Busiest 2021 Pickup', path: busy2021Pick },
        { heading: 'Busiest 2021 Drop off', path:  busy2021Drop},
      ];  
      
    const image2 = [
        { heading: 'Busiest 2022 Pickup', path: busy2022Pick },
        { heading: 'Busiest 2022 Drop off', path: busy2022Drop },
      ]; 

    const image3 = [
        { heading: 'Busiest 2023 Pickup', path: busy2023Pick },
        { heading: 'Busiest 2023 Drop off', path: busy2023Drop },
      ]; 
  
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
              <button>Selected year: {selectedOption || 'None'}</button>
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

            {selectedOption == "2021" ? <ImageRow images={image1} height="90%"/> 
            : selectedOption == "2022" ? <ImageRow images={image2} height="90%"/> 
            : <ImageRow images={image3} height="90%"/>}
          
          </div>
        </div>
    );

};

export default TopBusiestLocations;