import axios from 'axios';

const API = 'http://localhost:8080/auth';

// CORRIGIDO: registrar agora retorna UsuarioDTO com { id, nomeJogador, email, role }
export const registrar = async (dados) => {
  const res = await axios.post(`${API}/registrar`, dados);
  return res.data; // UsuarioDTO
};

// CORRIGIDO: login retorna UsuarioDTO com id — salva id no localStorage
export const login = async (dados) => {
  const res = await axios.post(`${API}/login`, dados);
  const usuario = res.data; // UsuarioDTO { id, nomeJogador, email, role }
  localStorage.setItem('usuarioId', usuario.id);
  localStorage.setItem('usuarioNome', usuario.nomeJogador);
  return usuario;
};

export const logout = () => {
  localStorage.removeItem('usuarioId');
  localStorage.removeItem('usuarioNome');
};

export const getUsuarioId = () => localStorage.getItem('usuarioId');
export const getUsuarioNome = () => localStorage.getItem('usuarioNome');
export const isAuthenticated = () => !!getUsuarioId();

export const listarUsuarios = () =>
  axios.get(`${API}/usuarios`);

export const buscarUsuarioPorId = (id) =>
  axios.get(`${API}/usuarios/${id}`);

export const buscarUsuarioPorEmail = (email) =>
  axios.get(`${API}/usuarios/email/${email}`);

export const atualizarUsuario = (id, dados) =>
  axios.put(`${API}/usuarios/${id}`, dados);

export const deletarUsuario = (id) =>
  axios.delete(`${API}/usuarios/${id}`);
