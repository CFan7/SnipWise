import React from "react";
import "./AboutUs.css";
import teamLogo from "../../assets/SnipWise.png";
import Anwar from "../../assets/Anwar.jpeg";
import Yufan from "../../assets/Yufan.jpeg";
import SangWoo from "../../assets/SangWoo.jpeg";
import Peilin from "../../assets/Peilin.jpeg";
import subbackground from "../../assets/subbackground.jpg";

const Value = ({ emoji, title }) => (
  <div className="value-item">
    <span className="value-emoji">{emoji}</span>
    <span className="value-title">{title}</span>
  </div>
);

const AboutUs = () => {
  const values = [
    { emoji: '🔒', title: 'Security' },
    { emoji: '🤝', title: 'Partnership' },
    { emoji: '😊', title: 'Customer Satisfaction and Empowerment'},
    { emoji: '📊', title: 'Data-Driven Excellence' },
    { emoji: '⚙️', title: 'Focus and Efficiency' },
    ];

    return (
      <div
      className="about-background" // 배경 스타일이 적용될 div
      style={{
        backgroundImage: `url(${subbackground})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        height: '100vh',
        margin: 0,
        padding: 0,
      }}
      >
      <div className="about-container">
        <h1 className="about-title">About Us</h1>
        <img src={teamLogo} alt="Team Logo" className="team-logo" />

        <div className="mission-section">
          <h2>Mission</h2>
          <p>To empower small businesses and startups with smart, data-driven link management tools that enhance efficiency, engagement, and decision-making</p>
          <h2>Vision</h2>
          <p>To become the go-to platform for small businesses and startups to unlock the power of link data, transforming link management into a strategic, insight-driven advantage.</p>
          {/* <h2>Value</h2>
          <p>Secure, Wise, and Customer Satisfaction</p> */}
          <h2>Values</h2>
          <div className="values-section">
            {values.map((value, index) => (
              <Value key={index} emoji={value.emoji} title={value.title} />
            ))}
          </div>
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
      </div>
  );
};

export default AboutUs;