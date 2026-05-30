import axios from 'axios';
import { clearAuthStorage, getStoredToken } from '../utils/auth';

export const api = axios.create({
  baseURL: 'http://localhost:8080/api',
});

api.interceptors.request.use((config) => {
  const token = getStoredToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (error.response?.status === 401) {
      clearAuthStorage();
      window.location.href = '/login';
    }

    const message =
      error.response?.data?.message ||
      error.response?.data?.data?.message ||
      'Something went wrong. Please try again.';

    return Promise.reject(new Error(message));
  },
);
