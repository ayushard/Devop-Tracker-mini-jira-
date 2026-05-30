import { api } from './api';

export const taskService = {
  getTasks: async (params) => {
    const response = await api.get('/tasks', { params });
    return response.data;
  },
  createTask: async (payload) => {
    const response = await api.post('/tasks', payload);
    return response.data;
  },
  updateTask: async (id, payload) => {
    const response = await api.put(`/tasks/${id}`, payload);
    return response.data;
  },
  deleteTask: async (id) => {
    const response = await api.delete(`/tasks/${id}`);
    return response.data;
  },
  updateStatus: async (id, payload) => {
    const response = await api.put(`/tasks/${id}/status`, payload);
    return response.data;
  },
  addComment: async (id, payload) => {
    const response = await api.post(`/tasks/${id}/comments`, payload);
    return response.data;
  },
  uploadAttachment: async (id, file) => {
    const formData = new FormData();
    formData.append('file', file);
    const response = await api.post(`/tasks/${id}/attachments`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return response.data;
  },
};
