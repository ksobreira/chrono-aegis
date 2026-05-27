import axios from 'axios';
import { authHeader } from './authService';

const API = 'http://localhost:8080/batalha';

export const iniciarBatalha = (dados) =>
  axios.post(`${API}/iniciar`, dados, { headers: authHeader() });

export const executarTurno = (batalhaId, acao) =>
  axios.post(`${API}/${batalhaId}/turno`, acao, { headers: authHeader() });

export const buscarBatalha = (batalhaId) =>
  axios.get(`${API}/${batalhaId}`, { headers: authHeader() });
