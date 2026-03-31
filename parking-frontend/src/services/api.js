import axios from 'axios';

const api = axios.create({
  // Leave baseURL empty.
  // In dev (5173), Vite Proxy handles it.
  // In "One-Port" (8083), it stays on the same port.
  baseURL: '',
  withCredentials: true,
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 && !error.config.url.includes('/auth/me')) {
      console.error("401 ERROR DETECTED - SHUTTING DOWN REDIRECT FOR DEBUGGING");
      // window.location.href = '/oauth2/authorization/keycloak'; // <--- COMMENT THIS OUT
    }
    return Promise.reject(error);
  }
);

export default api;

export const checkIn = (data) => api.post('/api/v1/tickets/check-in', data);
export const checkOut = (ticketId) => api.post(`/api/v1/tickets/${ticketId}/check-out`);
export const getTickets = () => api.get('/api/v1/tickets/history');
export const getTestMessage = () => api.get('/api/v1/tickets/test');
export const getVehicleTypes = () => api.get('/api/v1/tickets/vehicle-types');
export const checkSession = () => api.get('/auth/me');