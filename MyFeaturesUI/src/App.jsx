

import { Routes, Route, BrowserRouter as Router } from 'react-router-dom';
import './App.css';

import Home from './components/Home/Home';


import LandingLogin from './components/LoginLandingPage/LoginLanding';

import UploadArtwork from './components/UploadArtwork/UploadArtwork'; 

import ArtworksList from './components/UploadArtwork/ArtworkList';
import Tag from './components/Tag/Tag'; 
import CreateProfile from './components/Profile/CreateProfile';
import ViewProfile from './components/Profile/ViewProfile';




const App = () => {
  return (
   

    <Router>
    <Routes>
        <Route path="/" element={<LandingLogin />} />
        <Route path="/home" element={<Home />} />
        
        <Route path="/uploadartwork" element={<Home><UploadArtwork /></Home>} />
        <Route path="/artworkslist" element={<Home><ArtworksList /></Home>} />
        <Route path="/tags" element={<Home><Tag /></Home>} />

        <Route path="/createprofile" element={<Home><CreateProfile /></Home>} />
        <Route path="/viewprofile" element={<Home><ViewProfile /></Home>} />

   
        
    </Routes>
  </Router>

    
  );
};

export default App;