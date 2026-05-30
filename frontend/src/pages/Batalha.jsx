import { useState, useEffect } from 'react';
import { buscarPorUsuario } from '../services/personagemService';
import { iniciarBatalha, executarAcao } from '../services/combateService';
import './Batalha.css';

const ACOES = [
  { valor: 'ATACAR',    label: '⚔️ Atacar',   desc: 'Ataque físico' },
  { valor: 'USAR_ITEM', label: '🧪 Usar Item', desc: 'Usar item'     },
  { valor: 'FUGIR',     label: '💨 Fugir',     desc: 'Escapar'       },
];

const CLASSE_SPRITE  = { GUERREIRO: '⚔️', MAGO: '🧙', ARQUEIRO: '🏹', CLERIGO: '✨' };
const MONSTRO_SPRITE = { Goblin: '👺', Ogro: '👹', 'Dragão': '🐉' };

const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

const hpPct   = (cur, max) => Math.max(0, Math.min(100, (cur / max) * 100));
const hpClass = (pct) => `pix-hp-fill ${pct > 50 ? 'high' : pct > 20 ? 'mid' : 'low'}`;

export default function Batalha() {
  const usuarioId = localStorage.getItem('usuarioId') || 1;
  const [personagens, setPersonagens]     = useState([]);
  const [personagemSel, setPersonagemSel] = useState(null);
  const [batalha, setBatalha]             = useState(null);
  const [log, setLog]                     = useState('Escolha seu herói!');
  const [acaoSel, setAcaoSel]             = useState('ATACAR');
  const [loading, setLoading]             = useState(false);
  const [erro, setErro]                   = useState('');
  const [fimBatalha, setFimBatalha]       = useState(null);
  const [shakeEnemy, setShakeEnemy]       = useState(false);
  const [shakeHero, setShakeHero]         = useState(false);
  const [flashEnemy, setFlashEnemy]       = useState(false);
  const [flashHero, setFlashHero]         = useState(false);
  const [enemyDead, setEnemyDead]         = useState(false);
  const [heroDead, setHeroDead]           = useState(false);
  const [xpGanho, setXpGanho]             = useState(null);

  useEffect(() => {
    buscarPorUsuario(usuarioId).then((r) => setPersonagens(r.data)).catch(() => {});
  }, []);

  const triggerShake = (target) => {
    if (target === 'enemy') {
      setShakeEnemy(true); setFlashEnemy(true);
      setTimeout(() => { setShakeEnemy(false); setFlashEnemy(false); }, 400);
    } else {
      setShakeHero(true); setFlashHero(true);
      setTimeout(() => { setShakeHero(false); setFlashHero(false); }, 400);
    }
  };

  const handleIniciar = async () => {
    if (!personagemSel) return;
    setLoading(true); setErro('');
    setFimBatalha(null); setEnemyDead(false); setHeroDead(false); setXpGanho(null);
    setLog('Iniciando batalha...');
    try {
      const res = await iniciarBatalha({ personagemId: personagemSel.id });
      const d = res.data;
      setBatalha({ ...d, hpMonstroMax: d.hpMonstro ?? 100 });
      setLog(`Um ${d.nomeMonstro || 'Inimigo'} selvagem apareceu!`);
    } catch (err) {
      setErro(err.response?.data || 'Erro ao iniciar batalha.');
      setLog('Erro ao iniciar batalha.');
    } finally {
      setLoading(false);
    }
  };

  const handleAcao = async () => {
    if (!batalha || loading) return;
    setLoading(true);
    try {
      const res = await executarAcao({ sessionId: batalha.sessionId, tipoAcao: acaoSel });
      const r = res.data;

      setLog(r.log?.[0] || '');
      if (r.hpMonstro < batalha.hpMonstro) triggerShake('enemy');
      await sleep(700);

      setBatalha((prev) => ({
        ...prev,
        hpPersonagem: r.hpJogador ?? prev.hpPersonagem,
        hpMonstro:    r.hpMonstro ?? prev.hpMonstro,
      }));

      if (r.log?.[1]) {
        setLog(r.log[1]);
        if (r.hpJogador < batalha.hpPersonagem) triggerShake('hero');
        await sleep(700);
      }

      if (r.xpGanho) setXpGanho(r.xpGanho);

      if (r.fimDeBatalha) {
        if (r.vitoria) {
          setLog(`${batalha.nomeMonstro} foi derrotado!`);
          await sleep(600); setEnemyDead(true);
        } else {
          setLog(`${personagemSel.nome} foi derrotado...`);
          await sleep(600); setHeroDead(true);
        }
        await sleep(900);
        setFimBatalha(r.vitoria ? 'vitoria' : 'derrota');
        setBatalha(null);
      } else {
        setLog(`O que ${personagemSel.nome} vai fazer?`);
      }
    } catch (err) {
      setErro(err.response?.data || 'Erro ao executar ação.');
    } finally {
      setLoading(false);
    }
  };

  const resetar = () => {
    setBatalha(null); setFimBatalha(null);
    setEnemyDead(false); setHeroDead(false);
    setXpGanho(null); setErro('');
    setPersonagemSel(null);
    setLog('Escolha seu herói!');
  };

  const heroSprite  = personagemSel ? (CLASSE_SPRITE[personagemSel.classeBase] || '🧙') : '🧙';
  const enemySprite = batalha ? (MONSTRO_SPRITE[batalha.nomeMonstro] || '👾') : '👾';
  const heroHpPct_  = batalha ? hpPct(batalha.hpPersonagem, personagemSel?.vidaMax || 1) : 100;
  const enemyHpPct_ = batalha ? hpPct(batalha.hpMonstro, batalha.hpMonstroMax) : 100;

  const spriteEnemyClass = [
    'batalha-sprite-enemy',
    shakeEnemy ? 'shake' : '',
    flashEnemy ? 'flash' : '',
    enemyDead  ? 'dead'  : '',
  ].filter(Boolean).join(' ');

  const spriteHeroClass = [
    'batalha-sprite-hero',
    shakeHero ? 'shake' : '',
    flashHero ? 'flash' : '',
    heroDead  ? 'dead'  : '',
  ].filter(Boolean).join(' ');

  return (
    <div className="batalha-root">
      {erro && <div className="pix-erro">{erro}</div>}

      {/* Cena */}
      <div className="batalha-scene">
        {batalha && (
          <>
            <div className="batalha-hud-enemy">
              <div className="batalha-hud-name">{batalha.nomeMonstro}</div>
              <div className="batalha-hp-label">HP</div>
              <div className="pix-hp-bg">
                <div className={hpClass(enemyHpPct_)} style={{ width: enemyHpPct_ + '%' }} />
              </div>
              <div className="batalha-hp-num">{Math.max(0, batalha.hpMonstro)} / {batalha.hpMonstroMax}</div>
            </div>
            <div className="batalha-hud-hero">
              <div className="batalha-hud-name">{personagemSel?.nome}</div>
              <div className="batalha-hp-label">HP</div>
              <div className="pix-hp-bg">
                <div className={hpClass(heroHpPct_)} style={{ width: heroHpPct_ + '%' }} />
              </div>
              <div className="batalha-hp-num">{Math.max(0, batalha.hpPersonagem)} / {personagemSel?.vidaMax}</div>
            </div>
          </>
        )}
        <div className={spriteEnemyClass}>{batalha ? enemySprite : '👾'}</div>
        <div className={spriteHeroClass}>{heroSprite}</div>
      </div>

      {/* Log */}
      <div className="batalha-log">
        <p className="batalha-log-text">{log}</p>
        <div className="batalha-cursor" />
      </div>

      {/* Fim */}
      {fimBatalha && (
        <div className="batalha-end">
          <span className={fimBatalha === 'vitoria' ? 'batalha-end-vitoria' : 'batalha-end-derrota'}>
            {fimBatalha === 'vitoria' ? '🏆 VITÓRIA!' : '💀 DERROTA...'}
          </span>
          {xpGanho && <span className="batalha-end-xp">+{xpGanho} XP GANHO!</span>}
          <button className="batalha-retry-btn" onClick={resetar}>► NOVA BATALHA</button>
        </div>
      )}

      {/* Seleção */}
      {!batalha && !fimBatalha && (
        <div className="batalha-actions">
          <div className="batalha-select-title">ESCOLHA SEU HERÓI:</div>
          <div className="batalha-char-list">
            {personagens.length === 0 && (
              <div className="pix-loading">Nenhum personagem encontrado.</div>
            )}
            {personagens.map((p) => (
              <button key={p.id}
                className={`batalha-char-btn ${personagemSel?.id === p.id ? 'active' : ''}`}
                onClick={() => setPersonagemSel(p)}>
                <span style={{ fontSize: 24 }}>{CLASSE_SPRITE[p.classeBase] || '🧙'}</span>
                <div className="batalha-char-info">
                  <span className="batalha-char-nome">{p.nome}</span>
                  <span className="batalha-char-stats">
                    NV.{p.nivel} | ❤️ {p.vidaMax} | ⚔️ {p.ataque} | 🛡️ {p.defesa}
                  </span>
                </div>
              </button>
            ))}
          </div>
          <button className="batalha-start-btn" onClick={handleIniciar}
            disabled={!personagemSel || loading}>
            {loading ? 'INICIANDO...' : '⚔️ ENTRAR EM BATALHA'}
          </button>
        </div>
      )}

      {/* Ações */}
      {batalha && !fimBatalha && (
        <div className="batalha-actions">
          <div className="batalha-actions-grid">
            {ACOES.map(({ valor, label, desc }) => (
              <button key={valor}
                className={`batalha-acao-btn ${acaoSel === valor ? 'selected' : ''}`}
                onClick={() => setAcaoSel(valor)}
                disabled={loading}>
                {label}
                <span className="batalha-acao-desc">{desc}</span>
              </button>
            ))}
          </div>
          <button className="batalha-exec-btn" onClick={handleAcao} disabled={loading}>
            {loading ? 'EXECUTANDO...' : '► EXECUTAR'}
          </button>
        </div>
      )}
    </div>
  );
}