import axios from 'axios';

// Configura la URL base según tu entorno
// Usa tu IP local o la dirección donde se ejecutan tus microservicios
const API_BASE_URL = 'http://localhost:8080'; 

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

export default apiClient;
