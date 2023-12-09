import React, {useState} from "react";
import './distanceCost.css';
import distanceCost2021Chart from '../data/distance cost/2021/Distance Cost 2021.png';
import distanceCost2021Pie from '../data/distance cost/2021/Distance Cost Pie 2021.png';
import distanceCost2022Chart from '../data/distance cost/2022/Distance Cost 2022.png';
import distanceCost2022Pie from '../data/distance cost/2022/Distance Cost Pie 2022.png';
import distanceCost2023Chart from '../data/distance cost/2023/Distance Cost 2023.png';
import distanceCost2023Pie from '../data/distance cost/2023/Distance Cost Pie 2023.png';
import ImageRow from "../imageRow/imageRow";



const DistanceCost = () => {

    const dropdownOptions = ['2021', '2022', '2023'];

    const [selectedOption, setSelectedOption] = useState(dropdownOptions[0]);

    const image1 = [
        { heading: '', path: distanceCost2021Chart },
        { heading: '', path: distanceCost2021Pie },
      ];  
      
    const image2 = [
        { heading: '', path: distanceCost2022Chart },
        { heading: '', path: distanceCost2022Pie },
      ]; 

    const image3 = [
        { heading: '', path: distanceCost2023Chart },
        { heading: '', path: distanceCost2023Pie },
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

            {selectedOption == "2021" ? <ImageRow images={image1} height="80%"/> 
            : selectedOption == "2022" ? <ImageRow images={image2} height="80%"/> 
            : <ImageRow images={image3} height="80%"/>}
          
          </div>
        </div>
    );

};

export default DistanceCost;