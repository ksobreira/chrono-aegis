import axios from 'axios';

const API = 'http://localhost:8080/batalha';

export const iniciarBatalha = (dto) =>
  axios.post(`${API}/iniciar`, dto);

export const executarAcao = (dto) =>
  axios.post(`${API}/acao`, dto);
