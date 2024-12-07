import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://snip-wise.com/api", // Backend URL
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

export default axiosInstance;
