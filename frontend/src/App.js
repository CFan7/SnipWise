import React, { useState } from "react";
import { BrowserRouter as Router, Route, Routes, Link, Navigate } from "react-router-dom";
import Home from "./app/Home/Home";
import AboutUs from "./app/AboutUs/Aboutus";
import Plans from "./app/Plans/Plans";
import RegisterLogin from "./app/RegisterLogin/RegisterLogin";
import CreateShortenUrl from "./app/CreateShortenUrl/CreateShortenUrl";
import Account from "./app/Account/Account";
import "./App.css";

function App() {
  const [user, setUser] = useState(null);

  const handleLogin = (userInfo) => {
    setUser(userInfo);
  };

  return (
    <Router>
      <div className="App">
        <nav className="navbar">
          {user ? (
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
          <Route path="/create" element={user ? <CreateShortenUrl /> : <Navigate to="/" />} />
          <Route path="/account" element={user ? <Account user={user} /> : <Navigate to="/" />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
