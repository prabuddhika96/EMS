import axios from 'axios';
import { RouteName } from '../constants/routeNames';

const baseUrl = import.meta.env.VITE_BASE_URL || "";

const loginRequestUrl = baseUrl + '/api/v1/auth/login'

const axiosInstance = axios.create({
    baseURL: baseUrl,
    headers: {
        "Content-Type": "application/json; charset=UTF-8",
    },
    withCredentials: true
});

axiosInstance.interceptors.response.use(
    (response) => response,

    async (error) => {
        const originalRequest = error.config;

        console.log(error.response)
        debugger


        if (error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            window.location.href = RouteName.Home;
        } else if (error.response.status === 403) {
            originalRequest._retry = true;

            console.log(error.response?.request)
            debugger
            if (!error.response?.request?.responseURL.startsWith(loginRequestUrl)) {
                window.location.href = RouteName.Home + "?sessionExpired=true";
            }
            else {
                window.location.href = RouteName.Unauthorized;
            }

        }

        return Promise.reject(error);
    }
);

export default axiosInstance;