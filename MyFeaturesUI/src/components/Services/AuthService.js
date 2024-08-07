import axios from 'axios';


const API_URL = 'http://localhost:8082/gitartsy/api';

axios.defaults.withCredentials = true;



const register = async (username, email, password, verifyPassword, role) => {
    try {
        const response = await axios.post(`${API_URL}/register/save`, {
            username,
            email,
            password,
            verifyPassword,
            role
        });
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : new Error('Registration failed');
    }
};

const login = async (username, password) => { // Ensure the parameter names match backend expectations
    try {
        const response = await axios.post(`${API_URL}/register/login`, {
            username, 
            password
        });
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : new Error('Login failed');
    }
};

export default {
    register,
    login
};