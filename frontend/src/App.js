import React from "react";
import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import AboutUs from "./app/AboutUs/Aboutus";
import CreateShortenUrl from "./app/CreateShortenUrl/CreateShortenUrl";
import Pricing from "./app/Pricing/Pricing";
import "./App.css";

function App() {
  const isLoggedIn = false;

  return (
    <Router>
      <div className="App">
        <nav className="navbar">
          <Link to="/about">About Us</Link>
          <Link to="/create">Create Shorten URL</Link>
          <Link to="/lookup">Look Up URL Mapping</Link>
          <Link to="/analysis">Data Analysis</Link>
          <Link to="/pricing">Pricing</Link>
          <Link to="/">{isLoggedIn ? "Username" : "Log in / Register"}</Link>
        </nav>
        <Routes>
          <Route path="/about" element={<AboutUs />} />
          <Route path="/create" element={<CreateShortenUrl />} />
          <Route path="/pricing" element={<Pricing />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
