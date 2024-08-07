import React, { useState, useEffect } from 'react';
import axios from 'axios';
//import './cssforpages/Artwork.css';

//Artwork Slide Method
const ArtworksPopup = () => {
  const [artworks, setArtworks] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);

  useEffect(() => {
    fetchArtworks(); 
  }, []);


//Fetching Artworks from external API and limiting to 20 images
  const fetchArtworks = async () => {
    try {
      const response = await axios.get('https://api.artic.edu/api/v1/artworks?page=1&limit=20');
      const data = response.data.data;
      const newArtworks = data.map(artwork => ({
        id: artwork.id,
        imageUrl: `https://www.artic.edu/iiif/2/${artwork.image_id}/full/843,/0/default.jpg`
      }));
      setArtworks(newArtworks);
    } catch (error) {
      console.error('Error fetching artworks:', error);
    }
  };

//Sliding through the images in a 2 second rotation
  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentIndex(prevIndex => (prevIndex + 1) % artworks.length);
    }, 2000);

    return () => clearInterval(interval); 
  }, [artworks]); 

  
  const artworkToShow = artworks.length > 0 ? artworks[currentIndex] : null;

  return (
    <>
      {artworkToShow && (
        <div className="popup">
          <img src={artworkToShow.imageUrl} alt={`Artwork ${currentIndex}`} />
        </div>
      )}
    </>
  );
};

export default ArtworksPopup;