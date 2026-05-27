import axios from 'axios';
import { authHeader } from './authService';

const API_INVENTARIO = 'http://localhost:8080/inventario';
const API_ITENS = 'http://localhost:8080/itens';
const API_ARMAS = 'http://localhost:8080/itens/armas';

// Inventário
export const buscarInventario = (personagemId) =>
  axios.get(`${API_INVENTARIO}/${personagemId}`, { headers: authHeader() });

export const equiparItem = (dados) =>
  axios.post(`${API_INVENTARIO}/equipar`, dados, { headers: authHeader() });

// Itens
export const criarItem = (item) =>
  axios.post(API_ITENS, item, { headers: authHeader() });

export const buscarItensPorPersonagem = (personagemId) =>
  axios.get(`${API_ITENS}/personagem/${personagemId}`, { headers: authHeader() });

export const buscarItemPorId = (id) =>
  axios.get(`${API_ITENS}/${id}`, { headers: authHeader() });

export const deletarItem = (id) =>
  axios.delete(`${API_ITENS}/${id}`, { headers: authHeader() });

// Armas
export const criarArma = (arma) =>
  axios.post(API_ARMAS, arma, { headers: authHeader() });

export const buscarArmaPorId = (id) =>
  axios.get(`${API_ARMAS}/${id}`, { headers: authHeader() });

export const listarArmas = () =>
  axios.get(API_ARMAS, { headers: authHeader() });
