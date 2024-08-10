// src/components/ViewProfile/ViewProfile.js
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './ViewProfile.css'; // Create this file for styling

const ViewProfile = () => {
    const [profile, setProfile] = useState(null);
    const [error, setError] = useState(null);
    const [user, setUser] = useState(null);
    const [profileId, setProfileId] = useState(null);

    useEffect(() => {
        const fetchProfileId = async (userId) => {
            try {
                const response = await axios.get(`http://localhost:8082/gitartsy/api/profiles/profileid/${userId}`);
                const id = response.data;
                setProfileId(id);
                return id;
            } catch (error) {
                setError('Error fetching profile ID.');
                console.error('Error fetching profile ID:', error);
            }
        };

        const fetchProfile = async () => {
            const userData = JSON.parse(localStorage.getItem('user'));
            if (userData) {
                setUser(userData);
                const userId = userData.userid;
                const id = await fetchProfileId(userId);
                if (id > 0) {
                    try {
                        const response = await axios.get(`http://localhost:8082/gitartsy/api/profiles/${id}`);
                        setProfile(response.data);
                    } catch (error) {
                        setError('Error fetching profile data.');
                        console.error('Error fetching profile:', error);
                    }
                } else {
                    setError('Profile does not exist.');
                }
            } else {
                setError('No user data found in local storage.');
            }
        };

        fetchProfile();
    }, []);

    if (error) return <div>{error}</div>;
    if (!profile) return <div>Loading...</div>;

    return (
        <div className="profile-container">
            <h1>{profile.name}'s Profile</h1>
            <img src={profile.fileDownloadUri} alt={profile.filename} className="profile-image" />
            <p><strong>Location:</strong> {profile.location}</p>
            <p><strong>Email:</strong> {profile.email}</p>
            <p><strong>Phone:</strong> {profile.phone}</p>
            <p><strong>Bio:</strong> {profile.bioDescription}</p>
        </div>
    );
};

export default ViewProfile;
