import React, { useState, useEffect } from "react";
import "./UrlOverview.css";
import axiosInstance from "../../api/axiosInstance";

const UrlOverview = () => {
  const [companies, setCompanies] = useState([]);
  const [groups, setGroups] = useState([]);

  const [selectedCompany, setSelectedCompany] = useState("");
  const [selectedGroup, setSelectedGroup] = useState("");
  const [urls, setUrls] = useState([]);
  const [isFetched, setIsFetched] = useState(false);

  const clientEmail = localStorage.getItem('clientEmail');

  useEffect(() => {
    const fetchCompanies = async () => {
      try {
        const companyResponse = await axiosInstance.get(`/clients/${clientEmail}/access?type=company&role=owner`);
        const companyList = companyResponse.data;
        setCompanies(companyList);
        if (companyList.length > 0) {
          setSelectedCompany(companyList[0]);
        }
      } catch (error) {
        console.error("Failed to fetch companies:", error);
      }
    };
    fetchCompanies();
  }, [clientEmail]);

  useEffect(() => {
    const fetchGroups = async () => {
      if (selectedCompany) {
        try {
          const groupResponse = await axiosInstance.get(`/companies/${selectedCompany}/groups`);
          const groupList = groupResponse.data;
          setGroups(groupList);
          if (groupList.length > 0) {
            setSelectedGroup(groupList[0].groupId);
          } else {
            setSelectedGroup("");
          }
        } catch (error) {
          console.error("Failed to fetch groups:", error);
        }
      } else {
        setGroups([]);
        setSelectedGroup("");
      }
    };
    fetchGroups();
  }, [selectedCompany]);

  const handleCompanyChange = (e) => {
    setSelectedCompany(e.target.value);
    setUrls([]);
    setIsFetched(false);
  };

  const handleGroupChange = (e) => {
    setSelectedGroup(e.target.value);
    setUrls([]);
    setIsFetched(false);
  };

  const handleFetchUrls = async () => {
    if (selectedCompany && selectedGroup) {
      try {
        const response = await axiosInstance.get(`/companies/${selectedCompany}/groups/${selectedGroup}/urls`);
        const urlList = response.data;
        setUrls(urlList);
      } catch (error) {
        console.error("Failed to fetch urls:", error);
        setUrls([]);
      } finally {
        setIsFetched(true);
      }
    } else {
      alert("Please select a company and a group first.");
    }
  };

  return (
    <div className="overview-container">
      <h2 className="title">URL Overview</h2>
      <div className="form-section">
        <label className="form-label">
          Select Company
          <select
            name="company"
            value={selectedCompany}
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
            value={selectedGroup}
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

        <button className="submit-button" onClick={handleFetchUrls}>Fetch URLs</button>
      </div>

      {isFetched && (urls.length > 0 ? (
        <table className="url-table">
          <thead>
            <tr>
              <th>Original URL</th>
              <th>Short URL</th>
              <th>Expiration Time</th>
              <th>Is Activated</th>
            </tr>
          </thead>
          <tbody>
            {urls.map((u, index) => (
              <tr key={index}>
                <td>{u.originalUrl}</td>
                <td>{u.shortUrl}</td>
                <td>{u.expirationTime}</td>
                <td>
                  {u.isActivated ? (
                    <span className="activated-true">True</span>
                  ) : (
                    <span className="activated-false">False</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>No URLs found for selected company and group. Try fetching again after some data is created.</p>
      ))}
    </div>
  );
};

export default UrlOverview;