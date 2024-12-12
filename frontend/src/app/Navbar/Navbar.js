import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import './Navbar.css';
import '@fortawesome/fontawesome-free/css/all.min.css'; // Font Awesome CSS 임포트

const Navbar = () => {
    const location = useLocation();
    const isHomePage = location.pathname === '/';

    return (
        <nav className="navbar">
            <Link to="/">
                <i className={`fas fa-home ${isHomePage ? '' : 'active'}`}></i>
            </Link>
            <Link to="/about">
                <i className={`fas fa-building ${isHomePage ? '' : 'active'}`}></i>
            </Link>
            <Link to="/plans">
                <i className={`fas fa-clipboard-list ${isHomePage ? '' : 'active'}`}></i>
            </Link>
            <Link to="/register">
                <i className={`fas fa-user ${isHomePage ? '' : 'active'}`}></i>
            </Link>
        </nav>
    );
};

export default Navbar;