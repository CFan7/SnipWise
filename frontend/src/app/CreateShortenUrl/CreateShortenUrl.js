import React, { useState, useEffect } from "react";
import "./CreateShortenUrl.css";
import axiosInstance from "../../api/axiosInstance";

const CreateShortenUrl = () => {
  const [companies, setCompanies] = useState([]);
  const [groups, setGroups] = useState([]);

  const [formData, setFormData] = useState({
    company: "",
    group: "",
    originalUrl: "",
    customizedName: "",
    expirationDays: ""
  });
  const [shortenedUrl, setShortenedUrl] = useState("");

  const clientEmail = localStorage.getItem('clientEmail');

  useEffect(() => {
    const fetchCompanies = async () => {
      try {
        const companyResponse = await axiosInstance.get(`/clients/${clientEmail}/access?type=company&role=owner`);
        const companyList = companyResponse.data;
        setCompanies(companyList);
        if (companyList.length > 0) {
          const firstCompany = companyList[0];
          setFormData(prev => ({ ...prev, company: firstCompany }));
        }
      } catch (error) {
        console.error("Failed to fetch companies:", error);
      }
    };
    fetchCompanies();
  }, [clientEmail]);

  // fetch groups when company is changed
  useEffect(() => {
    const fetchGroups = async () => {
      if (formData.company) {
        try {
          const groupResponse = await axiosInstance.get(`/companies/${formData.company}/groups`);
          const groupList = groupResponse.data; 
          setGroups(groupList);
          if (groupList.length > 0) {
            setFormData(prev => ({ ...prev, group: groupList[0].groupId }));
          } else {
            setFormData(prev => ({ ...prev, group: "" }));
          }
        } catch (error) {
          console.error("Failed to fetch groups:", error);
        }
      }
    };
    fetchGroups();
  }, [formData.company]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleCompanyChange = (e) => {
    const selectedCompany = e.target.value;
    setFormData(prev => ({ ...prev, company: selectedCompany }));
  };

  const handleGroupChange = (e) => {
    const selectedGroupId = e.target.value;
    setFormData(prev => ({ ...prev, group: selectedGroupId }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const expirationDate = new Date();
      const days = parseInt(formData.expirationDays, 10);
      if (!isNaN(days)) {
        expirationDate.setDate(expirationDate.getDate() + days);
      }
      const expirationISO = expirationDate.toISOString(); 

      const payload = {
        suffix: formData.customizedName || "",
        originalUrl: formData.originalUrl,
        expirationTime: expirationISO,
        isActivated: true,
        groupId: formData.group
      };

      const response = await axiosInstance.post("/url", payload);
      if (response.data && response.data.shortUrl) {
        console.log("Shortened URL:", response.data);
        alert("URL shortened successfully! Short Url: " + response.data.shortUrl);
        setShortenedUrl(response.data.shortUrl);
      } else {
        console.log("Failed to shorten URL:", response.data);
        alert("Failed to shorten URL. Please try again.");
      }
    } catch (error) {
      if (error.response && error.response.status === 409) {
        alert("Customized name already exists. Please try another one.");
      } else {
        console.error("Failed to shorten URL:", error);
        alert("Failed to shorten URL. Please check console for details.");
      }
    }
  };

  return (
    <div className="create-url-container">
      <h2 className="title">Create Shorten URL</h2>
      <form onSubmit={handleSubmit} className="url-form">
        <label className="form-label">
          Select Company
          <select
            name="company"
            value={formData.company}
            onChange={handleCompanyChange}
            className="input-field"
          >
            {companies.map((companyName, index) => (
              <option key={index} value={companyName}>
                {companyName}
              </option>
            ))}
          </select>
        </label>

        <label className="form-label">
          Select Group
          <select
            name="group"
            value={formData.group}
            onChange={handleGroupChange}
            className="input-field"
          >
            {groups.map((g) => (
              <option key={g.groupId} value={g.groupId}>
                {g.groupName}
              </option>
            ))}
          </select>
        </label>

        <label className="form-label">
          Original URL
          <input
            type="text"
            name="originalUrl"
            value={formData.originalUrl}
            onChange={handleChange}
            className="input-field"
            placeholder="Enter the original URL"
            required
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

        <label className="form-label">
          Expiration Time (in days)
          <input
            type="number"
            name="expirationDays"
            value={formData.expirationDays}
            onChange={handleChange}
            className="input-field"
            placeholder="Enter expiration time in days"
          />
        </label>

        <button type="submit" className="submit-button">Shorten URL</button>
      </form>
      {shortenedUrl && <p className="result">Shortened URL: {shortenedUrl}</p>}
    </div>
  );
};

export default CreateShortenUrl;
