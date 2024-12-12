import React, { useState, useEffect, useContext } from "react";
import "./Account.css";
import axiosInstance from "../../api/axiosInstance";
import { UserContext } from "../../context/UserContext";

const Account = () => {
  const [clientInfo, setClientInfo] = useState(null);
  const [loading, setLoading] = useState(true);
  const { logout } = useContext(UserContext);

  const clientEmail = localStorage.getItem("clientEmail");

  useEffect(() => {
    const fetchClientInfo = async () => {
      try {
        const response = await axiosInstance.get(`/clients/${clientEmail}`);
        setClientInfo(response.data);
      } catch (error) {
        console.error("Failed to fetch client info:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchClientInfo();
  }, [clientEmail]);

  if (loading) {
    return <div className="account-container">Loading account information...</div>;
  }

  return (
    <div className="account-container">
      <h2 className="title">Account Information</h2>
      {clientInfo ? (
        <div className="account-details">
          <p className="detail"><strong>Username:</strong> {clientInfo.clientName}</p>
          <p className="detail"><strong>Email:</strong> {clientInfo.clientEmail}</p>
          <p className="detail"><strong>Date of Birth:</strong> {clientInfo.dateOfBirth}</p>
          <p className="detail"><strong>Phone Number:</strong> {clientInfo.phoneNumber}</p>
        </div>
      ) : (
        <p className="error">Unable to load account information. Please try again later.</p>
      )}
      <br />
      <button className="submit-button" onClick={logout}>Logout</button>
    </div>
  );
};

export default Account;
