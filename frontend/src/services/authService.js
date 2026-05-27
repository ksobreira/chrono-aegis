import axios from 'axios';

const API = 'http://localhost:8080/auth';

export const registrar = (dados) =>
  axios.post(`${API}/registrar`, dados);

export const login = async (dados) => {
  const res = await axios.post(`${API}/login`, dados);
  const token = res.data;
  localStorage.setItem('token', token);
  return token;
};

export const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('usuarioId');
};

export const getToken = () => localStorage.getItem('token');

export const isAuthenticated = () => !!getToken();

export const listarUsuarios = () =>
  axios.get(`${API}/usuarios`, { headers: authHeader() });

export const buscarUsuarioPorId = (id) =>
  axios.get(`${API}/usuarios/${id}`, { headers: authHeader() });

export const buscarUsuarioPorEmail = (email) =>
  axios.get(`${API}/usuarios/email/${email}`, { headers: authHeader() });

export const atualizarUsuario = (id, dados) =>
  axios.put(`${API}/usuarios/${id}`, dados, { headers: authHeader() });

export const deletarUsuario = (id) =>
  axios.delete(`${API}/usuarios/${id}`, { headers: authHeader() });

export const authHeader = () => {
  const token = getToken();
  return token ? { Authorization: `Bearer ${token}` } : {};
};
