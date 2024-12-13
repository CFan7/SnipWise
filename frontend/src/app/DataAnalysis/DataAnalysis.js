import React, { useState } from 'react';
import axiosInstance from '../../api/axiosInstance';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';
import "./DataAnalysis.css";

const DataAnalysis = () => {
  const [shortUrl, setShortUrl] = useState('');
  const [hourlyData, setHourlyData] = useState([]);
  const [regionArr, setRegionData] = useState([]);

  const handleFetch = async () => {
    try {
      const response = await axiosInstance.get(`/url?shortUrl=${encodeURIComponent(shortUrl)}`);
      const data = response.data;
      
      const countsByHour = {};

      data.forEach(item => {
        const date = new Date(item.timestamp / 1000);
        const hourKey = date.setMinutes(0,0,0);
        countsByHour[hourKey] = (countsByHour[hourKey] || 0) + 1;
      });

      const sortedKeys = Object.keys(countsByHour).sort((a,b) => a - b);

      const hourly = sortedKeys.map(key => {
        const hourDate = new Date(parseInt(key,10));
        const displayHour = hourDate.toLocaleString([], {
        //   year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit",
          hour12: false,
        //   timeZoneName: "short"
        });
        return {
          hour: displayHour,
          clicks: countsByHour[key]
        };
      });

      setHourlyData(hourly);
    } catch (error) {
      console.error("Failed to fetch click data:", error);
    }
  };

  return (
    <div className="data-analysis-container">
      <h2 className="data-analysis-title">Data Analysis</h2>
      <input
        type="text"
        value={shortUrl}
        onChange={(e) => setShortUrl(e.target.value)}
        placeholder="Enter short URL"
        className="data-analysis-input"
      />
      <button onClick={handleFetch} className="data-analysis-button">Analyze</button>

      {hourlyData.length > 0 && (
        <div className="chart-container">
          <LineChart width={600} height={300} data={hourlyData} margin={{ top: 20, bottom: 20, left: 50, right: 50}}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="hour" interval={0}/>
            <YAxis />
            <Tooltip />
            <Legend />
            <Line type="monotone" dataKey="clicks" stroke="#8884d8" activeDot={{ r: 8 }} />
          </LineChart>
        </div>
      )}
    </div>
  );
};

export default DataAnalysis;