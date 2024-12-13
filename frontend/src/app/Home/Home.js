import React from "react";
import "./Home.css";
import teamLogo from "../../assets/SnipWise.png";
import slogan from "../../assets/Slogan.png";
import subbackground from "../../assets/subbackground.jpg";

const Home = () => {
    return (
        <div className="home-container" style={{ backgroundImage: `url(${subbackground})` }}>
            <div className="home-images">
                <img src={teamLogo} alt="Team Logo" className="team-logo" />
                <img src={slogan} alt="Snipwise slogan" />
            </div>
        </div>
    );
};

export default Home;