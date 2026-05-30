import axios from 'axios';
import { getUsuarioId } from './authService';

const API = '/personagens';

export const criarPersonagem = (dados) =>
  axios.post(API, dados);

export const buscarPorUsuario = (usuarioId) => {
  const id = usuarioId ?? getUsuarioId();
  return axios.get(`${API}/usuario/${id}`);
};

export const ganharXp = (id, xp) =>
  axios.post(`${API}/${id}/ganhar-xp`, null, { params: { xp } });

export const evoluirPersonagem = (id, arquetipo) =>
  axios.post(`${API}/${id}/evoluir`, null, { params: { arquetipo } });
