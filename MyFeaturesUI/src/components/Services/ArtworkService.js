import axios from 'axios';



const BASE_URL = 'http://localhost:8082/gitartsy/api/artworks';

// Add a new artwork
export const uploadArtwork =  (formData) => {
  try {
    const response =  axios.post(`${BASE_URL}/new`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
        
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error response data:", error.response?.data);
    console.error("Error response status:", error.response?.status);
    console.error("Error response headers:", error.response?.headers);
    throw new Error("There was an error creating the artwork!");
  }
};


// Fetch artworks by profile ID
export const fetchArtworksByProfile = async  (profileId) => {
  try {
      const response =  await axios.get(`${BASE_URL}/profile/${profileId}`);
      console.log(response);
      return response.data;
  } catch (error) {
      console.error("There was an error fetching the artworks!", error);
      throw error;
  }
};

export default {
  
  uploadArtwork,
  fetchArtworksByProfile
};