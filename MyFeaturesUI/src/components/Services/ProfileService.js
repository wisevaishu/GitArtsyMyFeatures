
import axios from 'axios';

const API_URL = 'http://localhost:8082/gitartsy/api/profiles'; 


export const createProfile = async (profileData) => {
    try {
        const response = await axios.post(`${API_URL}/new`, profileData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        return response.data;
    } catch (error) {
        throw new Error('Error creating profile');
    }
};



export const checkProfileExists = async (userId) => {
    try {
        const response = await axios.get(`http://localhost:8082/gitartsy/api/profiles/exists/${userId}`);
        return response.data; // true or false
    } catch (error) {
        console.error("Error checking if profile exists:", error);
        return false;
    }
};



export default {
  
    createProfile,
    checkProfileExists
  };

