import React from "react";
import "./AboutUs.css";
import teamLogo from "../../assets/SnipWise.png";
import Anwar from "../../assets/Anwar.jpeg";
import Yufan from "../../assets/Yufan.jpeg";
import SangWoo from "../../assets/SangWoo.jpeg";
import Peilin from "../../assets/Peilin.jpeg";

const AboutUs = () => {
  return (
    <div className="about-container">
      <img src={teamLogo} alt="Team Logo" className="team-logo" />

      <div className="mission-section">
        <h2>Mission</h2>
        <p>To provide the best URL shortening service</p>
        <h2>Vision</h2>
        <p>To be the leading URL shortening provider for bussiness</p>
        <h2>Value</h2>
        <p>Secure, Wise, and Customer Satisfaction</p>
      </div>
      
      <h2 className="founder-title">Our Founders</h2>

      <div className="team-member">
        <img src={Anwar} alt="SnipWise" className="team-image" />
        <div className="team-info">
          <h2>Anwar Khaddaj</h2>
          <p>Applied Mathematician</p>
        </div>
      </div>
      <div className="team-member">
        <img src={Yufan} alt="SnipWise" className="team-image" />
        <div className="team-info">
          <h2>Yufan Wang</h2>
          <p>Computer Science</p>
        </div>
      </div>
      <div className="team-member">
        <img src={SangWoo} alt="SnipWise" className="team-image" />
        <div className="team-info">
          <h2>SangWoo Park</h2>
          <p>Electrical & Computer Engineering</p>
        </div>
      </div>
      <div className="team-member">
        <img src={Peilin} alt="SnipWise" className="team-image" />
        <div className="team-info">
          <h2>Peilin Shen</h2>
          <p>Computer Science</p>
        </div>
      </div>

      <h2 className="contact-title">Contact Us</h2>
      <p className="contact-info">contactus@snipwise.com</p>
    </div>
  );
};

export default AboutUs;