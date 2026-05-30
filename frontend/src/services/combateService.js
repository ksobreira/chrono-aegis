import axios from 'axios';

const API = '/batalha';

export const iniciarBatalha = (dto) =>
  axios.post(`${API}/iniciar`, dto);

export const executarAcao = (dto) =>
  axios.post(`${API}/acao`, dto);
