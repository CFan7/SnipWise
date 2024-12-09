import React from 'react';
import './Navbar.css'; // 네비게이션 바 스타일 시트 임포트

const Navbar = () => {
    return (
        <nav className="navbar">
            <a href="/">Home</a>
            <a href="/about">About</a>
            <a href="/contact">Contact</a>
        </nav>
    );
};

export default Navbar;