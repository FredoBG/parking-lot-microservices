import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8083',
  withCredentials: true,
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // 1. SAVE the current path (e.g., /test or /check-in)
      localStorage.setItem('redirect_after_login', window.location.pathname);

      console.log("Session expired. Saving path and redirecting...");

      // 2. Redirect to the BFF login trigger
      window.location.href = 'http://localhost:8083/oauth2/authorization/keycloak';

      return new Promise(() => {});
    }
    return Promise.reject(error);
  }
);

export default api;
export const checkIn = (data) => api.post('/api/v1/tickets/check-in', data);
export const getTestMessage = () => api.get('/api/v1/tickets/test'); // Your new test endpoint
export const getTickets = () => api.get('/api/v1/tickets/history');
