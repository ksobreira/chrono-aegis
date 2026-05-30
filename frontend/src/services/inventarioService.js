import axios from 'axios';

const API_INVENTARIO = '/inventario';
const API_ITENS = '/itens';
const API_ARMAS = '/itens/armas';


export const buscarInventario = (personagemId) =>
  axios.get(`${API_INVENTARIO}/${personagemId}`);

export const equiparItem = (dto) =>
  axios.post(`${API_INVENTARIO}/equipar`, dto);

// Itens
export const criarItem = (item) =>
  axios.post(API_ITENS, item);

export const buscarItensPorPersonagem = (personagemId) =>
  axios.get(`${API_ITENS}/personagem/${personagemId}`);

export const buscarItemPorId = (id) =>
  axios.get(`${API_ITENS}/${id}`);

export const deletarItem = (id) =>
  axios.delete(`${API_ITENS}/${id}`);

// Armas
export const criarArma = (arma) =>
  axios.post(API_ARMAS, arma);

export const buscarArmaPorId = (id) =>
  axios.get(`${API_ARMAS}/${id}`);

export const listarArmas = () =>
  axios.get(API_ARMAS);
