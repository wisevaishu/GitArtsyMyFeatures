// src/components/Layout.js
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import '../Home/Home.css'; // Ensure this CSS file is accessible from this location

const Layout = ({ children }) => {
    const [user, setUser] = useState(null);

    useEffect(() => {
        const userData = JSON.parse(localStorage.getItem('user'));
        if (userData) {
            setUser(userData);
        }
    }, []);

    if (!user) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <nav className="navbar">
                <ul>
                    <li><Link to="/home">Home</Link></li>
                    <li><Link to="/uploadartwork">Upload Artwork</Link></li>
                    <li><Link to="/tags">Manage Tags</Link></li> 
                    <li><Link to="/artworkslist">List Artworks</Link></li> 
                    {/* <li><Link to="/sample">sample</Link></li> 
                    <li><Link to="/samplelist">sample list</Link></li>  */}
                    
                </ul>
            </nav>
            <div className="home-content">
                <h1>Welcome, {user.username}</h1>
                <p>User ID: {user.userid}</p>
                <p>Role: {user.role}</p>
            </div>
            <main>{children}</main>
        </div>
    );
};

export default Layout;