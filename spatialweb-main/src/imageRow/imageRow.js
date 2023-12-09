import React from 'react';

const ImageRow = ({ images, height = '50%' }) => {
  return (
    <div style={{ display: 'flex'}}>
      {images.map((image, index) => (
        <div key={index} style={{ marginRight: '20px', marginLeft: '20px' }}>
          <h2>{image.heading}</h2>
          <img
            src={image.path}
            alt={image.heading}
            style={{  width: '100%', height: height, marginBottom: '10px' }}
          />
        </div>
      ))}
    </div>
  );
};

export default ImageRow;
