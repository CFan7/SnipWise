import React, { useState } from "react";
import "./CreateShortenUrl.css";

const CreateShortenUrl = () => {
  const [formData, setFormData] = useState({
    originalUrl: "",
    customizedName: "",
  });
  const [shortenedUrl, setShortenedUrl] = useState("");

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // communicate with backend to shorten the URL
    const mockShortenedUrl = "http://company.SnipWise.com/store"; // mock URL
    setShortenedUrl(mockShortenedUrl);
  };

  return (
    <div className="create-url-container">
      <h2 className="title">Create Shorten URL</h2>
      <form onSubmit={handleSubmit} className="url-form">
        <label className="form-label">
          Original URL
          <input
            type="text"
            name="originalUrl"
            value={formData.originalUrl}
            onChange={handleChange}
            className="input-field"
            placeholder="Enter the original URL"
          />
        </label>
        <label className="form-label">
          Customized Name (optional)
          <input
            type="text"
            name="customizedName"
            value={formData.customizedName}
            onChange={handleChange}
            className="input-field"
            placeholder="Enter a customized name"
          />
        </label>
        <button type="submit" className="submit-button">Shorten URL</button>
      </form>
      {shortenedUrl && <p className="result">Shortened URL: {shortenedUrl}</p>}
    </div>
  );
};

export default CreateShortenUrl;
