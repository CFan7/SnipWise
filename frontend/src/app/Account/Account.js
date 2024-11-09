// src/pages/Account.js
import React from "react";
import "./Account.css";

const Account = ({ user }) => {
  return (
    <div className="account-container">
      <h2>Account Information</h2>
      <p><strong>Username:</strong> {user.username}</p>
      <p><strong>Company:</strong> {user.company}</p>
      <p><strong>Date of Birth:</strong> {user.dob}</p>
      <p><strong>Email:</strong> {user.email}</p>
      <p><strong>Phone:</strong> {user.phone}</p>
      <p><strong>Password:</strong>{user.password}</p>
    </div>
  );
};

export default Account;
