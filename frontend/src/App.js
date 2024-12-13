import React, { useContext } from "react";
import { BrowserRouter as Router, Route, Routes, Navigate , Link } from "react-router-dom";
import Home from "./app/Home/Home";
import AboutUs from "./app/AboutUs/Aboutus";
import Plans from "./app/Plans/Plans";
import RegisterLogin from "./app/RegisterLogin/RegisterLogin";
import CreateShortenUrl from "./app/CreateShortenUrl/CreateShortenUrl";
import Account from "./app/Account/Account";
import Management from "./app/Management/Management";
import "./App.css";
import { UserProvider, UserContext } from "./context/UserContext";
import UrlOverview from "./app/UrlOverview/UrlOverview";
import DataAnalysis from "./app/DataAnalysis/DataAnalysis";

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
    login(userInfo); // UserContext의 login 함수 호출
  };

  if (loading) {
    return <div>Loading...</div>; // 로딩 상태 표시
  }

  const isLoggedIn = !!token;

  return (
    <Router>
      <div className="App">
        <nav className="navbar">
          {isLoggedIn ? (
            <>
              <Link to="/create">Create Shorten URL</Link>
              <Link to="/lookup">Lookup URL</Link>
              <Link to="/management">Management</Link>
              <Link to="/analysis">Data Analysis</Link>
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
          <Route path="/lookup" element={isLoggedIn ? <UrlOverview /> : <Navigate to="/" />} />
          <Route path="/account" element={isLoggedIn ? <Account /> : <Navigate to="/" />} />
          <Route path="/management" element={isLoggedIn ? <Management /> : <Navigate to="/" />} />
          <Route path="/analysis" element={isLoggedIn ? <DataAnalysis /> : <Navigate to="/" />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;