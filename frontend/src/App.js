// src/App.js
import React, { useContext } from "react";
import { BrowserRouter as Router, Route, Routes, Link, Navigate } from "react-router-dom";
import Home from "./app/Home/Home";
import AboutUs from "./app/AboutUs/Aboutus";
import Plans from "./app/Plans/Plans";
import RegisterLogin from "./app/RegisterLogin/RegisterLogin";
import CreateShortenUrl from "./app/CreateShortenUrl/CreateShortenUrl";
import Account from "./app/Account/Account";
import "./App.css";
import { UserProvider, UserContext } from "./context/UserContext";

function App() {
  return (
    <UserProvider>
      <AppContent />
    </UserProvider>
  );
}

function AppContent() {
  const { token, login, loading } = useContext(UserContext);

  const handleLogin = (userInfo) => {
    login(userInfo);
  };

  if (loading) {
    // When the token is being checked, display a loading message
    return <div>Loading...</div>;
  }

  const isLoggedIn = !!token;

  return (
    <Router>
      <div className="App">
        <nav className="navbar">
          {isLoggedIn ? (
            <>
              <Link to="/create">Create Shorten URL</Link>
              <Link to="/retrieve">Retrieve URL</Link>
              <Link to="/analysis">Access Data Analysis</Link>
              <Link to="/account">Account</Link>
            </>
          ) : (
            <>
              <Link to="/">Home</Link>
              <Link to="/about">About Us</Link>
              <Link to="/plans">Plans</Link>
              <Link to="/register">Register/Login</Link>
            </>
          )}
        </nav>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/about" element={<AboutUs />} />
          <Route path="/plans" element={<Plans />} />
          <Route path="/register" element={<RegisterLogin onLogin={handleLogin} />} />
          <Route path="/create" element={isLoggedIn ? <CreateShortenUrl /> : <Navigate to="/" />} />
          <Route path="/account" element={isLoggedIn ? <Account /> : <Navigate to="/" />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
