import React, { useState, useEffect } from 'react';
import api from '../Services/TagService';
import '../Tag/Tag.css'; // Import the CSS file

const TagManager = () => {
    const [tags, setTags] = useState([]);
    const [formData, setFormData] = useState({ name: '' });
    const [currentTagId, setCurrentTagId] = useState(null);
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        const fetchTags = async () => {
            try {
                const response = await api.getAllTags();
                setTags(response.data);
            } catch (error) {
                console.error('Error fetching tags:', error);
            }
        };

        fetchTags();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            if (currentTagId) {
                await api.updateTag(currentTagId, formData);
                alert('Tag updated successfully!');
            } else {
                await api.createTag(formData);
                alert('Tag created successfully!');
            }
            setFormData({ name: '' });
            setCurrentTagId(null);
            const response = await api.getAllTags();
            setTags(response.data);
        } catch (error) {
            setErrorMessage('Failed to save tag');
            console.error('Error saving tag:', error);
        }
    };

    const handleEdit = (tag) => {
        setFormData({ name: tag.name });
        setCurrentTagId(tag.tagId);
    };

    const handleDelete = async (id) => {
        try {
            await api.deleteTag(id);
            setTags(tags.filter(tag => tag.tagId !== id));
        } catch (error) {
            console.error('Error deleting tag:', error);
        }
    };

    return (
        <div className="tag-manager-container">
            <h1>Tag Manager</h1>

            <form onSubmit={handleSubmit}>
                <label>
                    Name:
                    <input
                        type="text"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                    />
                </label>
                <br />
                <button type="submit">
                    {currentTagId ? 'Update Tag' : 'Create Tag'}
                </button>
                {errorMessage && <p className="error-message">{errorMessage}</p>}
            </form>

            <h2>All Tags</h2>
            <ul>
                {tags.map(tag => (
                    <li key={tag.tagId}>
                        {tag.name}
                        <div>
                            <button className="btn-edit" onClick={() => handleEdit(tag)}>Edit</button>
                            <button className="btn-delete" onClick={() => handleDelete(tag.tagId)}>Delete</button>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default TagManager;