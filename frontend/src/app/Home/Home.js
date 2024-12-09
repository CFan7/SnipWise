import React from "react";
import "./Home.css";
import logo from "../../assets/SnipWise.png";
import slogan from "../../assets/Slogan.png";
import backgroundNeon from "../../assets/backgroundNeon.jpg";

const Home = () => {
    return (
        <div className="home-container" style={{ backgroundImage: `url(${backgroundNeon})` }}>
            <div className="home-images">
                <img src={logo} alt="Snipwise logo" />
                <img src={slogan} alt="Snipwise slogan" />
            </div>
        </div>
    );
};

export default Home;