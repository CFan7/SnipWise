import React, { useContext } from "react";
import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import Home from "./app/Home/Home";
import AboutUs from "./app/AboutUs/Aboutus";
import Plans from "./app/Plans/Plans";
import RegisterLogin from "./app/RegisterLogin/RegisterLogin";
import CreateShortenUrl from "./app/CreateShortenUrl/CreateShortenUrl";
import Account from "./app/Account/Account";
import Navbar from "./app/Navbar/Navbar"; // Navbar 컴포넌트 추가
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
    login(userInfo); // UserContext의 login 함수 호출
  };

  if (loading) {
    return <div>Loading...</div>; // 로딩 상태 표시
  }

  const isLoggedIn = !!token;

  return (
    <Router>
      <div className="App">
        <Navbar /> {/* 네비게이션 바 포함 */}
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