import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import "./RegisterLogin.css";
import axiosInstance from "../../api/axiosInstance";
import { UserContext } from "../../context/UserContext";
import subbackground from "../../assets/subbackground.jpg";

const RegisterLogin = () => {
  const [isRegistering, setIsRegistering] = useState(true);
  const [formData, setFormData] = useState({
    clientName: "",
    dateOfBirth: "",
    clientEmail: "",
    phoneNumber: "",
    passwd: ""
  });
  const navigate = useNavigate();
  const { login } = useContext(UserContext);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      let response;
      if (isRegistering) {
        response = await axiosInstance.post("/clients", formData);
        alert(`Registration successful! Please login with your email: ${response.data.clientEmail}`);
        setIsRegistering(false);
      } else {
        response = await axiosInstance.post("/clients/login", {
          clientEmail: formData.clientEmail,
          passwd: formData.passwd,
        });
        alert("Login successful!");

        const { clientEmail, jwt, expirationTime } = response.data;

        // Store the token and expiration time in local storage
        await login({ clientEmail, jwt, expirationTime });

        navigate("/create");
      }
    } catch (error) {
      if (error.response) {
        if (error.response && error.response.status === 401) {
          alert("Incorrect username or password!");
        } else if (error.response && error.response.status === 409) {
          alert("User already exists with this email!");
        } else {
          alert(error.response.data.message || "An error occurred. Please try again.");
        }
      } else {
        alert("Unable to connect to the server. Please try again.");
      }
    }
  };

  return (
        <div
              className="plans-background"
              style={{
                backgroundImage: `url(${subbackground})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                height: '100vh',
                margin: 0,
                padding: 0,
              }}
        >
    <div className="register-container">
      <div className="content"> {}
        {/* <h2>{isRegistering ? "Register" : "Login"}</h2> */}

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
                name="clientName"
                value={formData.clientName}
                onChange={handleChange}
                required
              />

              <label>Date of Birth</label>
              <input
                type="date"
                name="dateOfBirth"
                value={formData.dateOfBirth}
                onChange={handleChange}
                required
              />

              <label>Phone</label>
              <input
                type="tel"
                name="phoneNumber"
                value={formData.phoneNumber}
                onChange={handleChange}
                required
              />
            </>
          )}

          <label>Email</label>
          <input
            type="email"
            name="clientEmail"
            value={formData.clientEmail}
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
      </div>
    </div>
    </div>
  );
};

export default RegisterLogin;