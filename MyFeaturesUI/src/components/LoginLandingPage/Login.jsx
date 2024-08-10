import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../Services/AuthService';

const Login = () => {
    const [formData, setFormData] = useState({
        username: '', 
        password: ''
    });

    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log('Inside handleSubmit');
        try {
            const response = await api.login(formData.username, formData.password);
            console.log('Login successful:', response);
            localStorage.setItem('user', JSON.stringify(response));
            navigate('/createprofile');
        } catch (error) {
            console.error('Login error:', error);
            setErrorMessage(error.response?.data || 'An error occurred');
        }
    };

    return (
        <div className="container">
            <div className="header">
                <div className="text">Login</div>
                <div className="underline"></div>
            </div>
            <form onSubmit={handleSubmit}>
                <div className="inputs">
                    <div className="input">
                        <input type="text" name="username" placeholder="Username" required value={formData.username} onChange={handleChange} />
                    </div>
                    <br />
                    <div className="input">
                        <input type="password" name="password" placeholder="Password" required value={formData.password} onChange={handleChange} />
                    </div>
                </div>
                {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
                <div className="submit-container">
                    <button type="submit" className="submit">Login</button>
                </div>
            </form>
            {/* <div className="footer">
                Not a member? <Link to='/signup'>Sign up here!</Link>
            </div> */}
        </div>
    );
};

export default Login;