import { api } from './api';

export const projectService = {
  getProjects: async (params) => {
    const response = await api.get('/projects', { params });
    return response.data;
  },
  createProject: async (payload) => {
    const response = await api.post('/projects', payload);
    return response.data;
  },
  updateProject: async (id, payload) => {
    const response = await api.put(`/projects/${id}`, payload);
    return response.data;
  },
  deleteProject: async (id) => {
    const response = await api.delete(`/projects/${id}`);
    return response.data;
  },
};
