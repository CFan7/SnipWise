// src/pages/Home.js
import React from "react";
import "./Home.css";
import logo from "../../assets/SnipWise.png";
import slogan from "../../assets/Slogan.png";

const Home = () => {
    return (
        <div className="about-container">
            <div className="home-images">
                <img src={logo} alt="Snipwise logo" />
                <img src={slogan} alt="Snipwise slogan" />
            </div>
        </div>
      );
};

export default Home;