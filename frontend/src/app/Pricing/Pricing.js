import React from "react";
import "./Pricing.css";

const Pricing = () => {
  return (
    <div className="pricing-container">
      <h1 className="pricing-title">Pricing</h1>
      <div className="pricing-cards">
        <div className="pricing-card">
          <div className="price-circle">
            <p>$149.99</p>
            <p>per month</p>
          </div>
          <h2 className="plan-title">Enterprise Basic</h2>
          <ul className="plan-features">
            <li>Around 5,000 active links</li>
            <li>Limited Analytics</li>
            <li>Expiry dates less than 3 years</li>
          </ul>
        </div>

        <div className="pricing-card">
          <div className="price-circle">
            <p>$249.99</p>
            <p>per month</p>
          </div>
          <h2 className="plan-title">Enterprise Premium</h2>
          <ul className="plan-features">
            <li>10,000+ active links</li>
            <li>Dynamic Analytics</li>
            <li>Extended Expiry dates</li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default Pricing;
