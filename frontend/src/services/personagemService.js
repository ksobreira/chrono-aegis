import axios from 'axios';
import { authHeader } from './authService';

const API = 'http://localhost:8080/personagens';

export const criarPersonagem = (dados) =>
  axios.post(API, dados, { headers: authHeader() });

export const buscarPorUsuario = (usuarioId) =>
  axios.get(`${API}/usuario/${usuarioId}`, { headers: authHeader() });

export const ganharXp = (id, xp) =>
  axios.post(`${API}/${id}/ganhar-xp`, null, {
    params: { xp },
    headers: authHeader(),
  });

export const evoluirPersonagem = (id, arquetipo) =>
  axios.post(`${API}/${id}/evoluir`, null, {
    params: { arquetipo },
    headers: authHeader(),
  });
