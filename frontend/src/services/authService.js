import { api } from './api';

export const authService = {
  login: async (payload) => {
    const response = await api.post('/auth/login', payload);
    return response.data;
  },
  register: async (payload) => {
    const response = await api.post('/auth/register', payload);
    return response.data;
  },
};
