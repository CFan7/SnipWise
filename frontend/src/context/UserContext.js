// src/context/UserContext.js
import React, { createContext, useState, useEffect } from 'react';

export const UserContext = createContext(null);

export const UserProvider = ({ children }) => {
  const [token, setToken] = useState(null);
  const [expirationTime, setExpirationTime] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const savedToken = localStorage.getItem('token');
    const savedExpiration = localStorage.getItem('expirationTime');

    // console.log("After refresh - savedToken:", savedToken);
    // console.log("After refresh - savedExpiration:", savedExpiration);

    if (savedToken && savedExpiration) {
      const expTime = new Date(savedExpiration);
      const now = new Date();
      if (now < expTime) {
        // resume the token and expiration time when token is not expired
        // console.log('now < expTime');
        setToken(savedToken);
        setExpirationTime(savedExpiration);
      } else {
        // console.log("now" + now);
        // console.log("expTime" + expTime);
        // console.log('now >= expTime');
        // remove token and expiration time when token is expired
        logout();
      }
    }
    setLoading(false);
  }, []);

  const login = ({ jwt, expirationTime }) => {
    setToken(jwt);
    setExpirationTime(expirationTime);
    localStorage.setItem('token', jwt);
    localStorage.setItem('expirationTime', expirationTime);
  };

  const logout = () => {
    setToken(null);
    setExpirationTime(null);
    localStorage.removeItem('token');
    localStorage.removeItem('expirationTime');
  };

  return (
    <UserContext.Provider value={{ token, expirationTime, login, logout, loading }}>
      {children}
    </UserContext.Provider>
  );
};
