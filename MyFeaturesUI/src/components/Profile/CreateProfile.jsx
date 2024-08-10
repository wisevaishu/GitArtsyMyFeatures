import React, { useState } from 'react';
import profileService from '../Services/ProfileService'; 
import '../Profile/CreateProfile.css'; 
import { useNavigate } from 'react-router-dom';

const CreateProfile = () => {
    const [formData, setFormData] = useState({
        name: '',
        location: '',
        email: '',
        phone: '',
        bioDescription: ''
    });
    const [selectedFile, setSelectedFile] = useState(null);
    const [preview, setPreview] = useState('');
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const userId = JSON.parse(localStorage.getItem('user')).userid;
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevData => ({
            ...prevData,
            [name]: value
        }));
    };

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        setSelectedFile(file);

        if (file) {
            setPreview(URL.createObjectURL(file));
            setError("");
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!selectedFile) {
            setError("Please select a profile picture.");
            return;
        }

        const formDataToSend = new FormData();
        formDataToSend.append('userId', userId);
        formDataToSend.append('name', formData.name);
        formDataToSend.append('location', formData.location);
        formDataToSend.append('email', formData.email);
        formDataToSend.append('phone', formData.phone);
        formDataToSend.append('bioDescription', formData.bioDescription);
        formDataToSend.append('profilePic', selectedFile);

        try {
           await profileService.createProfile(formDataToSend);
           alert('Profile Cretaed');
           setFormData({
            name: '',
            location: '',
            email: '',
            phone: '',
            bioDescription: ''
            

        }); 
        setSelectedFile("");
        setPreview("");
        
        navigate('/viewprofile');
        } catch (error) {
            console.error("Error creating profile:", error);
            setError("An error occurred while creating the profile.");
            setSuccess("");
        }
    };

    return (
        <div className="create-profile-container">
            <h1>Create Profile</h1>
            <form onSubmit={handleSubmit}>
                <label>
                    Name:
                    <input type="text" name="name" value={formData.name} onChange={handleChange} required />
                </label>
                <br />
                <label>
                    Location:
                    <input type="text" name="location" value={formData.location} onChange={handleChange} required />
                </label>
                <br />
                <label>
                    Email:
                    <input type="email" name="email" value={formData.email} onChange={handleChange} required />
                </label>
                <br />
                <label>
                    Phone:
                    <input type="text" name="phone" value={formData.phone} onChange={handleChange} required />
                </label>
                <br />
                <label>
                    Bio Description:
                    <textarea name="bioDescription" value={formData.bioDescription} onChange={handleChange} required />
                </label>
                <br />
                <label>
                    Profile Picture:
                    <input type="file" name="profilePic" onChange={handleFileChange} required />
                </label>
                {preview && <img src={preview} alt="Preview" style={{ marginTop: '10px', width: '200px', height: 'auto' }} />}
                <br />
                <button type="submit">Create Profile</button>
            </form>
            {error && <p className="error-message">{error}</p>}
            {success && <p className="success-message">{success}</p>}
        </div>
    );
};

export default CreateProfile;
