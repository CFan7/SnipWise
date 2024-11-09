// src/pages/RegisterLogin.js
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./RegisterLogin.css";

const RegisterLogin = ({ onLogin }) => {
  const [isRegistering, setIsRegistering] = useState(true); // controls whether the user is registering or logging in
  const [formData, setFormData] = useState({
    username: "",
    company: "",
    dob: "",
    email: "",
    phone: "",
    password: ""
  });
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // mock register or login logic
    onLogin(formData);
    // navigate to create page after successful registration or login
    navigate("/create");
  };

  return (
    <div className="register-container">
      <h2>{isRegistering ? "Register" : "Login"}</h2>
      
      <div className="toggle-buttons">
        <button
          className={`toggle-button ${isRegistering ? "active" : ""}`}
          onClick={() => setIsRegistering(true)}
        >
          Register
        </button>
        <button
          className={`toggle-button ${!isRegistering ? "active" : ""}`}
          onClick={() => setIsRegistering(false)}
        >
          Login
        </button>
      </div>

      <form onSubmit={handleSubmit}>
        {isRegistering && (
          <>
            <label>Username</label>
            <input type="text" name="username" value={formData.username} onChange={handleChange} required />
            
            <label>Company</label>
            <input type="text" name="company" value={formData.company} onChange={handleChange} required />
            
            <label>Date of Birth</label>
            <input type="date" name="dob" value={formData.dob} onChange={handleChange} required />
          </>
        )}
        
        <label>Email</label>
        <input type="email" name="email" value={formData.email} onChange={handleChange} required />
        
        <label>Phone</label>
        <input type="tel" name="phone" value={formData.phone} onChange={handleChange} required />

        <label>Password</label>
        <input type="password" name="password" value={formData.password} onChange={handleChange} required />
        
        <button type="submit">Submit</button>
      </form>
    </div>
  );
};

export default RegisterLogin;
