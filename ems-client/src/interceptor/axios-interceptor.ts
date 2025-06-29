import axios from 'axios';

const baseUrl = import.meta.env.VITE_BASE_URL || "";

const axiosInstance = axios.create({
    baseURL: baseUrl,
    headers: {
        "Content-Type": "application/json; charset=UTF-8",
        // 'Trace-ID': generateTraceId(),
    },
    withCredentials: true
});

export default axiosInstance;