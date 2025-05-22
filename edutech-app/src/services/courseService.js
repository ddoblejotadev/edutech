import axios from 'axios';
import { API_URL, COURSE_ENDPOINTS } from '../config/api';
import { getToken } from './authService';

// Obtener todos los cursos
export const getAllCourses = async () => {
  try {
    const token = await getToken();
    const response = await axios.get(`${API_URL}${COURSE_ENDPOINTS.GET_ALL}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    
    return response.data;
  } catch (error) {
    console.error('Error al obtener cursos:', error);
    throw error;
  }
};

// Obtener un curso por ID
export const getCourseById = async (courseId) => {
  try {
    const token = await getToken();
    const response = await axios.get(`${API_URL}${COURSE_ENDPOINTS.GET_BY_ID(courseId)}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    
    return response.data;
  } catch (error) {
    console.error(`Error al obtener curso ${courseId}:`, error);
    throw error;
  }
};
