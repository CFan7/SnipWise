import React, { useState, useEffect } from "react";
import "./Management.css";
import axiosInstance from "../../api/axiosInstance";

const Management = () => {
  const [companyName, setCompanyName] = useState("");
  const [createdCompany, setCreatedCompany] = useState("");

  const [companies, setCompanies] = useState([]);
  const [selectedCompany, setSelectedCompany] = useState("");
  const [groupName, setGroupName] = useState("");
  const [createdGroup, setCreatedGroup] = useState("");

  useEffect(() => {
    const fetchCompanies = async () => {
      try {
        const clientEmail = localStorage.getItem('clientEmail');
        const response = await axiosInstance.get(`/clients/${clientEmail}/access?type=company&role=owner`);
        const companyList = response.data;
        setCompanies(companyList);
        console.log("Fetched companies:", companyList);
        if (companyList.length > 0) {
            setSelectedCompany(companyList[0]);
          }
      } catch (error) {
        console.error("Failed to fetch companies:", error);
      }
    };
    fetchCompanies();
  }, []);

  const handleCompanySubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axiosInstance.post("/companies", { companyName: companyName });
      if (response.data) {
        console.log("Company created successfully:", response.data);
        alert(`Company "${companyName}" created successfully!`);
        setCompanyName(""); // clear the input field
      } else {
        if (response.status === 409) {
            alert("Company already exists with this name.");
        } else {
            alert("Failed to create company. Please try again.");
        }
      }
    } catch (error) {
      console.error("Failed to create company:", error);
      alert("Failed to create company. Please check the console for details.");
    }
  };

  const handleGroupSubmit = async (e) => {
    e.preventDefault();
    try {
        console.log("Selected company:", selectedCompany);
      const response = await axiosInstance.post(`/companies/${selectedCompany}/groups`, { groupName: groupName});
      if (response.data) {
        console.log("Group created successfully:", response.data);
        alert(`Group "${groupName}" created successfully under company "${selectedCompany}"!`);
        setGroupName("");
      } else {
        if (response.status === 409) {
            alert("Group already exists with this name.");
        } else {
            alert("Failed to create group. Please try again.");
        }
      }
    } catch (error) {
      console.error("Failed to create group:", error);
    }
  };

  return (
    <div className="management-container">
      {/* create companies */}
      <div className="management-box">
        <h3 className="subtitle">Create Company</h3>
        <form onSubmit={handleCompanySubmit} className="management-form">
          <label className="form-label">
            Company Name
            <input
              type="text"
              name="companyName"
              value={companyName}
              onChange={(e) => setCompanyName(e.target.value)}
              className="input-field"
              placeholder="Enter company name"
            />
          </label>
          <button type="submit" className="submit-button">
            Create Company
          </button>
        </form>
        {createdCompany && <p className="result">Created Company: {createdCompany}</p>}
      </div>

      {/* create groups */}
      <div className="management-box">
        <h3 className="subtitle">Create Group</h3>
        <form onSubmit={handleGroupSubmit} className="management-form">
          <label className="form-label">
            Select Company
            <select
              name="company"
              value={selectedCompany}
              onChange={(e) => setSelectedCompany(e.target.value)}
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
            Group Name
            <input
              type="text"
              name="groupName"
              value={groupName}
              onChange={(e) => setGroupName(e.target.value)}
              className="input-field"
              placeholder="Enter group name"
            />
          </label>
          <button type="submit" className="submit-button">
            Create Group
          </button>
        </form>
        {createdGroup && <p className="result">Created Group: {createdGroup}</p>}
      </div>
    </div>
  );
};

export default Management;
