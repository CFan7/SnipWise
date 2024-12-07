// src/pages/RegisterLogin.js
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./RegisterLogin.css";
import axiosInstance from "../../api/axiosInstance";

const RegisterLogin = ({}) => {
  const [isRegistering, setIsRegistering] = useState(true); // controls whether the user is registering or logging in
  const [formData, setFormData] = useState({
    username: "",
    dob: "",
    client_email: "",
    phone: "",
    passwd: ""
  });
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMessage(""); // Clear previous errors

    try {
      if (isRegistering) {
        // Registration API call
        const response = await axiosInstance.post("/clients", formData);
        console.log("Registration successful:", response.data);
        alert("Registration successful!");
      } else {
        // Login API call
        const response = await axiosInstance.post("/clients/login", {
          client_email: formData.client_email,
          passwd: formData.passwd,
        });
        console.log("Login successful:", response.data);
        alert("Login successful!");
      }

      // Navigate to the create URL page upon success
      navigate("/create");
    } catch (error) {
      if (error.response) {
        if (error.response.status === 401) {
          // Handle 401 Unauthorized
          setErrorMessage("Incorrect username or password!");
        } else {
          // Handle other errors
          setErrorMessage(error.response.data.message || "An error occurred");
        }
      } else {
        // Handle network or other errors
        setErrorMessage("Unable to connect to the server. Please try again.");
      }
    }
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
            <input
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
              required
            />

            <label>Date of Birth</label>
            <input
              type="date"
              name="dob"
              value={formData.dob}
              onChange={handleChange}
              required
            />

            <label>Phone</label>
            <input
              type="tel"
              name="phone"
              value={formData.phone}
              onChange={handleChange}
              required
            />
          </>
        )}

        <label>Email</label>
        <input
          type="email"
          name="client_email"
          value={formData.client_email}
          onChange={handleChange}
          required
        />

        <label>Password</label>
        <input
          type="password"
          name="passwd"
          value={formData.passwd}
          onChange={handleChange}
          required
        />

        <button type="submit">Submit</button>
      </form>

      {errorMessage && <p className="error-message">{errorMessage}</p>}
    </div>
  );
};

export default RegisterLogin;
