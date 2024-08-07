import React from 'react';
import Login from './Login';
import ArtworksPopup from './Artwork';
import '../styling.css';
import Banner from '../Banner';


const LandingLogin = () => {
  return (
    <div className="app-container">
        <Banner />
      <div className="login-container">
         <Login /> 
      </div>
      <div className="popup-container">
        {/* <ArtworksPopup /> */}
      </div>
      {/* Footer Part */}
    </div>
  );
};

export default LandingLogin;