// src/context/UserContext.js
import React, { createContext, useState, useEffect } from 'react';

export const UserContext = createContext(null);

export const UserProvider = ({ children }) => {
  const [clientEmail, setClientEmail] = useState(null);
  const [token, setToken] = useState(null);
  const [expirationTime, setExpirationTime] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const savedEmail = localStorage.getItem('clientEmail');
    const savedToken = localStorage.getItem('token');
    const savedExpiration = localStorage.getItem('expirationTime');

    // console.log("After refresh - savedToken:", savedToken);
    // console.log("After refresh - savedExpiration:", savedExpiration);

    if (savedEmail && savedToken && savedExpiration) {
      const expTime = new Date(savedExpiration);
      const now = new Date();
      if (now < expTime) {
        // resume the token and expiration time when token is not expired
        // console.log('now < expTime');
        setClientEmail(savedEmail);
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

  const login = ({ clientEmail, jwt, expirationTime }) => {
    setClientEmail(clientEmail);
    setToken(jwt);
    setExpirationTime(expirationTime);
    localStorage.setItem('clientEmail', clientEmail);
    localStorage.setItem('token', jwt);
    localStorage.setItem('expirationTime', expirationTime);
  };

  const logout = () => {
    setClientEmail(null);
    setToken(null);
    setExpirationTime(null);
    localStorage.removeItem('clientEmail');
    localStorage.removeItem('token');
    localStorage.removeItem('expirationTime');
  };

  return (
    <UserContext.Provider value={{ clientEmail, token, expirationTime, login, logout, loading }}>
      {children}
    </UserContext.Provider>
  );
};
